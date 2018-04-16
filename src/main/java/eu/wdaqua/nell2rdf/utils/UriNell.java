package eu.wdaqua.nell2rdf.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Statement;

public class UriNell {

	private static Map<String, Integer>	numberSequences							= new HashMap<>();

	public static final String			NAMESPACE_PREFIX						= "https://w3id.org/nellrdf/";

	public static final String			NAMESPACE_MIDDLE_NAMED_GRAPHS			= "quad/";
	public static final String			NAMESPACE_MIDDLE_NARY_RELATIONS			= "nary/";
	public static final String			NAMESPACE_MIDDLE_NDFLUENTS				= "nd/";
	public static final String			NAMESPACE_MIDDLE_REIFICATION			= "reif/";
	public static final String			NAMESPACE_MIDDLE_SINGLETON_PROPERTY		= "sp/";
	public static final String			NAMESPACE_END_RESOURCE					= "resource/";
	public static final String			NAMESPACE_END_ONTOLOGY					= "ontology/";
	public static final String			NAMESPACE_END_METADATA					= "metadata/";

	public static final String			NAMESPACE_WIKIPEDIA						= "http://en.wikipedia.org/wiki/";
	public static final String			NAMESPACE_DBPEDIA						= "http://dbpedia.org/resource/";

	public static final String			PROPERTY_HAS_WIKIPEDIA_PAGE				= "haswikipediaurl";

	public static final String			PROPERTY_SUBJECT_PROPERTY				= "subjectProperty";
	public static final String			PROPERTY_OBJECT_PROPERTY				= "objectProperty";

	public static final String			PROPERTY_ASSOCIATED_WITH				= "associatedWith";
	public static final String			PROPERTY_AT_TIME						= "atTime";
	public static final String			PROPERTY_GENERALIZATION_VALUE			= "generalizationValue";
	public static final String			PROPERTY_GENERATED_BY					= "generatedBy";
	public static final String			PROPERTY_ITERATION						= "iteration";
	public static final String			PROPERTY_ITERATION_OF_PROMOTION			= "iterationOfPromotion";
	public static final String			PROPERTY_LATITUDE_VALUE					= "latitudeValue";
	public static final String			PROPERTY_LONGITUDE_VALUE				= "longitudeValue";
	public static final String			PROPERTY_PROBABILITY					= "probability";
	public static final String			PROPERTY_PROBABILITY_OF_BELIEF			= "probabilityOfBelief";
	public static final String			PROPERTY_PROVENANCE						= "provenanceProperty";
	public static final String			PROPERTY_PROVENANCE_DATATYPE			= "provenanceDatatypeProperty";
	public static final String			PROPERTY_PROVENANCE_EXTENT				= "provenanceExtent";
	public static final String			PROPERTY_PROVENANCE_PART_OF				= "provenancePartOf";
	public static final String			PROPERTY_RELATION_VALUE					= "relationValue";
	public static final String			PROPERTY_SOURCE							= "source";
	public static final String			PROPERTY_TOKE_ENTITY					= "tokenEntity";
	public static final String			PROPERTY_TOKEN							= "hasToken";

	public static final String			PROPERTY_FREEBASE_DATE					= "freebaseDate";
	public static final String			PROPERTY_MORPHOLOGICAL_PATTERN			= "morphologicalPattern";
	public static final String			PROPERTY_MORPHOLOGICAL_PATTERN_NAME		= "morphologicalPatternName";
	public static final String			PROPERTY_MORPHOLOGICAL_PATTERN_VALUE	= "morphologicalPatternValue";
	public static final String			PROPERTY_MORPHOLOGICAL_PATTERN_SCORE	= "morphologicalPatternScore";
	public static final String			PROPERTY_PATTERN_OCCURRENCES			= "patternOccurrences";
	public static final String			PROPERTY_TEXTUAL_PATTERN				= "textualPattern";
	public static final String			PROPERTY_NUMBER_OF_OCCURRENCES			= "nbOfOccurrences";
	public static final String			PROPERTY_OLD_BUG						= "oldBug";
	public static final String			PROPERTY_LOCATION						= "location";
	public static final String			PROPERTY_PLACE_NAME						= "name";
	public static final String			PROPERTY_PROMOTED_ENTITY				= "promotedEntity";
	public static final String			PROPERTY_PROMOTED_ENTITY_CATEGORY		= "promotedEntityCategory";
	public static final String			PROPERTY_PROMOTED_RELATION				= "promotedRelation";
	public static final String			PROPERTY_PROMOTED_VALUE					= "promotedValue";
	public static final String			PROPERTY_PROMOTED_VALUE_CATEGORIE		= "promotedValueCategory";

	public static final String			PROPERTY_TEXT_URL						= "textUrl";
	public static final String			PROPERTY_TEXT							= "text";
	public static final String			PROPERTY_URL							= "url";
	public static final String			PROPERTY_ONTOLOGY_MODIFICATION			= "ontologyModification";
	public static final String			PROPERTY_RELATION_PATH					= "relationPath";
	public static final String			PROPERTY_DIRECTION_OF_PATH				= "direction";
	public static final String			PROPERTY_SCORE_OF_PATH					= "score";
	public static final String			PROPERTY_LIST_OF_RELATIONS				= "listOfRelations";
	public static final String			PROPERTY_RULE_SCORES					= "ruleScores";
	public static final String			PROPERTY_RULE							= "rule";
	public static final String			PROPERTY_VARIABLE						= "variable";
	public static final String			PROPERTY_VALUE_OF_VARIABLE				= "valueOfVariable";
	public static final String			PROPERTY_PREDICATE						= "predicate";
	public static final String			PROPERTY_PREDICATE_NAME					= "predicateName";
	public static final String			PROPERTY_FIRST_VARIABLE_OF_PREDICATE	= "firstVariable";
	public static final String			PROPERTY_SECOND_VARIABLE_OF_PREDICATE	= "secondVariable";
	public static final String			PROPERTY_ACCURACY						= "accuracy";
	public static final String			PROPERTY_NUMBER_CORRECT					= "nbCorrect";
	public static final String			PROPERTY_NUMBER_INCORRECT				= "nbIncorrect";
	public static final String			PROPERTY_NUMBER_UNKNOWN					= "nbUnknown";
	public static final String			PROPERTY_SENTENCE						= "sentence";
	public static final String			PROPERTY_USER							= "user";
	public static final String			PROPERTY_ENTITY							= "entity";
	public static final String			PROPERTY_RELATION						= "relation";
	public static final String			PROPERTY_VALUE							= "value";
	public static final String			PROPERTY_ACTION							= "action";
	public static final String			PROPERTY_FILE							= "file";

	public static final String			CLASS_BELIEF							= "Belief";
	public static final String			CLASS_CANDIDATE_BELIEF					= "CandidateBelief";
	public static final String			CLASS_PROMOTED_BELIEF					= "PromotedBelief";
	public static final String			CLASS_TOKEN								= "Token";
	public static final String			CLASS_TOKEN_GENERALIZATION				= "GeneralizationToken";
	public static final String			CLASS_TOKEN_GEO							= "GeoToken";
	public static final String			CLASS_TOKEN_RELATION					= "RelationToken";

	public static final String			CLASS_COMPONENT_EXECUTION				= "ComponentExecution";
	public static final String			CLASS_ALIAS_MATCHER_EXECUTION			= "AliasMatcherExecution";
	public static final String			CLASS_CMC_EXECUTION						= "CMCExecution";
	public static final String			CLASS_CPL_EXECUTION						= "CPLExecution";
	public static final String			CLASS_KB_MANIPULATION_EXECUTION			= "KbManipulationExecution";
	public static final String			CLASS_LE_EXECUTION						= "LEExecution";
	public static final String			CLASS_LATLONG_EXECUTION					= "LatLongExecution";
	public static final String			CLASS_MBL_EXECUTION						= "MBLExecution";
	public static final String			CLASS_OE_EXECUTION						= "OEExecution";
	public static final String			CLASS_ONTOLOGY_MODIFIER_EXECUTION		= "OntologyModifierExecution";
	public static final String			CLASS_PRA_EXECUTION						= "PRAExecution";
	public static final String			CLASS_RL_EXECUTION						= "RLExecution";
	public static final String			CLASS_SEAL_EXECUTION					= "SEALExecution";
	public static final String			CLASS_SEMPARSE_EXECUTION				= "SemparseExecution";
	public static final String			CLASS_SPREADSHEETEDITS_EXECUTION		= "SpreadsheetEditsExecution";

	public static final String			CLASS_COMPONENT							= "Component";
	public static final String			RESOURCE_ALIAS_MATCHER					= "AliasMatcher";
	public static final String			RESOURCE_CMC							= "CMC";
	public static final String			RESOURCE_CPL							= "CPL";
	public static final String			RESOURCE_KB_MANIPULATION				= "KbManipulation";
	public static final String			RESOURCE_LE								= "LE";
	public static final String			RESOURCE_LATLONG						= "LatLong";
	public static final String			RESOURCE_MBL							= "MBL";
	public static final String			RESOURCE_OE								= "OE";
	public static final String			RESOURCE_ONTOLOGY_MODIFIER				= "OntologyModifier";
	public static final String			RESOURCE_PRA							= "PRA";
	public static final String			RESOURCE_RL								= "RL";
	public static final String			RESOURCE_SEAL							= "SEAL";
	public static final String			RESOURCE_SEMPARSE						= "Semparse";
	public static final String			RESOURCE_SPREADSHEETEDITS				= "SpreadsheetEdits";

	public static final String			CLASS_MORPHOLOGICAL_PATTERN				= "MorphologicalPatternScoreTriple";
	public static final String			RESOURCE_MORPHOLOGICAL_PATTERN			= "MorphologicalPattern";
	public static final String			CLASS_PATTERN_OCCURRENCE				= "PatternNbOfOccurrencesPair";
	public static final String			RESOURCE_PATTERN_OCCURRENCE				= "PatternOccurrence";
	public static final String			CLASS_NAMELATLONG_TRIPLE				= "NameLatLongTriple";
	public static final String			RESOURCE_NAMELATLONG_TRIPLE				= "NameLatLong";
	public static final String			CLASS_TEXT_URL							= "TextUrlPair";
	public static final String			RESOURCE_TEXT_URL						= "TextUrl";
	public static final String			CLASS_RELATION_PATH						= "Path";
	public static final String			RESOURCE_RELATION_PATH					= "RelationPath";
	public static final String			CLASS_RULE_SCORES_TUPLE					= "RuleScoresTuple";
	public static final String			RESOURCE_RULE_SCORES_TUPLE				= "RuleScores";
	public static final String			CLASS_RULE								= "Rule";
	public static final String			RESOURCE_RULE							= "Rule";
	public static final String			CLASS_VARIABLE							= "Variable";
	public static final String			RESOURCE_VARIABLE						= "Variable";
	public static final String			CLASS_PREDICATE							= "Predicate";
	public static final String			RESOURCE_PREDICATE						= "Predicate";

	// public static final String RESOURCE_BELIEF = "belief";
	// public static final String RESOURCE_TOKEN = "token";
	public static final String			RESOURCE_TOKEN_RELATION					= "relationToken";
	public static final String			RESOURCE_TOKEN_GENERALIZATION			= "generalizationToken";
	public static final String			RESOURCE_TOKEN_GEO						= "geoToken";

	public static final String			SUFFIX_PROMOTED							= "Promoted";
	public static final String			SUFFIX_CANDIDATE						= "Candidate";

	public static final String			CLASS_LINK								= "ExternalLink";
	public static final String			PROPERTY_GENERATED_AT_TIME				= "generatedAtTime";

	public static final String			NAMESPACE_NDFLUENTS						= "http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#";
	public static final String			CLASS_PROVENANCE_PART					= NAMESPACE_NDFLUENTS + "ContextualPart";

	public static final String			NAMESPACE_SINGLETON_PROPERTY			= "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public static final String			SINGLETON_PROPERTY_OF					= NAMESPACE_SINGLETON_PROPERTY + "singletonPropertyOf";
	public static final String			TYPE_SINGLETON_PROPERTY					= NAMESPACE_SINGLETON_PROPERTY + "SingletonProperty";

	public static final String			ENGLISH_TAG								= "en";

	protected static String				base									= NAMESPACE_PREFIX;

	public static void setBase(final String base) {
		UriNell.base = base;
	}

	public static final String replaceCharacters(final String name) {
		return name.replaceAll("\\s", "").replaceAll(":", "_").replaceAll("%20", "_").replaceAll("\"", "");
	}

	public static final String createSequentialName(final String name) {
		return name + "_" + numberSequences.compute(name, (K, V) -> V == null ? 1 : ++V);
	}

	public static final String createUri(final String namespace, final String localname) {
		return namespace + replaceCharacters(localname);
	}

	public static final String getMetadataUri(final String name) {
		return createUri(base + NAMESPACE_END_METADATA, name);
	}

	public static final String getOntologyUri(final String name) {
		return createUri(base + NAMESPACE_END_ONTOLOGY, name);
	}

	public static final String getResourceUri(final String name) {
		return createUri(base + NAMESPACE_END_RESOURCE, name);
	}

	public static String createAnchorUri(final Statement triple) {
		final String subjectString = triple.getSubject().getLocalName();
		final String predicateString = triple.getPredicate().getLocalName();
		final String objectString = triple.getObject().isResource() ? triple.getObject().asResource().getLocalName() : String.valueOf(triple.getObject().asLiteral().getString().hashCode());
		return getMetadataUri(subjectString + "_" + predicateString + "_" + objectString);
	}

}
