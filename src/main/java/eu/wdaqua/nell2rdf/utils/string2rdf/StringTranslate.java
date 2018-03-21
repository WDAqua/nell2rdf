package eu.wdaqua.nell2rdf.utils.string2rdf;

import static eu.wdaqua.nell2rdf.utils.UriNell.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.time.ZonedDateTime;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;

import eu.wdaqua.nell2rdf.NellOntologyConverter;
import eu.wdaqua.nell2rdf.extract.metadata.models.Header;
import eu.wdaqua.nell2rdf.extract.metadata.models.LineInstanceJOIN;
import eu.wdaqua.nell2rdf.extract.metadata.util.Utility;
import eu.wdaqua.nell2rdf.utils.string2rdf.components.ComponentRDF;
import eu.wdaqua.nell2rdf.utils.string2rdf.components.ComponentRDFBuilder;

/**
 *
 * @author Quentin Cruzille & Jose M. Gimenez-Garcia
 *
 *         Cree un model Jena et le rempli avec les informations extraites du fichier de Nell.
 *
 */
public class StringTranslate {

	private final Logger	log				= Logger.getLogger(StringTranslate.class);

	private final String	metadata;
	private final boolean	deleteOriginalTriples;
	private OutputStream	outputStream	= null;

	// private LineInstanceJOIN belief;

	/**
	 * Model contenant contenant les triplets de Nell.
	 */
	private final Model		model;

	private DatasetGraph	dataset			= null;

	/**
	 * URI pour notre prefixe.
	 */
	// private final String base;
	// private final String ontologyBase;

	private final String	lang;

	/**
	 * URI pour le prefixe SKOS.
	 */
	private final String	skos;

	/**
	 * Propriete prefLabel de SKOS.
	 * (Jena n'implementant pas le vocabulaire SKOS, il est necessaire de la gerer manuellement).
	 */
	private final Property	prefLabel;

	public List<String>		fail;
	public List<String>		good;

	private final boolean	addDBpediaLinks	= true;										// TODO: accept as parameter

	/**
	 * Constructeur, initialise le model et les prefixes.
	 */
	public StringTranslate(final String metadata, final String lang, final String file, final boolean deleteOriginalTriples) {
		this.model = ModelFactory.createDefaultModel();
		switch (metadata) {
			case NellOntologyConverter.NONE:
				setBase(NAMESPACE_PREFIX);
				break;
			case NellOntologyConverter.REIFICATION:
				setBase(NAMESPACE_PREFIX + NAMESPACE_MIDDLE_REIFICATION);
				break;
			case NellOntologyConverter.N_ARY:
				setBase(NAMESPACE_PREFIX + NAMESPACE_MIDDLE_NARY_RELATIONS);
				break;
			case NellOntologyConverter.QUADS:
				setBase(NAMESPACE_PREFIX + NAMESPACE_MIDDLE_NAMED_GRAPHS);
				this.dataset = TDBFactory.createDatasetGraph();
				break;
			case NellOntologyConverter.SINGLETON_PROPERTY:
				setBase(NAMESPACE_PREFIX + NAMESPACE_MIDDLE_SINGLETON_PROPERTY);
				break;
			case NellOntologyConverter.NDFLUENTS:
				setBase(NAMESPACE_PREFIX + NAMESPACE_MIDDLE_NDFLUENTS);
				break;
			default:
				this.log.warn("Metadata model not recognized. Converting string to RDF without metadata");
				setBase(NAMESPACE_PREFIX);
				break;
		}

		this.metadata = metadata;
		this.deleteOriginalTriples = deleteOriginalTriples;
		this.lang = lang;
		this.skos = "http://www.w3.org/2004/02/skos/core#";
		// this.model.setNsPrefix(UriNell.PREFIX_RESOURCE, this.resourceBase);
		// this.model.setNsPrefix(UriNell.PREFIX_ONTOLOGY, this.ontologyBase);
		// this.model.setNsPrefix(UriNell.PREFIX_METADATA, this.provenanceResourceBase);
		// this.model.setNsPrefix("skos", this.skos);
		// this.model.setNsPrefix("rdfs", this.rdfs);
		// this.model.setNsPrefix("xsd", this.xsd);
		this.prefLabel = this.model.createProperty(this.skos + "prefLabel");
		this.fail = new LinkedList<>();
		this.good = new LinkedList<>();

		// workaround to reduce memory consumption
		try {
			this.outputStream = new FileOutputStream(file);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prend un tableau de chaines de caracteres et les traduits en model Jena.
	 *
	 * @param nellData
	 */

	public void stringToRDF(final String nellData) {
		final String[] split = nellData.split("\t");
		final LineInstanceJOIN belief = new LineInstanceJOIN(split[0], split[1], split[2], split[3], split[4], Utility.DecodeURL(split[5]), split[6], split[7], split[8], split[9], split[10],
				split[11],
				Utility.DecodeURL(split[12]), nellData);

		final Statement triple = this.stringToRDFWithoutMetadata(belief);
		final Optional<Statement> optionalDBpediaTriple = addDBpediaTriple(triple);

		if (!excludeTripleFromReification(triple)) {

			Optional<Resource> optionalAnchor;
			Optional<Resource> optionalDPTanchor;

			switch (this.metadata) {
				case NellOntologyConverter.NONE:
					this.log.debug("Converting string to RDF without metadata");
					optionalAnchor = Optional.empty();
					optionalDPTanchor = Optional.empty();
					break;
				case NellOntologyConverter.REIFICATION:
					this.log.debug("Converting string to RDF using reification to attach metadata");
					optionalAnchor = Optional.of(toReification(triple));
					optionalDPTanchor = optionalDBpediaTriple.map(dbpediaTriple -> toReification(dbpediaTriple));
					break;
				case NellOntologyConverter.N_ARY:
					this.log.debug("Converting string to RDF using n-ary relations to attach metadata");
					optionalAnchor = Optional.of(toNary(triple));
					optionalDPTanchor = optionalDBpediaTriple.map(dbpediaTriple -> toNary(dbpediaTriple));
					break;
				case NellOntologyConverter.QUADS:
					this.log.debug("Converting string to RDF using quads to attach metadata");
					optionalAnchor = Optional.of(toQuads(triple));
					optionalDPTanchor = optionalDBpediaTriple.map(dbpediaTriple -> toQuads(dbpediaTriple));
					break;
				case NellOntologyConverter.SINGLETON_PROPERTY:
					this.log.debug("Converting string to RDF using singleton property to attach metadata");
					optionalAnchor = Optional.of(toSingletoProperty(triple));
					optionalDPTanchor = optionalDBpediaTriple.map(dbpediaTriple -> toSingletoProperty(dbpediaTriple));
					break;
				case NellOntologyConverter.NDFLUENTS:
					this.log.debug("Converting string to RDF using NdFluents to attach metadata");
					optionalAnchor = Optional.of(toNdFluents(triple));
					optionalDPTanchor = optionalDBpediaTriple.map(dbpediaTriple -> toNdFluents(dbpediaTriple));
					break;
				default:
					this.log.warn("Metadata model not recognized. Converting string to RDF without metadata");
					optionalAnchor = Optional.empty();
					optionalDPTanchor = Optional.empty();
					break;
			}

			optionalAnchor.ifPresent(anchor -> {
				this.attachMetadata(anchor, belief);
				optionalDPTanchor.ifPresent(dbpediaAnchor -> this.attachMetadata(dbpediaAnchor));
				if (this.deleteOriginalTriples) {
					triple.remove();
					optionalDBpediaTriple.ifPresent(dbpediaTriple -> dbpediaTriple.remove());
				}
			});
		}

		// workaround to reduce memory consumption
		if (this.dataset != null) {
			RDFDataMgr.write(this.outputStream, this.dataset, Lang.NQUADS);
			this.dataset.clear();
		} else {
			this.model.write(this.outputStream, this.lang);
			this.model.removeAll();
		}
	}

	private Resource toNdFluents(final Statement triple) {

		// Create NdFluents triples
		Property property;
		Resource resource;
		property = this.model.getProperty(getMetadataUri(PROPERTY_PROVENANCE_PART_OF));
		resource = this.model.createResource(createSequentialName(triple.getSubject().getURI()), this.model.getResource(createUri(NAMESPACE_NDFLUENTS, CLASS_PROVENANCE_PART)));
		resource.addProperty(property, triple.getSubject());
		if (triple.getObject().isResource()) {
			resource = this.model.createResource(createSequentialName(triple.getObject().asResource().getURI()), this.model.getResource(createUri(NAMESPACE_NDFLUENTS, CLASS_PROVENANCE_PART)));
			resource.addProperty(property, triple.getObject());
		}

		property = this.model.getProperty(getMetadataUri(PROPERTY_PROVENANCE_EXTENT));
		// resource = model.createResource(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()), model.getResource(UriNell.CLASS_BELIEF));
		resource = this.model.createResource(createAnchorUri(triple));
		triple.getSubject().addProperty(property, resource);
		if (triple.getObject().isResource()) {
			triple.getObject().asResource().addProperty(property, resource);
		}

		return resource;

	}

	private Resource toSingletoProperty(final Statement triple) {
		final Property singletonProperty = this.model.getProperty(createAnchorUri(triple));
		singletonProperty.addProperty(this.model.getProperty(SINGLETON_PROPERTY_OF), triple.getPredicate());
		singletonProperty.addProperty(RDF.type, this.model.getResource(TYPE_SINGLETON_PROPERTY));
		triple.getSubject().addProperty(singletonProperty, triple.getObject());
		return singletonProperty;
	}

	private Resource toQuads(final Statement triple) {
		this.dataset = TDBFactory.createDatasetGraph();
		final Resource tripleId = this.model.createResource(createAnchorUri(triple));
		this.dataset.add(tripleId.asNode(), triple.getSubject().asNode(), triple.getPredicate().asNode(), triple.getObject().asNode());
		return tripleId;
	}

	private Resource toNary(final Statement triple) {
		final Property predicate = this.model.getProperty(triple.getPredicate().toString());
		final Property predicate1 = this.model.getProperty(triple.getPredicate().toString() + "_statement");
		final Property predicate2 = this.model.getProperty(triple.getPredicate().toString() + "_value");
		predicate.addProperty(this.model.getProperty(getMetadataUri(PROPERTY_SUBJECT_PROPERTY)), predicate1);
		predicate.addProperty(this.model.getProperty(getMetadataUri(PROPERTY_OBJECT_PROPERTY)), predicate2);

		// RDFNode statement = model.createResource(UriNell.createAnchorUri(UriNell.RESOURCE_BELIEF, belief.isCandidate()), model.getResource(UriNell.CLASS_BELIEF));
		final RDFNode statement = this.model.createResource(createAnchorUri(triple));

		triple.getSubject().addProperty(predicate1, statement);
		statement.asResource().addProperty(predicate2, triple.getObject());

		return statement.asResource();
	}

	private Resource toReification(final Statement triple) {
		return triple.createReifiedStatement(createAnchorUri(triple));
	}

	// private void createProvenanceOntology() {
	// // Create properties
	// this.model.createProperty(UriNell.PROPERTY_ASSOCIATED_WITH);
	// this.model.createProperty(UriNell.PROPERTY_GENERATED_BY);
	// this.model.createProperty(UriNell.PROPERTY_ITERATION);
	// this.model.createProperty(UriNell.PROPERTY_ITERATION_OF_PROMOTION);
	// this.model.createProperty(UriNell.PROPERTY_PROBABILITY);
	// this.model.createProperty(UriNell.PROPERTY_PROBABILITY_OF_BELIEF);
	// this.model.createProperty(UriNell.PROPERTY_AT_TIME);
	// this.model.createProperty(UriNell.PROPERTY_SOURCE);
	// this.model.createProperty(UriNell.PROPERTY_TOKEN);
	// this.model.createProperty(UriNell.PROPERTY_TOKE_ENTITY);
	// this.model.createProperty(UriNell.PROPERTY_RELATION_VALUE);
	// this.model.createProperty(UriNell.PROPERTY_GENERALIZATION_VALUE);
	// this.model.createProperty(UriNell.PROPERTY_LATITUDE_VALUE);
	// this.model.createProperty(UriNell.PROPERTY_LONGITUDE_VALUE);
	//
	// // Create classes
	// this.model.createResource(UriNell.CLASS_BELIEF);
	// this.model.createResource(UriNell.CLASS_CANDIDATE_BELIEF);
	// this.model.createResource(UriNell.CLASS_PROMOTED_BELIEF);
	// this.model.createResource(UriNell.CLASS_COMPONENT);
	// this.model.createResource(UriNell.CLASS_COMPONENT_EXECUTION);
	// this.model.createResource(UriNell.CLASS_TOKEN);
	// this.model.createResource(UriNell.CLASS_TOKEN_RELATION);
	// this.model.createResource(UriNell.CLASS_TOKEN_GENERALIZATION);
	// this.model.createResource(UriNell.CLASS_TOKEN_GEO);
	//
	// if (this.metadata == NellOntologyConverter.NDFLUENTS) {
	// this.model.setNsPrefix(UriNell.PREFIX_NDFLUENTS, UriNell.NAMESPACE_NDFLUENTS);
	// }
	// }

	// private void createComponents() {
	// this.model.createResource(this.provenanceResourceBase + ConstantList.ALIASMATCHER);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.CMC);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.CPL);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.LE);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.LATLONG);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.MBL);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.OE);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.ONTOLOGYMODIFIER);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.PRA);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.RULEINFERENCE);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.SEAL);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.SEMPARSE);
	// this.model.createResource(this.provenanceResourceBase + ConstantList.SPREADSHEETEDITS);
	// }

	private boolean excludeTripleFromReification(final Statement triple) {
		final boolean excludeTripleFromReification = false;
		// TODO accept as parameter / configuration file
		// if (triple.getPredicate().getNameSpace() == RDF.getURI()) {
		// excludeTripleFromReification = true;
		// }
		return excludeTripleFromReification;
	}

	private void attachMetadata(final Resource resource, final LineInstanceJOIN belief) {
		Property predicate;
		RDFNode object;

		// createProvenanceOntology();
		if (belief.isCandidate() == true) {
			resource.addProperty(RDF.type, this.model.getResource(getMetadataUri(CLASS_CANDIDATE_BELIEF)));
		} else {
			resource.addProperty(RDF.type, this.model.getResource(getMetadataUri(CLASS_PROMOTED_BELIEF)));
		}

		// Add iteration of promotion
		predicate = this.model.getProperty(getMetadataUri(PROPERTY_ITERATION_OF_PROMOTION));
		object = this.model.createTypedLiteral(belief.getNrIterationsInt(), XSDDatatype.XSDnonNegativeInteger);
		resource.addProperty(predicate, object);

		if (belief.getProbabilityDouble() != null) {
			// Add probability
			predicate = this.model.getProperty(getMetadataUri(PROPERTY_PROBABILITY_OF_BELIEF));
			object = this.model.createTypedLiteral(belief.getProbabilityDouble(), XSDDatatype.XSDdecimal);
			resource.addProperty(predicate, object);
		}

		belief.getListComponents().forEach((final String K, final Header V) -> {
			final ComponentRDF component = ComponentRDFBuilder.build(V, resource);
			// System.out.println(component.getClass());
			component.addTriples();
		});
	}

	private void attachMetadata(final Resource resource) {
		resource.addProperty(RDF.type, this.model.getResource(getMetadataUri(CLASS_LINK)));

		final Property predicate = this.model.getProperty(getMetadataUri(PROPERTY_GENERATED_AT_TIME));
		final RDFNode object = this.model.createTypedLiteral(ISO_OFFSET_DATE_TIME.format(ZonedDateTime.now()), XSDDatatype.XSDdateTime);
		resource.addProperty(predicate, object);
	}

	private Optional<Statement> addDBpediaTriple(final Statement statement) {
		Statement triple = null;
		if (this.addDBpediaLinks && statement.getPredicate().getURI().equals(getOntologyUri(PROPERTY_HAS_WIKIPEDIA_PAGE))) {
			final Resource newSubject = statement.getSubject();
			final Property newProperty = OWL2.sameAs;
			final String dbpediaResource = statement.getObject().asLiteral().getString().replaceFirst(NAMESPACE_WIKIPEDIA, "");
			final Resource newObject = this.model.createResource(createUri(NAMESPACE_DBPEDIA, dbpediaResource));
			newSubject.addProperty(newProperty, newObject);
			triple = this.model.createStatement(newSubject, newProperty, newObject);
		}
		return Optional.ofNullable(triple);
	}

	private Statement stringToRDFWithoutMetadata(final LineInstanceJOIN belief) {
		return this.stringToRDFWithoutMetadata(this.model, belief);
	}

	private Statement stringToRDFWithoutMetadata(final Model model, final LineInstanceJOIN belief) {
		final String[] nellData = belief.completeLine.split("\t");
		/* Traitement du sujet. */
		String[] nellDataSplit = belief.getEntity().split(":", 2);
		nellDataSplit[1] = nellDataSplit[1].replaceAll(":", "_");
		final Resource subject = model.getResource(getResourceUri(nellDataSplit[1]));
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
		final Property relation = findRelation(belief.getRelation());

		/* Traitement de l'objet. */
		RDFNode object_node;
		nellDataSplit = belief.getValue().split(":", 2);
		if (nellDataSplit[0].equals("concept")) {
			/* Cas ou l'objet n'est pas un literal. */
			Resource object_resource;
			nellDataSplit[1] = nellDataSplit[1].replaceAll(":", "_");
			if (nellData[1].equals("generalizations")) {
				object_resource = model.getResource(getOntologyUri(nellDataSplit[1]));
			} else {
				object_resource = model.getResource(getResourceUri(nellDataSplit[1]));

				// if (!nellData[11].equals("")) {
				// findType(nellData[11], object_resource);
				// }
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
				try {
					trueLiteral = new BigInteger(nellData[2]);

				} catch (final NumberFormatException e) {

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
		// return ResourceFactory.createStatement(subject, relation, object_node);
	}

	/**
	 * Prend une chaine de caractere et verifie s'il existe deje une resource associee e cette chaene, si oui
	 * la renvoie, sinon, la cree.
	 */
	// private Resource getOrCreateRessource(String string)
	// {
	// Resource resource;
	// if((resource=model.getResource(createUri(this.resourceBase + string))==null))
	// {
	// resource=model.createResource(this.resourceBase + string);
	// }
	// return resource;
	// }
	//
	// private Resource getOrCreateRessourceClass(String string)
	// {
	// Resource resource;
	// if((resource=model.getResource(createUri(this.ontologyBase +string))==null))
	// {
	// resource=model.createResource(this.ontologyBase +string);
	// }
	// return resource;
	// }

	/**
	 * Renvoi la propriete associee e une chaene de caracteres.
	 *
	 * @param relation
	 * @return
	 */
	private Property findRelation(final String relation) {
		switch (relation) {
			case "generalizations":
				return (RDF.type);

			default:
				final String[] s = relation.split(":", 2);
				Property p;
				s[1] = s[1].replaceAll(":", "_");
				p = this.model.getProperty(getOntologyUri(s[1]));
				return (p);
		}
	}

	/**
	 * Parse la chaene de caracteres label pour trouver les differents label associes e subject.
	 * Associe les label trouves e subject avec RDFS.label et prefLabel avec SKOS.prefLabel.
	 *
	 * @param subject
	 * @param label
	 */
	private void findLabel(final Resource subject, final String label) {
		int i;
		final String[] labelSplit = label.split("\" ");
		for (i = 0; i < labelSplit.length; i++) {
			final String l = labelSplit[i].replaceAll("\"", "");
			subject.addProperty(RDFS.label, l);
		}
	}

	/**
	 * Parse la chaene de caracteres type pour trouver les classes associees e la resource subject.
	 * Associe les classes trouvees e subject avec RDF.type.
	 *
	 * @param type
	 * @param subject
	 */
	private void findType(final String type, final Resource subject) {
		final String[] typeSplit = type.split(" ");
		for (final String element : typeSplit) {
			final String[] trueType = element.split(":", 2);
			if (trueType[0].equals("concept")) {
				trueType[1] = trueType[1].replaceAll(":", "_");
				trueType[1] = trueType[1].replaceAll("\"", "");
				final Resource classe = this.model.getResource(trueType[1]);
				subject.addProperty(RDF.type, classe);
			}
		}
	}

	/**
	 * Renvoie le model.
	 *
	 * @return
	 */
	public Model getModel() {
		return this.model;
	}
}
