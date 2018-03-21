package eu.wdaqua.nell2rdf.utils;

import java.util.Collection;
import java.util.LinkedList;

import eu.wdaqua.nell2rdf.beans.PredicatePair;

import org.apache.commons.collections.CollectionUtils;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;

import com.google.common.collect.Multimap;

import eu.wdaqua.nell2rdf.beans.NellModel;
import eu.wdaqua.nell2rdf.utils.RdfModelUtils.Scope;
import eu.wdaqua.nell2rdf.utils.exceptions.BadScopeException;

import static eu.wdaqua.nell2rdf.utils.UriNell.*;

/**
 * Utility class for generating the mapping between Nell in-memory model of the CSV file, and the targeted Jena model of NELL.
 *
 * @author Christophe Gravier
 */
public class NellMapper {

	public static Logger log = Logger.getLogger(NellMapper.class);

	/**
	 * Initialises an empty in-memory Jena model with prefixes nellclassesn nellproperties, and nellunknown added to model so that Jena writer can use them to abbrev. IRIs.
	 *
	 * @return an empty model following OWL_DL specs, stored in-memory.
	 */
	public static Model createEmptyModel(final String basePrefix) {
		final Model m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		m.setNsPrefix("nellonto", basePrefix + "ontology/");
		return m;
	}

	/**
	 * Lookup classes and properties in the original nell ontology and create them in the nell RDF ontology.
	 *
	 * @param nell
	 *            a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param nellRdf
	 *            Jena model in which classes and properties should be added.
	 * @param prefix
	 *            prepend this prefix to any generated IRI by the program
	 */
	public static void lookupClassesAndProperties(final NellModel nell, final Model nellRdf) {
		final String predicate = "memberofsets";
		final Collection<PredicatePair> classesAndProperties = nell.getOntology().get(predicate);
		int nbC = 0, nbP = 0;
		for (final PredicatePair subobj : classesAndProperties) {
			final String subject = subobj.getSubject();
			final String object = subobj.getObject();
			if (object.toLowerCase().contains("relation")) {
				// we found a new relation, let us map it to a RDFProperty
				nbP++;
				RdfModelUtils.addProperty(nellRdf, subject);
			} else if (object.toLowerCase().contains("category")) {
				// we found a new class, let us map it to a RDFClass
				nbC++;
				RdfModelUtils.addClass(nellRdf, subject);
			} else {
				log.warn("The triple (S,P,O) = ("
						+ subobj.getSubject()
						+ ","
						+ predicate
						+ ","
						+ subobj.getObject()
						+ ") is a predicate for class or property declaration, yet the object does not precise whther it is a property (contains string relation) or a class (contains string category)");
			}
		}
		log.info("Found " + nbC + " classes and " + nbP + " properties out of " + classesAndProperties.size() + " unique memberofsets");
	}

	/**
	 * Lookup for all predicates to map from the NELL ontology to RDF, excluding classes and properties declaration, which are dealt aside in
	 * {@link #lookupClassesAndProperties(NellModel, Model, String)} should have already been called on this model.
	 *
	 * @param nell
	 *            a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param nellRdf
	 *            Jena model in which classes and properties should be added.
	 * @param prefix
	 *            prepend this prefix to any generated IRI by the program
	 */
	public static void parseAllPredicates(final NellModel nell, final Model nellRdf) {

		final Multimap<String, PredicatePair> allTriples = nell.getOntology();

		// only generalizations
		for (final PredicatePair ppair : allTriples.get("generalizations")) {
			RdfModelUtils.addSubsumption(nellRdf, ppair.getSubject(), ppair.getObject());
		}

		// all but memberofsets, generalizations
		for (final String predicate : allTriples.keySet()) {
			if (!predicate.equals("memberofsets") && !predicate.equals("generalizations")) {
				addAllInstanceForPredicate(nellRdf, allTriples, predicate);
			}
		}

	}

	/**
	 * For each existing predicates different from "memberofsets"
	 *
	 * @param nellRdf
	 *            Jena model in which classes and properties should be added.
	 * @param prefix
	 *            prepend this prefix to any generated IRI by the program
	 * @param allTriples
	 *            all pair of subject/object for a <code>predicate</code>
	 * @param predicate
	 *            specific predicate for all (subject,object).
	 */
	private static void addAllInstanceForPredicate(final Model nellRdf, final Multimap<String, PredicatePair> allTriples, final String predicate) {
		for (final PredicatePair subobj : allTriples.get(predicate)) {
			final String subject = subobj.getSubject();
			final String object = subobj.getObject();
			NellMapper.addTriple(nellRdf, predicate, subject, object);
		}
	}

	/**
	 * Display some stats on the RDF in memory model after processing the RDF File. These are the same stats that the one that can be displayed on the NELL CSV file using bash script located at
	 * src/main/resources/printNellCsvStats.sh
	 *
	 * @param nellRdf
	 *            NELL ontology as a Jena model.
	 * @param nell
	 *            a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param prefix
	 *            prepend this prefix to any generated IRI by the program
	 */
	public static void displayStats(final Model nellRdf, final NellModel nell) {

		final Selector selectorClass = new SimpleSelector(null, RDF.type, RDFS.Class);
		final Selector selectorProperty = new SimpleSelector(null, RDF.type, RDF.Property);
		final Selector selectorFunctionalProperty = new SimpleSelector(null, RDF.type, OWL.FunctionalProperty);
		final Selector selectorDomain = new SimpleSelector(null, RDFS.domain, (RDFNode) null);
		final Selector selectorRange = new SimpleSelector(null, RDFS.range, (RDFNode) null);
		final Selector selectorSubsomption = new SimpleSelector(null, RDFS.subClassOf, (RDFNode) null);
		final Selector selectorInverseOf = new SimpleSelector(null, OWL2.inverseOf, (RDFNode) null);

		final StmtIterator classDisjunction = nellRdf.listStatements(new SimpleSelector(null, OWL2.disjointWith, (RDFNode) null) {
			@Override
			public boolean selects(final Statement s) {
				return (s.getSubject().getProperty(RDF.type).equals(RDFS.Class));
			}
		});

		final StmtIterator propsDisjunction = nellRdf.listStatements(new SimpleSelector(null, OWL2.propertyDisjointWith, (RDFNode) null) {
			@Override
			public boolean selects(final Statement s) {
				return (!(s.getObject().toString().contains("www.w3.org")));
			}
		});

		final Selector selectorComment = new SimpleSelector(null, RDFS.comment, (RDFNode) null);

		final Selector selectorPosIrreflexive = new SimpleSelector(null, RDF.type, OWL2.IrreflexiveProperty);
		final Selector selectorNegIrreflexive = new SimpleSelector(null, OWL2.propertyDisjointWith, OWL2.IrreflexiveProperty);

		final Selector selectorPosSymm = new SimpleSelector(null, RDF.type, OWL2.AsymmetricProperty);
		final Selector selectorNegSymm = new SimpleSelector(null, OWL2.propertyDisjointWith, OWL2.AsymmetricProperty);

		final Property propInstanceType = nellRdf.getProperty(getOntologyUri("instancetype"));
		final Selector selectorInstanceType = new SimpleSelector(null, propInstanceType, (RDFNode) null);

		final Property propDomInRange = nellRdf.getProperty(getOntologyUri("domainwithinrange"));
		final Selector selectorDomainRange = new SimpleSelector(null, propDomInRange, (RDFNode) null);

		final Property propRangeInDom = nellRdf.getProperty(getOntologyUri("rangewithindomain"));
		final Selector selectorRangeDomain = new SimpleSelector(null, propRangeInDom, (RDFNode) null);

		final Property propPopulate = nellRdf.getProperty(getOntologyUri("populate"));
		final Selector selectorPopulate = new SimpleSelector(null, propPopulate, (RDFNode) null);

		final Property propVisible = nellRdf.getProperty(getOntologyUri("visible"));
		final Selector selectorVisible = new SimpleSelector(null, propVisible, (RDFNode) null);

		final Property propHumanformat = nellRdf.getProperty(getOntologyUri("humanformat"));
		final Selector selectorHumanFormat = new SimpleSelector(null, propHumanformat, (RDFNode) null);

		final int disclass = classDisjunction.toList().size();
		final int disprop = propsDisjunction.toList().size();
		final int irrefelxivepos = nellRdf.listStatements(selectorPosIrreflexive).toList().size();
		final int irrefelxiveneg = nellRdf.listStatements(selectorNegIrreflexive).toList().size();
		final int asymmpos = nellRdf.listStatements(selectorPosSymm).toList().size();
		final int asymmneg = nellRdf.listStatements(selectorNegSymm).toList().size();

		// @formatter:off
		log.info("Stats on file :");
		log.info(nellRdf.listStatements(selectorClass).toList().size() + " class triples");
		log.info(nellRdf.listStatements(selectorSubsomption).toList().size() + " subsumptions" );
		log.info((disclass+disprop) + " disjonctions : "+ disclass + " classes and " + disprop + " props");
		log.info(nellRdf.listStatements(selectorProperty).toList().size() + " properties");
		log.info(nellRdf.listStatements(selectorDomain).toList().size() + " domain properties");
		log.info(nellRdf.listStatements(selectorRange).toList().size() + " range properties");
		log.info(nellRdf.listStatements(selectorFunctionalProperty).toList().size() + " functional properties");
		log.info(nellRdf.listStatements(selectorHumanFormat).toList().size() + " human readable formatter");
		log.info(nellRdf.listStatements(selectorComment).toList().size() + " description");
		log.info(nellRdf.listStatements(selectorInstanceType).toList().size() + " instancetype");
		log.info((irrefelxivepos+irrefelxiveneg) + " irreflexive assertions found ("+irrefelxivepos+" positives, and "+irrefelxiveneg+" negatives)");
		log.info((asymmpos+asymmneg) + " asymmetric assertions found ("+asymmpos+" positives, and "+asymmneg+" negatives)");
		log.info(nellRdf.listStatements(selectorDomainRange).toList().size() + " domainwithinrange");
		log.info(nellRdf.listStatements(selectorRangeDomain).toList().size() + " rangewithindomain");
		log.info(nellRdf.listStatements(selectorPopulate).toList().size() + " populate");
		log.info(nellRdf.listStatements(selectorVisible).toList().size() + " visible");
		log.info(nellRdf.listStatements(selectorInverseOf).toList().size() + " inverse properties");
		log.info("--------------------------------------------------------");
		log.info(nell.getNbTriples() + " unique triples in all in the " + nell.getNbLines() + " lines of the input file.");
		// @formatter:on
	}

	/**
	 * Depending on the predicate, the method calls the associated NellRdfUtils methods to update NELL ontology mapped into a Jena model.
	 *
	 * @param nellRdf
	 *            NELL ontology as a Jena model.
	 * @param predicate
	 *            predicate in the triple (P in <S,<b>P</b>,O>)
	 * @param subject
	 *            subject in the triple (S in <<b>S</b>,P,O>)
	 * @param object
	 *            object in the triple (O in <S,P,<b>O</b>>)
	 */
	private static void addTriple(final Model nellRdf, final String predicate, final String subject, final String object) {

		try {
			switch (predicate) {

				case "antireflexive": {
					RdfModelUtils.addAntiReflexiveProperty(nellRdf, subject, object);
					break;
				}

				case "antisymmetric": {
					RdfModelUtils.addAntiSymetricProperty(nellRdf, subject, object);
					break;
				}

				case "description": {
					RdfModelUtils.addDescription(nellRdf, subject, object);
					break;
				}

				case "domain": {
					RdfModelUtils.addPropertyScope(nellRdf, subject, object, Scope.DOMAIN);
					break;
				}

				case "domainwithinrange": {
					RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "domainwithinrange");
					break;
				}

				case "generalizations": {
					RdfModelUtils.addSubsumption(nellRdf, subject, object);
					break;
				}

				case "humanformat": {
					RdfModelUtils.addHumanReadableFormat(nellRdf, subject, object);
					break;
				}

				case "instancetype": {
					RdfModelUtils.addNellInstanceType(nellRdf, subject, object);
					break;
				}

				case "inverse": {
					RdfModelUtils.addInverseProperty(nellRdf, subject, object);
					break;
				}

				case "mutexpredicates": {
					RdfModelUtils.addDisjointness(nellRdf, subject, object);
					break;
				}

				case "nrofvalues": {
					if (object.equals("1")) {
						RdfModelUtils.addFunctionalProperty(nellRdf, subject);
					}
					break;
				}

				case "populate": {
					RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "populate");
					break;
				}

				case "range": {
					RdfModelUtils.addPropertyScope(nellRdf, subject, object, Scope.RANGE);
					break;
				}

				case "rangewithindomain": {
					RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "rangewithindomain");
					break;
				}

				case "visible": {
					RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "visible");
					break;
				}

				default: {
					log.warn("Unkown predicate " + predicate + " in triple (S,P,O) => (" + subject + "," + predicate + "," + object + ")");
					break;
				}
			}
		} catch (final BadScopeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is a consistency checking of the nell in-memory model. Is create the sets of names for classes and properties and lookup the intersection of the two sets. Would the intersection not
	 * be not emptyset, the cardinality of the intersection is the number of (property,class) having the same name. However, given the namespaces for classes and properties is different, they will not
	 * have the same IRI. Because some business logic of this mapping may only rely on names, the model could end up not being consistent. For this purpose, when the interseciton is not null, each
	 * pair of duplicate names between one class and one property is logged using {@link #log} at warning log level. No counter-actions areundertaken byt he program.
	 *
	 * @param nellModel
	 *            a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 */
	public static void countNoneUniqueName(final NellModel nellModel) {
		final Collection<PredicatePair> classesAndProp = nellModel.getOntology().get("memberofsets");
		final Collection<String> classes = new LinkedList<>();
		final Collection<String> props = new LinkedList<>();

		for (final PredicatePair fact : classesAndProp) {
			if (fact.getObject().contains("category")) {
				classes.add(fact.getSubject());
			} else if (fact.getObject().contains("relation")) {
				props.add(fact.getObject());
			} else {
				System.out.println("Triple " + fact.getSubject() + ", \"memberofsets\" " + fact.getObject()
						+ " is a memberofsets predicate not linked to any object whose name contains \"category\" or \"relation\" string.");
			}
		}

		@SuppressWarnings("unchecked")
		final Collection<String> nonUniqueSubjects = CollectionUtils.intersection(classes, props);
		if (nonUniqueSubjects.size() <= 0) {
			log.info("Safety check : all classes and properties contains unique names.");
		} else {
			for (final String nonUNiqueSubject : nonUniqueSubjects) {
				log.warn("Misleading information for memberofsets : duplicate name between prop and classes : " + nonUNiqueSubject + ". This may/should/must lead to inconsistency!");
			}
		}
	}
}