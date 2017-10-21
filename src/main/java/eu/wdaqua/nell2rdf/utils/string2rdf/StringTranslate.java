package eu.wdaqua.nell2rdf.utils.string2rdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ReifiedStatement;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;

import eu.wdaqua.nell2rdf.NellOntologyConverter;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.models.LineInstanceJOIN;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import eu.wdaqua.nell2rdf.utils.UriNell;
import eu.wdaqua.nell2rdf.utils.string2rdf.components.ComponentRDF;
import eu.wdaqua.nell2rdf.utils.string2rdf.components.ComponentRDFBuilder;

/**
 * 
 * @author Quentin Cruzille & Jose M. Gimenez-Garcia
 * 
 * Cree un model Jena et le rempli avec les informations extraites du fichier de Nell.
 *
 */
public class StringTranslate {

    private final Logger		log						= Logger.getLogger(StringTranslate.class);

    private final String		metadata;
    private final boolean deleteOriginalTriples;
    private OutputStream outputStream = null;
    
    private LineInstanceJOIN belief;

	/**
	 * Model contenant contenant les triplets de Nell.
	 */
	private Model model;
	
	/**
	 * URI pour notre prefixe.
	 */
	private final String		base;

	/**
	 * base prefix + /ontology for classes and property (T-Box related stuffs)
	 */
	private String resourceBase;
	private String ontologyBase;
	private String provenanceResourceBase;

	private String lang;
	
	/**
	 * URI pour le prefixe SKOS.
	 */
	private String skos;
	
	/**
	 * URI pour le prefixe RDFS.
	 */
	private final String		rdfs;

	private final String		xsd;

	/**
	 * Propriete prefLabel de SKOS.
	 * (Jena n'implementant pas le vocabulaire SKOS, il est necessaire de la gerer manuellement).
	 */
	private Property prefLabel;
	
	public List<String> fail;
	public List<String> good;
	
	/**
	 * Constructeur, initialise le model et les prefixes.
	 */
	public StringTranslate(final String metadata, String lang, String file, boolean deleteOriginalTriples) {
		this.model = ModelFactory.createDefaultModel();
		this.base = UriNell.PREFIX;
		this.metadata = metadata;
		this.deleteOriginalTriples = deleteOriginalTriples;
		this.lang = lang;
		this.resourceBase = this.base + UriNell.NAMESPACE_END_RESOURCE;
		this.ontologyBase = this.base + UriNell.NAMESPACE_END_ONTOLOGY;
		this.skos = "http://www.w3.org/2004/02/skos/core#";
		this.rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		this.xsd = "http://www.w3.org/2001/XMLSchema#";
//		this.model.setNsPrefix(UriNell.PREFIX_RESOURCE, this.resourceBase);
//		this.model.setNsPrefix(UriNell.PREFIX_ONTOLOGY, this.ontologyBase);
//		this.model.setNsPrefix(UriNell.PREFIX_METADATA, this.provenanceResourceBase);
//		this.model.setNsPrefix("skos", this.skos);
//		this.model.setNsPrefix("rdfs", this.rdfs);
//		this.model.setNsPrefix("xsd", this.xsd);
		this.prefLabel = this.model.createProperty(this.skos + "prefLabel");
		this.fail = new LinkedList<>();
		this.good = new LinkedList<>();

		// workaround to reduce memory consumption
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Prend un tableau de chaines de caracteres et les traduits en model Jena.
	 * @param nellData
	 */

	public void stringToRDF(final String nellData) {
		DatasetGraph graph = null;
		String[] split = nellData.split("\t");
		belief = new LineInstanceJOIN(split[0], split[1], split[2], split[3], split[4], Utility.DecodeURL(split[5]), split[6], split[7], split[8], split[9], split[10], split[11], Utility.DecodeURL(split[12]), nellData);
		switch (this.metadata) {
            case NellOntologyConverter.NONE:
                log.debug("Converting string to RDF without metadata");
                stringToRDFWithoutMetadata(belief);
                break;
            case NellOntologyConverter.REIFICATION:
                log.debug("Converting string to RDF using reification to attach metadata");
                stringToRDFWithReification(belief);
                break;
            case NellOntologyConverter.N_ARY:
                log.debug("Converting string to RDF using n-ary relations to attach metadata");
                stringToRDFWithNAry(belief);
                break;
            case NellOntologyConverter.QUADS:
                log.debug("Converting string to RDF using quads to attach metadata");
                graph = stringToRDFWithQuads(belief);
                break;
            case NellOntologyConverter.SINGLETON_PROPERTY:
                log.debug("Converting string to RDF using singleton property to attach metadata");
                stringToRDFWithSingletoProperty(belief);
                break;
            case NellOntologyConverter.NDFLUENTS:
                log.debug("Converting string to RDF using NdFluents to attach metadata");
                stringToRDFWithNdFluents(belief);
                break;
            default:
                log.warn("Metadata model not recognized. Converting string to RDF without metadata");
                stringToRDFWithoutMetadata(belief);
                break;
        }

		//workaround to reduce memory consumption
		
		if (graph != null) {
			RDFDataMgr.write(outputStream, graph, Lang.NQUADS) ;
		}
		
		this.model.write(this.outputStream, this.lang);
		this.model.removeAll();
	}

	private void stringToRDFWithNdFluents(final LineInstanceJOIN belief) {
        // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(belief);

        // Create NdFluents triples
        Property property;
        Resource resource;
        property = model.getProperty(UriNell.PREFIX_NDFLUENTS, UriNell.PROPERTY_PROVENANCE_PART_OF);
        resource =  model.createResource(UriNell.createSequentialUri(triple.getSubject().getURI()), model.getResource(UriNell.CLASS_PROVENANCE_PART));
        resource.addProperty(property, triple.getSubject());
        if (triple.getObject().isResource()) {
            resource = model.createResource(UriNell.createSequentialUri(triple.getObject().asResource().getURI()), model.getResource(UriNell.CLASS_PROVENANCE_PART));
            resource.addProperty(property, triple.getObject());
        }

        property = model.getProperty(UriNell.PREFIX_NDFLUENTS, UriNell.PROPERTY_PROVENANCE_EXTENT);
        // resource = model.createResource(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()), model.getResource(UriNell.CLASS_BELIEF));
        resource = model.createResource(UriNell.createAnchorUri(belief.getEntity(), belief.getRelation(), belief.getValue()), model.getResource(UriNell.CLASS_BELIEF));
        triple.getSubject().addProperty(property, resource);
        if (triple.getObject().isResource()) {
            triple.getObject().asResource().addProperty(property, resource);
        }

        // Attach metadata to reification statement
        attachMetadata(resource, belief);
        
     // Delete original triples if requested
        if (deleteOriginalTriples) {
            triple.remove();
        }
    }

	private void stringToRDFWithSingletoProperty(final LineInstanceJOIN belief) {

        // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(belief);

        // Create the Singleton Property
//        Property singletonProperty = createSingletonPropertyOf(triple.getPredicate());
        // final Property singletonProperty = model.getProperty(UriNell.createAnchorUri(triple.getPredicate().getURI(), belief.isCandidate()));
        final Property singletonProperty = model.getProperty(UriNell.createAnchorUri(belief.getEntity(), belief.getRelation(), belief.getValue()));
        singletonProperty.addProperty(RDF.type, model.getResource(UriNell.SINGLETON_PROPERTY_OF));

        // Attach metadata to reification statement
        attachMetadata(singletonProperty, belief);

        // Delete original triples if requested
        if (deleteOriginalTriples) {
            triple.remove();
        }

	}

	private DatasetGraph stringToRDFWithQuads(final LineInstanceJOIN belief) {

		// Create Named Graph
        //final Resource tripleId = model.createResource(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()), model.getResource(UriNell.CLASS_BELIEF));
        final Resource tripleId = model.createResource(UriNell.createAnchorUri(belief.getEntity(), belief.getRelation(), belief.getValue()), model.getResource(UriNell.CLASS_BELIEF));
        Model model = ModelFactory.createDefaultModel();
        
        DatasetGraph dataset = TDBFactory.createDatasetGraph();
        
//        dataset.addNamedModel(tripleId.getURI(), model);
        
        // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(model, belief);
        
    	dataset.add(tripleId.asNode(), triple.getSubject().asNode(), triple.getPredicate().asNode(), triple.getObject().asNode());
        
//        dataset.getNamedModel(tripleId.getURI());
//        dataset.addNamedModel(tripleId.getURI(), model);

        // Attach metadata to triple ID
        attachMetadata(tripleId, belief);
        
     // Delete original triples if requested
        if (deleteOriginalTriples) {
            triple.remove();
        }
        
        return dataset;
	}

	private void stringToRDFWithNAry(final LineInstanceJOIN belief) {

	    // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(belief);

        // Create N-Ary triples
        Property predicate = this.model.getProperty(triple.getPredicate().toString());
        Property predicate1 = this.model.getProperty(triple.getPredicate().toString() + "_statement");
        Property predicate2 = this.model.getProperty(triple.getPredicate().toString() + "_value");
        predicate.addProperty(this.model.getProperty(UriNell.PROPERTY_SUBJECT_PROPERTY), predicate1);
        predicate.addProperty(this.model.getProperty(UriNell.PROPERTY_OBJECT_PROPERTY), predicate2);
        
//        RDFNode statement = model.createResource(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()), model.getResource(UriNell.CLASS_BELIEF));
        RDFNode statement = model.createResource(UriNell.createAnchorUri(belief.getEntity(), belief.getRelation(), belief.getValue()), model.getResource(UriNell.CLASS_BELIEF));
        
        triple.getSubject().addProperty(predicate1,statement);
        statement.asResource().addProperty(predicate2,triple.getObject());

        // Attach metadata to reification statement
        attachMetadata(statement.asResource(), belief);

        // Delete original triples if requested
        if (deleteOriginalTriples) {
            triple.remove();
        }

	}

	private void stringToRDFWithReification(final LineInstanceJOIN belief) {

		// Create normal triple without metadata
		final Statement triple = stringToRDFWithoutMetadata(belief);

		// Create reification
//		ReifiedStatement statement = triple.createReifiedStatement(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()));
		ReifiedStatement statement = triple.createReifiedStatement(UriNell.createAnchorUri(belief.getEntity(), belief.getRelation(), belief.getValue()));
//        ReifiedStatement statement = triple.createReifiedStatement(createSequentialProvenanceResourceUri(UriNell.RESOURCE_BELIEF,belief.isCandidate()));

		// Attach metadata to reification statement
		attachMetadata(statement, belief);

        // Delete original triples if requested
        if (deleteOriginalTriples) {
            triple.remove();
        }
	}

//    private void createProvenanceOntology() {
//        // Create properties
//        this.model.createProperty(UriNell.PROPERTY_ASSOCIATED_WITH);
//        this.model.createProperty(UriNell.PROPERTY_GENERATED_BY);
//        this.model.createProperty(UriNell.PROPERTY_ITERATION);
//        this.model.createProperty(UriNell.PROPERTY_ITERATION_OF_PROMOTION);
//        this.model.createProperty(UriNell.PROPERTY_PROBABILITY);
//        this.model.createProperty(UriNell.PROPERTY_PROBABILITY_OF_BELIEF);
//        this.model.createProperty(UriNell.PROPERTY_AT_TIME);
//        this.model.createProperty(UriNell.PROPERTY_SOURCE);
//        this.model.createProperty(UriNell.PROPERTY_TOKEN);
//        this.model.createProperty(UriNell.PROPERTY_TOKE_ENTITY);
//        this.model.createProperty(UriNell.PROPERTY_RELATION_VALUE);
//        this.model.createProperty(UriNell.PROPERTY_GENERALIZATION_VALUE);
//        this.model.createProperty(UriNell.PROPERTY_LATITUDE_VALUE);
//        this.model.createProperty(UriNell.PROPERTY_LONGITUDE_VALUE);
//
//	    // Create classes
//        this.model.createResource(UriNell.CLASS_BELIEF);
//        this.model.createResource(UriNell.CLASS_CANDIDATE_BELIEF);
//        this.model.createResource(UriNell.CLASS_PROMOTED_BELIEF);
//        this.model.createResource(UriNell.CLASS_COMPONENT);
//        this.model.createResource(UriNell.CLASS_COMPONENT_EXECUTION);
//        this.model.createResource(UriNell.CLASS_TOKEN);
//        this.model.createResource(UriNell.CLASS_TOKEN_RELATION);
//        this.model.createResource(UriNell.CLASS_TOKEN_GENERALIZATION);
//        this.model.createResource(UriNell.CLASS_TOKEN_GEO);
//
//        if (this.metadata == NellOntologyConverter.NDFLUENTS) {
//            this.model.setNsPrefix(UriNell.PREFIX_NDFLUENTS, UriNell.NAMESPACE_NDFLUENTS);
//        }
//    }

//    private void createComponents() {
//        this.model.createResource(this.provenanceResourceBase + ConstantList.ALIASMATCHER);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.CMC);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.CPL);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.LE);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.LATLONG);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.MBL);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.OE);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.ONTOLOGYMODIFIER);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.PRA);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.RULEINFERENCE);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.SEAL);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.SEMPARSE);
//        this.model.createResource(this.provenanceResourceBase + ConstantList.SPREADSHEETEDITS);
//    }

	private void attachMetadata(final Resource resource, final LineInstanceJOIN belief) {
		Property predicate;
		RDFNode object;
		
		// createProvenanceOntology();
        resource.addProperty(RDF.type, model.getResource(UriNell.CLASS_BELIEF));

        // Add iteration of promotion
        predicate = this.model.getProperty(UriNell.PROPERTY_ITERATION_OF_PROMOTION);
        object = this.model.createTypedLiteral(belief.getNrIterationsInt(),XSDDatatype.XSDinteger);
        resource.addProperty(predicate, object);

        if (belief.getProbabilityDouble() != null) {
            // Add probability
            predicate = this.model.getProperty(UriNell.PROPERTY_PROBABILITY_OF_BELIEF);
            object = this.model.createTypedLiteral(belief.getProbabilityDouble(), XSDDatatype.XSDdecimal);
            resource.addProperty(predicate, object);
        }

        belief.getListComponents().forEach((String K, Header V) -> {
        	ComponentRDF component = ComponentRDFBuilder.build(V, resource);
        	component.addTriples();
        	});
	}

	private Statement stringToRDFWithoutMetadata(final LineInstanceJOIN belief) {
		return stringToRDFWithoutMetadata(this.model, belief);
	}
	
	private Statement stringToRDFWithoutMetadata(Model model, final LineInstanceJOIN belief) {
		String[] nellData = belief.completeLine.split("\t");
		/* Traitement du sujet. */
		String[] nellDataSplit = belief.getEntity().split(":", 2);
		nellDataSplit[1] = nellDataSplit[1].replaceAll(":", "_");
		final Resource subject = getOrCreateRessource(nellDataSplit[1]);
		if (!nellData[6].equals(" ")) {
			findLabel(subject, nellData[6]);
		}

		if (!nellData[8].equals(" ")) {
			nellData[8].replaceAll("\"", "");
			subject.addProperty(this.prefLabel, nellData[8]);
		}

		if (!nellData[10].equals("")) {
			findType(nellData[10], subject);
		}
		
		/* Traitement du predicat. */
		Property relation = this.findRelation(belief.getRelation());
		
		/* Traitement de l'objet. */
		RDFNode object_node;
		nellDataSplit = belief.getValue().split(":", 2);
		if (nellDataSplit[0].equals("concept")) {
			/* Cas ou l'objet n'est pas un literal. */
			Resource object_resource;
			nellDataSplit[1] = nellDataSplit[1].replaceAll(":", "_");
			if (nellData[1].equals("generalizations")) {
				object_resource = getOrCreateRessourceClass(nellDataSplit[1]);
			} else {
				object_resource = getOrCreateRessource(nellDataSplit[1]);

				if (!nellData[11].equals("")) {
					findType(nellData[11], object_resource);
				}
			}

			if (!nellData[7].equals(" ")) {
				findLabel(object_resource, nellData[7]);
			}

			if (!nellData[9].equals(" ")) {
				nellData[9].replaceAll("\"", "");
				object_resource.addProperty(this.prefLabel, nellData[9]);
			}

			object_node = object_resource;
			subject.addProperty(relation, object_resource);
		} else {
			/*
			 * Cas ou l'objet est un literal, on verifie le cas specifique ou c'est une URL, on utilise xsd:string pour les autres par defaut, a voir comment les differencier par la suite.
			 */
			if (nellDataSplit[0].equals("http")) {
				final RDFDatatype datatype = XSDDatatype.XSDanyURI;
				final Literal object_literal = this.model.createTypedLiteral(nellData[2], datatype);
				object_node = object_literal;
				subject.addProperty(relation, object_literal);
			} else {
				BigInteger trueLiteral = null;
				try
				{
					trueLiteral = new BigInteger(nellData[2]);
					
				}
				catch(NumberFormatException e)
				{
					
				}

				if (trueLiteral == null) {
					final Literal object_literal = this.model.createTypedLiteral(nellData[2], XSDDatatype.XSDstring);
					object_node = object_literal;
					subject.addProperty(relation, object_literal);
				} else {
					final Literal object_literal = this.model.createTypedLiteral(trueLiteral, XSDDatatype.XSDinteger);
					object_node = object_literal;
					subject.addProperty(relation, object_literal);
				}
			}
		}

		return model.createStatement(subject, relation, object_node);
		//return ResourceFactory.createStatement(subject, relation, object_node);
	}
	
	/**
	 * Prend une chaine de caractere et verifie s'il existe deje une resource associee e cette chaene, si oui
	 * la renvoie, sinon, la cree.
	 */
	private Resource getOrCreateRessource(String string)
	{
		Resource resource;
		if((resource=model.getResource(this.resourceBase + string))==null)
		{
			resource=model.createResource(this.resourceBase + string);
		}
		return resource;
	}
	
	private Resource getOrCreateRessourceClass(String string)
	{
		Resource resource;
		if((resource=model.getResource(this.ontologyBase +string))==null)
		{
			resource=model.createResource(this.ontologyBase +string);
		}
		return resource;
	}

	/**
	 * Renvoi la propriete associee e une chaene de caracteres.
	 * @param relation
	 * @return
	 */
	private Property findRelation(String relation)
	{
		switch(relation)
		{
			case "generalizations":
				return (RDF.type);
				
			default:
				String[] s = relation.split(":", 2);
				Property p;
				s[1]=s[1].replaceAll(":", "_");
				if( (p=this.model.getProperty(this.ontologyBase +s[1])) == null )
				{
					p=this.model.createProperty(this.ontologyBase +s[1]);
				}
				return(p);	
		}
	}
	
	/**
	 * Parse la chaene de caracteres label pour trouver les differents label associes e subject.
	 * Associe les label trouves e subject avec RDFS.label et prefLabel avec SKOS.prefLabel.
	 * @param subject
	 * @param label
	 */
	private void findLabel(Resource subject, String label)
	{
		int i;
		String[] labelSplit = label.split("\" ");
		for(i=0;i<labelSplit.length;i++)
		{
			String l=labelSplit[i].replaceAll("\"", "");
			subject.addProperty(RDFS.label, l);
		}	
	}
	
	/**
	 * Parse la chaene de caracteres type pour trouver les classes associees e la resource subject.
	 * Associe les classes trouvees e subject avec RDF.type.
	 * @param type
	 * @param subject
	 */
	private void findType(String type, Resource subject)
	{
		String[] typeSplit = type.split(" ");
		for(int i=0;i<typeSplit.length;i++)
		{
			String[] trueType = typeSplit[i].split(":",2);
			if(trueType[0].equals("concept"))
			{
				trueType[1]=trueType[1].replaceAll(":", "_");
				trueType[1]=trueType[1].replaceAll("\"", "");
				Resource classe=this.getOrCreateRessourceClass(trueType[1]);
				subject.addProperty(RDF.type,classe);
			}
		}
	}

	/**
	 * Renvoie le model.
	 * @return
	 */
	public Model getModel()
	{
		return model;
	}
}
