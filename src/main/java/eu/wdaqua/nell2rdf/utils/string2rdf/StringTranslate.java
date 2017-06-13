package eu.wdaqua.nell2rdf.utils.string2rdf;

import eu.wdaqua.nell2rdf.NellOntologyConverter;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.models.LatLong;
import eu.wdaqua.nell2rdf.extract.metadata.models.LineInstanceJOIN;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Quentin Cruzille & Jose M. Gimenez-Garcia
 * 
 * Cree un model Jena et le rempli avec les informations extraites du fichier de Nell.
 *
 */
public class StringTranslate {

    public static Logger		log						= Logger.getLogger(StringTranslate.class);

    private final String		metadata;
    private final boolean       candidates;
    private OutputStream outputStream = null;

    private Map<String,Integer> numberSequences = new HashMap<>();

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
	private String provenanceOntologyBase;
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

	// Change to double if more than 2^31-1 (~ 2.14 billion) statements
	private int					statementNumber;

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
	public StringTranslate(final String prefix, final String metadata, String separator, String lang, boolean candidates, String file) {
		this.model = ModelFactory.createDefaultModel();
		this.base = prefix;
		this.metadata = metadata;
		this.candidates = candidates;
		this.lang = lang;
		this.resourceBase = this.base + ConstantList.NAMESPACE_END_RESOURCE + separator;
		this.ontologyBase = this.base + ConstantList.NAMESPACE_END_ONTOLOGY + separator;
		this.provenanceOntologyBase = this.base + ConstantList.NAMESPACE_END_PROVENANCE_ONTOLOGY + separator;
		this.provenanceResourceBase = this.base + ConstantList.NAMESPACE_END_PROVENANCE_RESOURCE + separator;
		this.skos = "http://www.w3.org/2004/02/skos/core#";
		this.rdfs = "http://www.w3.org/2000/01/rdf-schema#";
		this.xsd = "http://www.w3.org/2001/XMLSchema#";
		this.model.setNsPrefix(ConstantList.PREFIX_RESOURCE, this.resourceBase);
		this.model.setNsPrefix(ConstantList.PREFIX_ONTOLOGY, this.ontologyBase);
		this.model.setNsPrefix(ConstantList.PREFIX_PROVENANCE_RESOURCE, this.provenanceResourceBase);
		this.model.setNsPrefix(ConstantList.PREFIX_PROVENANCE_ONTOLOGY, this.provenanceOntologyBase);
		this.model.setNsPrefix("skos", this.skos);
		this.model.setNsPrefix("rdfs", this.rdfs);
		this.model.setNsPrefix("xsd", this.xsd);
		this.prefLabel = this.model.createProperty(this.skos + "prefLabel");
		this.fail = new LinkedList<>();
		this.good = new LinkedList<>();
		this.statementNumber = 0;

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

	public void stringToRDF(final String[] nellData) {
		switch (this.metadata) {
			case NellOntologyConverter.NONE:
			    log.debug("Converting string to RDF without metadata");
				stringToRDFWithoutMetadata(nellData);
				break;
			case NellOntologyConverter.REIFICATION:
                log.debug("Converting string to RDF using reification to attach metadata");
				stringToRDFWithReification(nellData);
				break;
			case NellOntologyConverter.N_ARY:
                log.debug("Converting string to RDF using n-ary relations to attach metadata");
				stringToRDFWithNAry(nellData);
				break;
			case NellOntologyConverter.QUADS:
                log.debug("Converting string to RDF using quads to attach metadata");
				stringToRDFWithQuads(nellData);
				break;
			case NellOntologyConverter.SINGLETON_PROPERTY:
                log.debug("Converting string to RDF using singleton property to attach metadata");
				stringToRDFWithSingletoProperty(nellData);
				break;
            case NellOntologyConverter.NDFLUENTS:
                log.debug("Converting string to RDF using NdFluents to attach metadata");
                stringToRDFWithNdFluents(nellData);
                break;
            default:
                log.debug("Metadata model not recognized. Converting string to RDF without metadata");
                stringToRDFWithoutMetadata(nellData);
                break;
		}

		//workaround to reduce memory consumption
		this.model.write(this.outputStream, this.lang);
		this.model.removeAll();
	}

	private void stringToRDFWithNdFluents(final String[] nellData) {
        // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(nellData);

        // Create NdFluents triples
        Property property;
        Resource resource;
        property = model.getProperty(ConstantList.PREFIX_NDFLUENTS, ConstantList.PROPERTY_PROVENANCE_PART_OF);
        resource =  createSequentialProvenanceResource(triple.getSubject().getLocalName(),ConstantList.NAMESPACE_NDFLUENTS + ConstantList.CLASS_PROVENANCE_PART);
        resource.addProperty(property, triple.getSubject());
        if (triple.getObject().isResource()) {
            resource = createSequentialProvenanceResource(triple.getObject().asResource().getLocalName(), ConstantList.NAMESPACE_NDFLUENTS + ConstantList.CLASS_PROVENANCE_PART);
            resource.addProperty(property, triple.getObject());
        }

        property = model.getProperty(ConstantList.PREFIX_NDFLUENTS, ConstantList.PROPERTY_PROVENANCE_EXTENT);
        resource = createSequentialProvenanceResource(ConstantList.RESOURCE_BELIEF, ConstantList.CLASS_BELIEF);
        triple.getSubject().addProperty(property, resource);
        if (triple.getObject().isResource()) {
            triple.getObject().asResource().addProperty(property, resource);
        }

        // Attach metadata to reification statement
        attachMetadata(resource, nellData);
    }

	private void stringToRDFWithSingletoProperty(final String[] nellData) {

        // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(nellData);

        // Create the Singleton Property
        Property singletonProperty = createSingletonPropertyOf(triple.getPredicate());

        // Attach metadata to reification statement
        attachMetadata(singletonProperty, nellData);

	}

	private void stringToRDFWithQuads(final String[] nellData) {

//        // Create normal triple without metadata
//        final Statement triple = stringToRDFWithoutMetadata(nellData);
//
//		// Create QUAD
//		final Resource tripleId = createSequentialProvenanceResource("ID");
//		final Quad quad = new Quad(tripleId.asNode(), triple.asTriple());
//		triple.createReifiedStatement();
//
//        // Attach metadata to triple ID
//        attachMetadata(tripleId, nellData);
	}

	private void stringToRDFWithNAry(final String[] nellData) {

	    // Create normal triple without metadata
        final Statement triple = stringToRDFWithoutMetadata(nellData);

        // Create N-Ary triples
        Property predicate1 = this.model.getProperty(triple.getPredicate().toString() + "_statement");
        Property predicate2 = this.model.getProperty(triple.getPredicate().toString() + "_value");
        RDFNode statement = createSequentialProvenanceResource(ConstantList.RESOURCE_BELIEF, ConstantList.CLASS_BELIEF);
        triple.getSubject().addProperty(predicate1,statement);
        statement.asResource().addProperty(predicate2,triple.getObject());

        // Attach metadata to reification statement
        attachMetadata(statement.asResource(), nellData);

	}

	private void stringToRDFWithReification(final String[] nellData) {

		// Create normal triple without metadata
		final Statement triple = stringToRDFWithoutMetadata(nellData);

		// Create reification
        ReifiedStatement statement = triple.createReifiedStatement(createSequentialProvenanceResourceUri(ConstantList.RESOURCE_BELIEF));

		// Attach metadata to reification statement
		attachMetadata(statement, nellData);
	}

    private void createProvenanceOntology() {
        // Create properties
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ASSOCIATED_WITH);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_GENERATED_BY);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ITERATION);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ITERATION_OF_PROMOTION);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_PROBABILITY);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_PROBABILITY_OF_BELIEF);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_AT_TIME);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_SOURCE);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_TOKEN);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_TOKE_ENTITY);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_RELATION_VALUE);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_GENERALIZATION_VALUE);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_LATITUDE_VALUE);
        this.model.createProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_LONGITUDE_VALUE);

	    // Create classes
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_BELIEF);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_CANDIDATE_BELIEF);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_PROMOTED_BELIEF);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_COMPONENT);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_COMPONENT_ITERATION);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_TOKEN);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_TOKEN_RELATION);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_TOKEN_GENERALIZATION);
        this.model.createResource(this.provenanceOntologyBase + ConstantList.CLASS_TOKEN_GEO);

        if (this.metadata == NellOntologyConverter.NDFLUENTS) {
            this.model.setNsPrefix(ConstantList.PREFIX_NDFLUENTS, ConstantList.NAMESPACE_NDFLUENTS);
        }
    }

    private void createComponents() {
        this.model.createResource(this.provenanceResourceBase + ConstantList.ALIASMATCHER);
        this.model.createResource(this.provenanceResourceBase + ConstantList.CMC);
        this.model.createResource(this.provenanceResourceBase + ConstantList.CPL);
        this.model.createResource(this.provenanceResourceBase + ConstantList.LE);
        this.model.createResource(this.provenanceResourceBase + ConstantList.LATLONG);
        this.model.createResource(this.provenanceResourceBase + ConstantList.MBL);
        this.model.createResource(this.provenanceResourceBase + ConstantList.OE);
        this.model.createResource(this.provenanceResourceBase + ConstantList.ONTOLOGYMODIFIER);
        this.model.createResource(this.provenanceResourceBase + ConstantList.PRA);
        this.model.createResource(this.provenanceResourceBase + ConstantList.RULEINFERENCE);
        this.model.createResource(this.provenanceResourceBase + ConstantList.SEAL);
        this.model.createResource(this.provenanceResourceBase + ConstantList.SEMPARSE);
        this.model.createResource(this.provenanceResourceBase + ConstantList.SPREADSHEETEDITS);
    }

	private void attachMetadata(final Resource resource, final String[] nellData) {
		Property predicate;
		RDFNode object;

		createProvenanceOntology();
        resource.addProperty(RDF.type, model.getResource(this.provenanceOntologyBase + ConstantList.CLASS_BELIEF));

		LineInstanceJOIN metadata = new LineInstanceJOIN(nellData[0], nellData[1], nellData[2], nellData[3], nellData[4], Utility.DecodeURL(nellData[5]), nellData[6], nellData[7], nellData[8], nellData[9], nellData[10], nellData[11], Utility.DecodeURL(nellData[12]), String.join("\t", nellData), this.candidates);

		// If it is a promoted belief, add iteration of promotion and probability
        if(!candidates) {
            // Add iteration of promotion
            predicate = this.model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ITERATION_OF_PROMOTION);
            object = this.model.createTypedLiteral(metadata.getNrIterationsInt(),XSDDatatype.XSDinteger);
            resource.addProperty(predicate, object);

            // Add probability
            predicate = this.model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_PROBABILITY_OF_BELIEF);
            object = this.model.createTypedLiteral(metadata.getProbabilityDouble(), XSDDatatype.XSDdecimal);
            resource.addProperty(predicate, object);
        }

        metadata.getListComponents().forEach((String K, Header V) -> {
            Property predicate_λ;
            RDFNode object_λ;

            // Create the Component Iteration
            predicate_λ = this.model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_GENERATED_BY);
            RDFNode componentIteration = createSequentialProvenanceResource(K, ConstantList.CLASS_COMPONENT_ITERATION);
            resource.addProperty(predicate_λ,componentIteration);

            // Add data to Component Iteration
            predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ASSOCIATED_WITH);
            object_λ = model.getResource(this.provenanceResourceBase + V.getComponentName());
            componentIteration.asResource().addProperty(predicate_λ, object_λ);

            predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_AT_TIME);
            object_λ = model.createTypedLiteral(V.getDateTime(),XSDDatatype.XSDdateTime);
            componentIteration.asResource().addProperty(predicate_λ, object_λ);

            if (candidates) {
                predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_ITERATION);
                object_λ = model.createTypedLiteral(V.getIteration(), XSDDatatype.XSDinteger);
                componentIteration.asResource().addProperty(predicate_λ, object_λ);

                predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_PROBABILITY);
                object_λ = model.createTypedLiteral(V.getProbability(), XSDDatatype.XSDdecimal);
                componentIteration.asResource().addProperty(predicate_λ,object_λ);
            }

            if (V.getStringSource() != null) {
                predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_SOURCE);
                object_λ = model.createTypedLiteral(V.getStringSource().trim(), XSDDatatype.XSDstring);
                componentIteration.asResource().addProperty(predicate_λ, object_λ);
            }

            // Create Token
            RDFNode token;
            if (V instanceof LatLong) {
                token = createSequentialProvenanceResource(ConstantList.RESOURCE_TOKEN_GEO, ConstantList.CLASS_TOKEN_GEO);
                predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_LATITUDE_VALUE);
                double[] latlong= V.getFormatHeader().getTokenElement2LatLong();
                object_λ = model.createTypedLiteral(latlong[0], XSDDatatype.XSDdecimal);
                token.asResource().addProperty(predicate_λ, object_λ);
                predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_LONGITUDE_VALUE);
                object_λ = model.createTypedLiteral(((LatLong) V).getFormatHeader().getTokenElement2LatLong()[1], XSDDatatype.XSDdecimal);
                token.asResource().addProperty(predicate_λ, object_λ);
            } else {
                switch (V.getFormatHeader().getTypeKB()) {
                    case ConstantList.RELATION:
                        token = createSequentialProvenanceResource(ConstantList.RESOURCE_TOKEN_RELATION, ConstantList.CLASS_TOKEN_RELATION);
                        predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_RELATION_VALUE);
                        object_λ = model.createTypedLiteral(V.getFormatHeader().getTokenElement2(), XSDDatatype.XSDstring);
                        token.asResource().addProperty(predicate_λ, object_λ);
                        break;
                    case ConstantList.CATEGORY:
                        token = createSequentialProvenanceResource(ConstantList.RESOURCE_TOKEN_GENERALIZATION, ConstantList.CLASS_TOKEN_GENERALIZATION);
                        predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_GENERALIZATION_VALUE);
                        object_λ = model.createTypedLiteral(V.getFormatHeader().getTokenElement2(), XSDDatatype.XSDstring);
                        token.asResource().addProperty(predicate_λ, object_λ);
                        break;
                    default:
                        token = createSequentialProvenanceResource(ConstantList.RESOURCE_TOKEN, ConstantList.CLASS_TOKEN);
                        break;
                }
            }
            predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_TOKE_ENTITY);
            object_λ = model.createTypedLiteral(V.getFormatHeader().getTokenElement1(), XSDDatatype.XSDstring);
            token.asResource().addProperty(predicate_λ, object_λ);
            predicate_λ = model.getProperty(this.provenanceOntologyBase + ConstantList.PROPERTY_TOKEN);
            componentIteration.asResource().addProperty(predicate_λ, token);
        });
	}

	private Statement stringToRDFWithoutMetadata(final String[] nellData) {

		/* Traitement du sujet. */
		String[] nellDataSplit = nellData[0].split(":", 2);
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
		Property relation = this.findRelation(nellData[1]);
		
		/* Traitement de l'objet. */
		RDFNode object_node;
		nellDataSplit = nellData[2].split(":", 2);
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

	private Property createSingletonPropertyOf(final Property generalProperty) {
	    final Property singletonProperty = model.getProperty(createSequentialProvenanceResourceUri(generalProperty.getLocalName()));
        singletonProperty.addProperty(RDF.type, model.getResource("http://www.w3.org/2000/01/rdf-schema#rdf:singletonPropertyOf"));
	    return singletonProperty;
    }

//	private Property getOrCreateProvenanceProperty(String string) {
//	    Property property;
//	    if ((property = model.getProperty(this.provenanceOntologyBase + string)) == null) {
//	        property = model.createProperty(this.provenanceOntologyBase + string);
//        }
//        return property;
//    }

	private Resource createSequentialProvenanceResource(final Resource resource_class) {
		final Resource resource = this.model.createResource(createSequentialProvenanceResourceUri(resource_class.getLocalName()));
		resource.addProperty(RDF.type, resource_class);
		return resource;
	}

	private Resource createSequentialProvenanceResource(final String resource_name, final String type) {
		final Resource resource = this.model.createResource(createSequentialProvenanceResourceUri(resource_name));
		resource.addProperty(RDF.type, model.getResource(this.provenanceOntologyBase + type));
		return resource;
	}

    private String createSequentialProvenanceResourceUri(final String name) {
        return this.provenanceResourceBase + name + (this.candidates ? ConstantList.SUFFIX_CANDIDATE : ConstantList.SUFFIX_PROMOTED) + numberSequences.compute(name, (K,V) -> V == null ? 1 : ++V);
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
