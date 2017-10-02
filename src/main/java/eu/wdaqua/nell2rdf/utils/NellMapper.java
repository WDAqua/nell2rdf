package eu.wdaqua.nell2rdf.utils;

import java.util.Collection;
import java.util.LinkedList;

import eu.wdaqua.nell2rdf.beans.PredicatePair;
import eu.wdaqua.nell2rdf.extract.metadata.util.ConstantList;

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

/**
 * Utility class for generating the mapping between Nell in-memory model of the CSV file, and the targeted Jena model of NELL.
 * @author Christophe Gravier
 */
public class NellMapper {

	public static Logger log = Logger.getLogger(NellMapper.class);

	/**
	 * Initialises an empty in-memory Jena model with prefixes nellclassesn nellproperties, and nellunknown added to model so that Jena writer can use them to abbrev. IRIs.
	 * @return an empty model following OWL_DL specs, stored in-memory.
	 */
	public static Model createEmptyModel(String basePrefix) {
		Model m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		m.setNsPrefix("nellonto", basePrefix + "ontology/");
		return m;
	}

	/**
	 * Lookup classes and properties in the original nell ontology and create them in the nell RDF ontology.
	 * @param nell a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param nellRdf Jena model in which classes and properties should be added.
	 * @param prefix prepend this prefix to any generated IRI by the program
	 */
	public static void lookupClassesAndProperties(NellModel nell, Model nellRdf, String prefix) {
		String predicate = "memberofsets";
		Collection<PredicatePair> classesAndProperties = nell.getOntology().get(predicate);
		int nbC = 0, nbP = 0;
		for (PredicatePair subobj : classesAndProperties) {
			String subject = subobj.getSubject();
			String object = subobj.getObject();
			if (object.toLowerCase().contains("relation")) {
				// we found a new relation, let us map it to a RDFProperty
				nbP++;
				RdfModelUtils.addProperty(nellRdf, subject, prefix);
			} else if (object.toLowerCase().contains("category")) {
				// we found a new class, let us map it to a RDFClass
				nbC++;
				RdfModelUtils.addClass(nellRdf, subject, prefix);
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
	 * @param nell a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param nellRdf Jena model in which classes and properties should be added.
	 * @param prefix prepend this prefix to any generated IRI by the program
	 */
	public static void parseAllPredicates(NellModel nell, Model nellRdf, String prefix) {

		Multimap<String, PredicatePair> allTriples = nell.getOntology();

		// only generalizations
		for (PredicatePair ppair : allTriples.get("generalizations")) {
			RdfModelUtils.addSubsumption(nellRdf, ppair.getSubject(), ppair.getObject(), prefix);
		}

		// all but memberofsets, generalizations
		for (String predicate : allTriples.keySet()) {
			if (!predicate.equals("memberofsets") && !predicate.equals("generalizations")) {
				addAllInstanceForPredicate(nellRdf, prefix, allTriples, predicate);
			}
		}

	}

	/**
	 * For each existing predicates different from "memberofsets"
	 * @param nellRdf Jena model in which classes and properties should be added.
	 * @param prefix prepend this prefix to any generated IRI by the program
	 * @param allTriples all pair of subject/object for a <code>predicate</code>
	 * @param predicate specific predicate for all (subject,object).
	 */
	private static void addAllInstanceForPredicate(Model nellRdf, String prefix, Multimap<String, PredicatePair> allTriples, String predicate) {
		for (PredicatePair subobj : allTriples.get(predicate)) {
			String subject = subobj.getSubject();
			String object = subobj.getObject();
			NellMapper.addTriple(nellRdf, predicate, subject, object, prefix);
		}
	}

	/**
	 * Display some stats on the RDF in memory model after processing the RDF File. These are the same stats that the one that can be displayed on the NELL CSV file using bash script located at
	 * src/main/resources/printNellCsvStats.sh
	 * @param nellRdf NELL ontology as a Jena model.
	 * @param nell a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 * @param prefix prepend this prefix to any generated IRI by the program
	 */
	public static void displayStats(Model nellRdf, NellModel nell) {

		Selector selectorClass = new SimpleSelector(null, RDF.type, RDFS.Class);
		Selector selectorProperty = new SimpleSelector(null, RDF.type, RDF.Property);
		Selector selectorFunctionalProperty = new SimpleSelector(null, RDF.type, OWL.FunctionalProperty);
		Selector selectorDomain = new SimpleSelector(null, RDFS.domain, (RDFNode) null);
		Selector selectorRange = new SimpleSelector(null, RDFS.range, (RDFNode) null);
		Selector selectorSubsomption = new SimpleSelector(null, RDFS.subClassOf, (RDFNode) null);
		Selector selectorInverseOf = new SimpleSelector(null, OWL2.inverseOf, (RDFNode) null);

		StmtIterator classDisjunction = nellRdf.listStatements(new SimpleSelector(null, OWL2.disjointWith, (RDFNode) null) {
			public boolean selects(Statement s) {
				return (s.getSubject().getProperty(RDF.type).equals(RDFS.Class));
			}
		});

		StmtIterator propsDisjunction = nellRdf.listStatements(new SimpleSelector(null, OWL2.propertyDisjointWith, (RDFNode) null) {
			public boolean selects(Statement s) {
				return (!(s.getObject().toString().contains("www.w3.org")));
			}
		});

		Selector selectorComment = new SimpleSelector(null, RDFS.comment, (RDFNode) null);

		Selector selectorPosIrreflexive = new SimpleSelector(null, RDF.type, OWL2.IrreflexiveProperty);
		Selector selectorNegIrreflexive = new SimpleSelector(null, OWL2.propertyDisjointWith, OWL2.IrreflexiveProperty);

		Selector selectorPosSymm = new SimpleSelector(null, RDF.type, OWL2.AsymmetricProperty);
		Selector selectorNegSymm = new SimpleSelector(null, OWL2.propertyDisjointWith, OWL2.AsymmetricProperty);

		Property propInstanceType = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "instancetype");
		Selector selectorInstanceType = new SimpleSelector(null, propInstanceType, (RDFNode) null);

		Property propDomInRange = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "domainwithinrange");
		Selector selectorDomainRange = new SimpleSelector(null, propDomInRange, (RDFNode) null);

		Property propRangeInDom = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "rangewithindomain");
		Selector selectorRangeDomain = new SimpleSelector(null, propRangeInDom, (RDFNode) null);

		Property propPopulate = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "populate");
		Selector selectorPopulate = new SimpleSelector(null, propPopulate, (RDFNode) null);

		Property propVisible = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "visible");
		Selector selectorVisible = new SimpleSelector(null, propVisible, (RDFNode) null);

		Property propHumanformat = nellRdf.getProperty(ConstantList.PREFIX + RdfModelUtils.nellNS + "humanformat");
		Selector selectorHumanFormat = new SimpleSelector(null, propHumanformat, (RDFNode) null);

		int disclass = classDisjunction.toList().size();
		int disprop = propsDisjunction.toList().size();
		int irrefelxivepos = nellRdf.listStatements(selectorPosIrreflexive).toList().size();
		int irrefelxiveneg = nellRdf.listStatements(selectorNegIrreflexive).toList().size();
		int asymmpos = nellRdf.listStatements(selectorPosSymm).toList().size();
		int asymmneg = nellRdf.listStatements(selectorNegSymm).toList().size();

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
	 * @param nellRdf NELL ontology as a Jena model.
	 * @param predicate predicate in the triple (P in <S,<b>P</b>,O>)
	 * @param subject subject in the triple (S in <<b>S</b>,P,O>)
	 * @param object object in the triple (O in <S,P,<b>O</b>>)
	 */
	private static void addTriple(Model nellRdf, String predicate, String subject, String object, String prefix) {

		try {
			switch (predicate) {

			case "antireflexive": {
				RdfModelUtils.addAntiReflexiveProperty(nellRdf, subject, object, prefix);
				break;
			}

			case "antisymmetric": {
				RdfModelUtils.addAntiSymetricProperty(nellRdf, subject, object, prefix);
				break;
			}

			case "description": {
				RdfModelUtils.addDescription(nellRdf, subject, object, prefix);
				break;
			}

			case "domain": {
				RdfModelUtils.addPropertyScope(nellRdf, subject, object, prefix, Scope.DOMAIN);
				break;
			}

			case "domainwithinrange": {
				RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "domainwithinrange", prefix);
				break;
			}

			case "generalizations": {
				RdfModelUtils.addSubsumption(nellRdf, subject, object, prefix);
				break;
			}

			case "humanformat": {
				RdfModelUtils.addHumanReadableFormat(nellRdf, subject, object, prefix);
				break;
			}

			case "instancetype": {
				RdfModelUtils.addNellInstanceType(nellRdf, subject, object, prefix);
				break;
			}

			case "inverse": {
				RdfModelUtils.addInverseProperty(nellRdf, subject, object, prefix);
				break;
			}

			case "mutexpredicates": {
				RdfModelUtils.addDisjointness(nellRdf, subject, object, prefix);
				break;
			}

			case "nrofvalues": {
				if (object.equals("1")) {
					RdfModelUtils.addFunctionalProperty(nellRdf, subject, prefix);
				}
				break;
			}

			case "populate": {
				RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "populate", prefix);
				break;
			}

			case "range": {
				RdfModelUtils.addPropertyScope(nellRdf, subject, object, prefix, Scope.RANGE);
				break;
			}

			case "rangewithindomain": {
				RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "rangewithindomain", prefix);
				break;
			}

			case "visible": {
				RdfModelUtils.addNellBooleanPropertyValue(nellRdf, subject, object, "visible", prefix);
				break;
			}

			default: {
				log.warn("Unkown predicate " + predicate + " in triple (S,P,O) => (" + subject + "," + predicate + "," + object + ")");
				break;
			}
			}
		} catch (BadScopeException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is a consistency checking of the nell in-memory model. Is create the sets of names for classes and properties and lookup the intersection of the two sets. Would the intersection not
	 * be not emptyset, the cardinality of the intersection is the number of (property,class) having the same name. However, given the namespaces for classes and properties is different, they will not
	 * have the same IRI. Because some business logic of this mapping may only rely on names, the model could end up not being consistent. For this purpose, when the interseciton is not null, each
	 * pair of duplicate names between one class and one property is logged using {@link #log} at warning log level. No counter-actions areundertaken byt he program.
	 * @param nellModel a Nell ontology as parsed from CSV file using the {@link NellModel#createModel(String)} method.
	 */
	public static void countNoneUniqueName(NellModel nellModel) {
		Collection<PredicatePair> classesAndProp = nellModel.getOntology().get("memberofsets");
		Collection<String> classes = new LinkedList<String>();
		Collection<String> props = new LinkedList<String>();

		for (PredicatePair fact : classesAndProp) {
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
		Collection<String> nonUniqueSubjects = CollectionUtils.intersection(classes, props);
		if (nonUniqueSubjects.size() <= 0) {
			log.info("Safety check : all classes and properties contains unique names.");
		} else {
			for (String nonUNiqueSubject : nonUniqueSubjects) {
				log.warn("Misleading information for memberofsets : duplicate name between prop and classes : " + nonUNiqueSubject + ". This may/should/must lead to inconsistency!");
			}
		}
	}
}