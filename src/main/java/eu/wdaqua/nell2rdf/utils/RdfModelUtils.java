package eu.wdaqua.nell2rdf.utils;

import eu.wdaqua.nell2rdf.utils.exceptions.BadScopeException;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;

/**
 * Utility class for manipulating a Jena model of NELL.
 * @author Christophe Gravier
 */

public class RdfModelUtils {

	public static Logger log = Logger.getLogger(RdfModelUtils.class);

	public static final String nellNS = "ontology/";
	
//	/** Namespace for properties */
//	public static final String propNS = "properties/";
//
//	/** Namespace for classes */
//	public static final String classNS = "classes/";
//
//	/** Namespace for NELL predicates that should be properties by are not mappable to RDFS/OWL or we are unsure on what it means. */
//	public static final String nellPredNS = "nellpredicates/";

	/**
	 * Enumerate type for a Property scope (whether range or domain). This is used to discriminate addRAnge or addDomain primitives.
	 */
	public enum Scope {
		RANGE, DOMAIN
	}

	/**
	 * Add a new property to the Nell RDF model.<br />
	 * The assertions that can be created in this method are <code>(Resource, RDF.type RDF.Property)</code>
	 * @param nellRdf the nell RDF Jena model instance
	 * @param propertyName name of the property to add
	 * @param prefix Prefix for the URI to construct, excluding the NS suffix for properties that is configured in this class at {@link #propNS}
	 */
	public static void addProperty(Model nellRdf, String propertyName, String prefix) {
		Resource newProperty = nellRdf.createProperty(prefix + nellNS + propertyName);
		newProperty.addProperty(RDF.type, RDF.Property);
	}

	/**
	 * Add a new property to the Nell RDF model that is a NELL predicate. It is the same as {@link #addProperty(Model, String, String)} but the generated IRI is different since nell predicate
	 * namespace is inserted between the prefix and the property name, where in the other method is it the property namespace that is inserted.<br />
	 * The assertions that can be created in this method are <code>(Resource, RDF.type RDF.Property)</code>
	 * @param nellRdf the nell RDF Jena model instance
	 * @param propertyName name of the property to add
	 * @param prefix Prefix for the URI to construct, excluding the NS suffix for properties that is configured in this class at {@link #nellPredNS}
	 */
	public static void addNellProperty(Model nellRdf, String propertyName, String prefix) {
		Resource newProperty = nellRdf.createProperty(prefix + nellNS + propertyName);
		nellRdf.createStatement(newProperty, RDF.type, RDF.Property);
	}

	/**
	 * Add a new class to the Nell RDF model.<br />
	 * The assertions that can be created in this method are <code>(Resource, RDF.type RDFS.Class)</code>
	 * @param nellRdf the nell RDF Jena model instance
	 * @param className name of the class to add
	 * @param prefix Prefix for the URI to construct, excluding the NS suffix for classes that is configured in this class at {@link #classNS}
	 */
	public static void addClass(Model nellRdf, String className, String prefix) {
		Resource newClass = nellRdf.createResource(prefix + nellNS + className);
		newClass.addProperty(RDF.type, RDFS.Class);
	}

	/**
	 * Specify if a property is irreflexive (= antireflexive in Nell vabulary), or not. If the property has not been defined, it is added to the RDF model beforehand.<br />
	 * The assertions that can be created in this method are :
	 * <ul>
	 * <li><code>(Resource, RDF.type, OWL2.IrreflexiveProperty)</code>, when the object for the "irreflexive" predicate equals to true</li>
	 * <li><code>(Resource, OWL2.propertyDisjointWith, OWL2.IrreflexiveProperty)</code>, when the object for the "irreflexive" predicate equals to false</li>
	 * </ul>
	 * This latter one is a little trickier : it means that the property is disjoint with the anonymous class representing all the property that are being irreflexive.
	 * @param nellRdf Nell RDF Jean Model
	 * @param propertyName name of the property
	 * @param antireflexiveValue Value in Nell ontology for antireflexivity property, which is whether "true" or "false" as strings.
	 * @param prefix domain prefix for these IRI, excluding properties NS.
	 */
	public static void addAntiReflexiveProperty(Model nellRdf, String propertyName, String antireflexiveValue, String prefix) {

		// for safety purpose.
		antireflexiveValue += antireflexiveValue.trim();

		// add property to model if not exist.
		String iri = prefix + nellNS + propertyName;
		if (!isProperty(iri, nellRdf)) {
			addProperty(nellRdf, propertyName, prefix);
		}

		// set irreflexive info ....
		Resource property = nellRdf.getResource(iri);
		if (antireflexiveValue.contains("false")) {
			// ... in this case the property is disjoint with properties that
			// are irreflexive.
			property.addProperty(OWL2.propertyDisjointWith, OWL2.IrreflexiveProperty);
		} else if (antireflexiveValue.contains("true")) {
			// ... in this case the property is irreflexive.
			property.addProperty(RDF.type, OWL2.IrreflexiveProperty);
		}
	}

	/**
	 * Specify if a property is antisymmetric or if it explicitly not an antisymmetric one. If the property has not been defined, it is added to the RDF model beforehand. The assertions that can be
	 * created in this method are :
	 * <ul>
	 * <li><code>(Resource, RDF.type, OWL2.AsymmetricProperty)</code>, when the object for the "asymmetric" predicate equals to true</li>
	 * <li><code>(Resource, OWL2.propertyDisjointWith, OWL2.AsymmetricProperty)</code>, when the object for the "asymmetric" predicate equals to false</li>
	 * </ul>
	 * This latter one is a little trickier : it means that the property is disjoint with the anonymous class representing all the property that are being symmetric.
	 * @param nellRdf Nell RDF Jena Model
	 * @param propertyName name of the property
	 * @param antisymmValue Value in Nell ontology for antisymmetric property, which is whether "true" or "false" as strings.
	 * @param prefix domain prefix for these IRI, excluding properties NS.
	 */
	public static void addAntiSymetricProperty(Model nellRdf, String propertyName, String antisymmValue, String prefix) {

		// for safety purpose.
		antisymmValue += antisymmValue.trim();

		// add property tomodel if not exist.
		String iri = prefix + nellNS + propertyName;
		if (!isProperty(iri, nellRdf)) {
			addProperty(nellRdf, propertyName, prefix);
		}

		// set irreflexive info ....
		Resource property = nellRdf.getResource(iri);
		if (antisymmValue.contains("false")) {
			// ... in this case the property is disjoint with properties that
			// are irreflexive.
			property.addProperty(OWL2.propertyDisjointWith, OWL2.AsymmetricProperty);
		} else if (antisymmValue.contains("true")) {
			// ... in this case the property is irreflexive.
			property.addProperty(RDF.type, OWL2.AsymmetricProperty);
		}
	}

	/**
	 * Add description to a property or a class. It first scans the model to find a class, and if not lookup a property.<br />
	 * The assertions that can be created in this method are <code>(Resource, RDFS.comment Literal^"en")</code>
	 * @param subject Subject that will receive a description.
	 * @param description the value of this description.
	 * @param prefix domain prefix for these IRI, excluding properties NS.
	 */
	public static void addDescription(Model nellRdf, String subject, String description, String prefix) {

		String iri = prefix + nellNS + subject;

		// TODO all description are expected to be in english. While it is the case until now for NELL ontology, they are no guarantee that it will remain all inenglish. We should check the language
		// of the descrption using a language detector algorithm. Apache TIKA provides a nice implementation of such language detector, provided the text is large enough (dozen+ words).
		Literal com = nellRdf.createLiteral(description, "en");
		if (isClass(iri, nellRdf)) {
			Resource myClass = nellRdf.getResource(iri);
			myClass.addProperty(RDFS.comment, com);
		} else if (isProperty(prefix + nellNS + subject, nellRdf)) {
			Resource myClass = nellRdf.getResource(iri);
			myClass.addProperty(RDFS.comment, com);
		} else {
			log.error("Cannot a description to " + iri + " : not a class or a property.");
		}
	}

	/**
	 * Add to Nell RDF model that subclass is specialization if superclass. Create classes in case neither subClass or superClass exits in Nell RDF Model.<br />
	 * The assertions that can be created in this method are <code>(RDFS.Class, RDFS.subClassOf RDFS.Class)</code>
	 * @param nellRdf Nell RDF model.
	 * @param subClass name of the sub class in the subsumption.
	 * @param superClass name of the super class in the subsumption.
	 * @param prefix Domain prefix of classes.
	 */
	public static void addSubsumption(Model nellRdf, String subClass, String superClass, String prefix) {

		String iriSubClass = prefix + nellNS + subClass;
		String iriSuperClass = prefix + nellNS + superClass;
		if (!isClass(iriSubClass, nellRdf)) {
			RdfModelUtils.addClass(nellRdf, subClass, prefix);
		}

		if (!isClass(iriSuperClass, nellRdf)) {
			RdfModelUtils.addClass(nellRdf, superClass, prefix);
		}
		Resource rsc = nellRdf.getResource(prefix + nellNS + subClass);
		Resource sup = nellRdf.getResource(prefix + nellNS + superClass);
		rsc.addProperty(RDFS.subClassOf, sup);
	}

	/**
	 * Add the information on human readable format from NELL for a class or property. The method look for a class with <code>resource</code>'s name, and if not found try to find a Property with that
	 * name. The human readable format is added to the resource that is found, or an error is log via the logger {@link #log} if no class or property corresponds to that name.<br />
	 * The assertions that can be created in this method are <code>(Resource, (prefix)(this.nellPredNS)/humanformat Literal^"en")</code>
	 * @param nellRdf The NELL ontology as a Jena model.
	 * @param resource a resource local name (class or property) we want to set a <code>humanFormater</code>
	 * @param humanFormater String representing how to print this information for a human.
	 * @param prefix Prefix of the NELL ontology.
	 */
	public static void addHumanReadableFormat(Model nellRdf, String resource, String humanFormater, String prefix) {
		Literal hFormat = nellRdf.createLiteral(humanFormater, "en");

		String iriClass = prefix + nellNS + resource;
		Resource humanReadableResource = null;
		if (isClass(iriClass, nellRdf)) {
			humanReadableResource = nellRdf.getResource(iriClass);
		} else {
			String iriProp = prefix + nellNS + resource;
			if (isProperty(iriProp, nellRdf)) {
				humanReadableResource = nellRdf.getResource(iriProp);
			}
		}

		if (humanReadableResource != null) {
			Property iriHumanReadableProp = nellRdf.getProperty(prefix + nellNS + "humanformat");
			humanReadableResource.addProperty(iriHumanReadableProp, hFormat);
		} else {
			log.error("Resource " + resource + " is neither or class nor a property !");
		}
	}

	/**
	 * Add disjunction between two resources that are both classes or both properties. It first search the ontology for two classes with the two given names as parameters :
	 * <ul>
	 * <li>If found, a new disjoint class assertion is added to the model, using {@link #createClassDisjunction(Resource, Resource, Model)} method</li>
	 * <li>If not found, it tries to find properties with these names. If found, property disjunction is added to the model using {@link #createPropertyDisjunction(Resource, Resource, Model)}, else
	 * the error is logged. using {@link #log}.</li>
	 * </ul>
	 * No assertions are directly created in this method, instead it calls {@link #createClassDisjunction(Resource, Resource, Model)} or {@link #createPropertyDisjunction(Resource, Resource, Model)}
	 * depending on the kind of the subject in the triple.
	 * @param nellRdf instance of a jena Model that corresponds to NELL ontolgoy in RDF.
	 * @param subject one resource disjuncts with <code>object</code>
	 * @param object one resource disjuncts with <code>subject</code>
	 * @param prefix Starting prefix for all resources in your NELL RDF namespace.
	 */
	public static void addDisjointness(Model nellRdf, String subject, String object, String prefix) {
		String iriSubject = prefix + nellNS + subject;
		String iriObject = prefix + nellNS + object;

		if (isClass(iriSubject, nellRdf) && isClass(iriObject, nellRdf)) {
			Resource oneClass = nellRdf.getResource(iriSubject);
			Resource otherClass = nellRdf.getResource(iriObject);
			createClassDisjunction(oneClass, otherClass, nellRdf);
		} else {
			iriSubject = prefix + nellNS + subject;
			iriObject = prefix + nellNS + object;
			if (isProperty(iriSubject, nellRdf) && isProperty(iriObject, nellRdf)) {
				Resource oneProp = nellRdf.getResource(iriSubject);
				Resource otherProp = nellRdf.getResource(iriObject);
				createPropertyDisjunction(oneProp, otherProp, nellRdf);
			} else {
				log.error("The resource " + subject + " and " + object + " are not both property or both classes, they cannot be disjoint.");
			}
		}
	}

	/**
	 * Create a new functional property assertion in the NELL RDF ontology. If the property cannot be found first, it is created beforehand.<br />
	 * The assertions that can be created in this method are <code>(Resource, RDF.Type OWL.FunctionalProperty)</code>
	 * @param nellRdf instance of a jena Model that corresponds to NELL ontology in RDF.
	 * @param propertyName name of the property that is to become functional.
	 * @param prefix Starting prefix for all resources in your NELL RDF namespace.
	 */
	public static void addFunctionalProperty(Model nellRdf, String propertyName, String prefix) {
		// add property to model if not exist...
		String iri = prefix + nellNS + propertyName;
		if (!isProperty(iri, nellRdf)) {
			addProperty(nellRdf, propertyName, prefix);
		}

		// ... then add the assertion about this property being functional
		Resource functionalProperty = nellRdf.getResource(iri);
		functionalProperty.addProperty(RDF.type, OWL.FunctionalProperty);
	}

	/**
	 * Add a scope (range or domain that is a RDF class) to an Property. If this property was not yet declared as ObjectProperty, the assertion is added.<br />
	 * If the property or the class do not exist, they are added to the model beforehand. In case the scope is not equal to Scope.RANGE or Scope.DOMAIN, a BadScopeException is raised.<br />
	 * The assertions that can be created in this method are :
	 * <ol>
	 * <li><code>(Resource, RDF.type OWL.ObjectProperty)</code></li>
	 * <li>Then :
	 * <ul>
	 * <li><code>(Resource RDF.Domain (prefix)(this.nellPredNS)/scopeName)</code> when <code>rangeOrDomain</code> equals Scope.DOMAIN</li>
	 * <li><code>(Resource RDF.Range (prefix)(this.nellPredNS)/scopeName)</code> when <code>rangeOrDomain</code> equals Scope.RANGE</li>
	 * </ul>
	 * </ol>
	 * @param nellRdf instance of a Jena Model that corresponds to NELL ontology in RDF.
	 * @param propertyName name of the object property whose range is <code>rangeName</code>
	 * @param scopeName name of the range or domain for this <code>propertyName</code>
	 * @param prefix Starting prefix for all resources in your NELL RDF namespace.
	 * @param rangeOrDomain Scope.RANGE or Scope.DOMAIN depending on the kind of scope information to add to the property.
	 * @throws BadScopeException when param rangeOrDomain does not equals Scope.RANGE or Scope.DOMAIN
	 */
	public static void addPropertyScope(Model nellRdf, String propertyName, String scopeName, String prefix, Scope rangeOrDomain) throws BadScopeException {

		if (rangeOrDomain != Scope.DOMAIN && rangeOrDomain != Scope.RANGE) {
			throw new BadScopeException(rangeOrDomain);
		}

		// add property to model if not exist.
		String propertyIri = prefix + nellNS + propertyName;
		if (!isProperty(propertyIri, nellRdf)) {
			addProperty(nellRdf, propertyName, prefix);
		}

		// add class to model if not exist.
		String rangeIri = prefix + nellNS + scopeName;
		if (!isClass(rangeIri, nellRdf)) {
			addClass(nellRdf, scopeName, prefix);
		}

		Resource objectProperty = nellRdf.getResource(propertyIri);
		Resource rangeClass = nellRdf.getResource(rangeIri);

		// make the property an ObjectProperty if it has not, and set its range.
		objectProperty.addProperty(RDF.type, OWL.ObjectProperty);

		Property scopeProperty = (rangeOrDomain == Scope.DOMAIN) ? RDFS.domain : RDFS.range;
		objectProperty.addProperty(scopeProperty, rangeClass);
	}

	/**
	 * State that <code>anotherProperty</code> is the inverse property of <code>oneProperty</code>. If <code>oneProperty</code> appears to be functional, <code>anotherProperty</code> is added the RDF
	 * type "inverseFunctionalProperty". The method checks the existence of each property, and create them to the Jena model in case of absence.<br />
	 * The assertions that can be created in this method are :
	 * <ul>
	 * <li><code>(Resource RDF.type OWL.InverseFunctionalProperty)</code> (if oneProperty was functional)</li>
	 * <li><code>(Resource OWL.inverseOf Resource)</code> in all case</li>
	 * </ul>
	 * @param nellRdf The NELL ontology as a Jena model.
	 * @param oneProperty an ObjectProperty. Reminder: no inverse property can be declared for OWL DatatyProperty, expect in OWL Full <a
	 *            href="http://www.w3.org/TR/2004/REC-owl-guide-20040210/#InverseFunctionalProperty">as specified here.</a>
	 * @param anotherProperty the inverse property of <code>oneProperty</code>
	 * @param prefix Prefix for the NELL ontology in the Jena model.
	 */
	public static void addInverseProperty(Model nellRdf, String oneProperty, String anotherProperty, String prefix) {

		// add properties to model if not exist.
		String onePropertyIri = prefix + nellNS + oneProperty;
		String anotherPropertyIri = prefix + nellNS + anotherProperty;

		if (!isProperty(onePropertyIri, nellRdf)) {
			addProperty(nellRdf, onePropertyIri, prefix);
		}
		if (!isProperty(anotherPropertyIri, nellRdf)) {
			addProperty(nellRdf, anotherPropertyIri, prefix);
		}

		// Set anotherProperty to be an inverseProperty of oneProperty
		Resource onePropertyResource = nellRdf.getResource(onePropertyIri);
		Resource anotherPropertyResource = nellRdf.getResource(anotherPropertyIri);
		if (onePropertyResource.hasProperty(RDF.type, OWL.FunctionalProperty)) {
			anotherPropertyResource.addProperty(RDF.type, OWL.InverseFunctionalProperty);
		}
		onePropertyResource.addProperty(OWL.inverseOf, anotherPropertyResource);
	}

	/**
	 * Add disjunction between two classes. If the two classes have the same URI, an error is logged to {@link #log} and the disjoint is not added to <code>nellRdf</code> (Jena model)<br />
	 * The assertions that can be created in this method are <code>Resource OWL2.disjointWith Resource</code>
	 * @param oneClass Resource in the Jena Model of one class.
	 * @param anotherClass Resource in the Jena Model of another class than <code>oneClass</code>.
	 * @param nellRdf Jena model of the Nell ontology model.
	 */
	public static void createClassDisjunction(Resource oneClass, Resource anotherClass, Model nellRdf) {
		if (oneClass.getURI().equals(anotherClass.getURI()) && oneClass.getURI() != null) {
			log.error("Class " + oneClass + " cannot be disjoint with itself !");
		} else {
			oneClass.addProperty(OWL2.disjointWith, anotherClass);
		}
	}

	/**
	 * Add disjunction between two properties. If the two propoerties have the same URI, an error is logged to {@link #log} and the disjoint is not added to <code>nellRdf</code> (Jena model)<br />
	 * The assertions that can be created in this method are <code>Resource OWL2.propertyDisjointWith Resource</code>
	 * @param oneProperty Resource in the Jena Model of one property.
	 * @param anotherProperty Resource in the Jena Model of another property.
	 * @param nellRdf Jena model of the Nell ontology model.
	 */
	public static void createPropertyDisjunction(Resource oneProperty, Resource anotherProperty, Model nellRdf) {
		if (oneProperty.getURI().equals(anotherProperty.getURI()) && oneProperty.getURI() != null) {
			log.error("Class " + anotherProperty + " cannot be disjoint with itself !");
		} else {
			oneProperty.addProperty(OWL2.propertyDisjointWith, anotherProperty);
		}
	}

	/**
	 * Initializes NELL predicates that are not yet mapped to RDF/RDFS/OWL/OWL2 properties (unsure of their meaning or no equivalent is available).
	 * @param nellRdf the NELL ontology as a Jena model.
	 * @param prefix Prefix to use for creating namespaces.
	 */
	public static void createNellProperties(Model nellRdf, String prefix) {
		addNellProperty(nellRdf, "humanformat", prefix);
		addNellProperty(nellRdf, "populate", prefix);
		addNellProperty(nellRdf, "instancetype", prefix);
		addNellProperty(nellRdf, "domainwithinrange", prefix);
		addNellProperty(nellRdf, "rangewithindomain", prefix);
		addNellProperty(nellRdf, "visible", prefix);
	}

	/**
	 * Test if the given <code>iri</code> corresponds to a resource r such as (r RDF.type RDF.Property).
	 * @param iri The IRI to lookup.
	 * @param nellRdf the NELL ontology as a Jena model.
	 * @return true if the property was found <b>and</b> that the resource at this IRI has the RDF type RDF.Property, false otherwise.
	 */
	private static boolean isProperty(String iri, Model nellRdf) {
		Resource expectedProperty = nellRdf.getResource(iri);
		return (expectedProperty != null && expectedProperty.hasProperty(RDF.type, RDF.Property));
	}

	/**
	 * Test if the given <code>iri</code> corresponds to a resource r such as (r RDF.type RDFS.Class).
	 * @param iri The IRI to lookup.
	 * @param nellRdf the NELL ontology as a Jena model.
	 * @return true if the class was found <b>and</b> that the resource at this IRI has the RDF type RDFS.Class, false otherwise.
	 */
	private static boolean isClass(String iri, Model nellRdf) {
		Resource expectedProperty = nellRdf.getResource(iri);
		return (expectedProperty != null && expectedProperty.hasProperty(RDF.type, RDFS.Class));
	}

	/**
	 * Add a prefix+nellPredNS+"instancetype" property betwen a <code>subject</code> and an <code>object</code>.<br />
	 * The assertions that can be created in this method are <code>Resource (prefix)(this.nellPredNS)/instancetype Literal^"en"</code>
	 * @param nellRdf the NELL ontology as a Jena model.
	 * @param subject Subject of the triple
	 * @param object Object of the triple
	 * @param prefix prefix to use when generating new IRI.
	 */
	public static void addNellInstanceType(Model nellRdf, String subject, String object, String prefix) {
		Resource subjectProp = nellRdf.getProperty(prefix + nellNS + subject);
		if (subjectProp == null) {
			log.error("Property " + subject + " cannot be found, hence instancetype with value " + object + " cannot be set.");
			return;
		}

		Property prop = nellRdf.getProperty(prefix + nellNS + "instancetype");
		if (prop == null) {
			log.error("Error, property " + prefix + nellNS + " instancetype cannot be found, although it should have been created earlier.");
		}

		Literal booleanLit = nellRdf.createLiteral(object, "en");
		subjectProp.addProperty(prop, booleanLit);
	}

	/**
	 * Add a specific NELL triple into the Jena model where the object of the triple is a string representing a boolean. The triple uses a property that was previously declared into prefix+NellPredNS
	 * by the method {@link #createNellProperties(Model, String)}
	 * @param nellRdf The NELL ontology as a Jena model.
	 * @param subject name of the subject in the nell triple.
	 * @param object "true" or "false"
	 * @param booleanRelationName name of the NELL boolean property.
	 * @param prefix NELL RDF prefix.
	 */
	static void addNellBooleanPropertyValue(Model nellRdf, String subject, String object, String booleanRelationName, String prefix) {
		Resource subjectProp = nellRdf.getProperty(prefix + nellNS + subject);
		if (subjectProp == null) {
			log.error("Property " + subject + " cannot be found, hence " + booleanRelationName + " with value " + object + " cannot be set.");
			return;
		}

		Property prop = nellRdf.getProperty(prefix + nellNS + booleanRelationName);
		if (prop == null) {
			log.error("Error, property " + prefix + nellNS + booleanRelationName + " cannot be found, although it hsould have been created earlier.");
		}

		Literal booleanLit = null;
		if (object.contains("true")) {
			booleanLit = nellRdf.createTypedLiteral(true);
			subjectProp.addProperty(prop, booleanLit);
		} else if (object.contains("false")) {
			booleanLit = nellRdf.createTypedLiteral(false);
			subjectProp.addProperty(prop, booleanLit);
		} else {
			log.error("rangewithingdomain neither true or false ! (value : " + object + ")");
		}
	}
}
