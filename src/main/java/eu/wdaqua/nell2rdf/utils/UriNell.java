package eu.wdaqua.nell2rdf.utils;

import java.util.HashMap;
import java.util.Map;

public class UriNell {

	private static Map<String,Integer> numberSequences = new HashMap<>();
	
	public static final String NAMESPACE_PREFIX = "https://w3id.org/nellrdf/";
	public static final String NAMESPACE_MIDDLE_NAMED_GRAPHS = "quad/";
	public static final String NAMESPACE_MIDDLE_NARY_RELATIONS = "nary/"; 
	public static final String NAMESPACE_MIDDLE_NDFLUENTS = "nd/";
	public static final String NAMESPACE_MIDDLE_REIFICATION = "reif/";
	public static final String NAMESPACE_MIDDLE_SINGLETON_PROPERTY = "sp/";
	public static final String NAMESPACE_END_RESOURCE = "resource/";
	public static final String NAMESPACE_END_ONTOLOGY = "ontology/";
	public static final String NAMESPACE_END_METADATA = "metadata/";
	
	//public static final String PREFIX_METADATA = "nellmeta";
	//public static final String PREFIX_ONTOLOGY = "nellonto";
	//public static final String PREFIX_RESOURCE = "nell";
	
	public static final String PROPERTY_SUBJECT_PROPERTY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "subjectProperty";
	public static final String PROPERTY_OBJECT_PROPERTY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "objectProperty";
	
	public static final String PROPERTY_ASSOCIATED_WITH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "associatedWith";
	public static final String PROPERTY_AT_TIME = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "atTime";
	public static final String PROPERTY_GENERALIZATION_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "generalizationValue";
	public static final String PROPERTY_GENERATED_BY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "generatedBy";
	public static final String PROPERTY_ITERATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "iteration";
	public static final String PROPERTY_ITERATION_OF_PROMOTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "iterationOfPromotion";
	public static final String PROPERTY_LATITUDE_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "latitudeValue";
	public static final String PROPERTY_LONGITUDE_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "longitudeValue";
	public static final String PROPERTY_PROBABILITY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "probability";
	public static final String PROPERTY_PROBABILITY_OF_BELIEF = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "probabilityOfBelief";
	public static final String PROPERTY_PROVENANCE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "provenanceProperty";
	public static final String PROPERTY_PROVENANCE_DATATYPE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "provenanceDatatypeProperty";
	public static final String PROPERTY_PROVENANCE_EXTENT = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "provenanceExtent";
	public static final String PROPERTY_PROVENANCE_PART_OF = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "provenancePartOf";
	public static final String PROPERTY_RELATION_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "relationValue";
	public static final String PROPERTY_SOURCE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "source";
	public static final String PROPERTY_TOKE_ENTITY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "tokenEntity";
	public static final String PROPERTY_TOKEN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "hasToken";
	
	public static final String PROPERTY_FREEBASE_DATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "freebaseDate";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "morphologicalPattern";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_NAME = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternName";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternValue";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_SCORE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternScore";
	public static final String PROPERTY_PATTERN_OCCURRENCES = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "patternOccurrences";
	public static final String PROPERTY_TEXTUAL_PATTERN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "textualPattern";
	public static final String PROPERTY_NUMBER_OF_OCCURRENCES = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "nbOfOccurrences";
	public static final String PROPERTY_OLD_BUG = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "oldBug";
	public static final String PROPERTY_LOCATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "location";
	public static final String PROPERTY_PLACE_NAME = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "name";
	public static final String PROPERTY_PROMOTED_ENTITY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "promotedEntity";
	public static final String PROPERTY_PROMOTED_ENTITY_CATEGORY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "promotedEntityCategory";
	public static final String PROPERTY_PROMOTED_RELATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "promotedRelation";
	public static final String PROPERTY_PROMOTED_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "promotedValue";
	public static final String PROPERTY_PROMOTED_VALUE_CATEGORIE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "promotedValueCategory";
	
	public static final String PROPERTY_TEXT_URL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "textUrl";
	public static final String PROPERTY_TEXT = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "text";
	public static final String PROPERTY_URL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "url";
	public static final String PROPERTY_ONTOLOGY_MODIFICATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "ontologyModification";
	public static final String PROPERTY_RELATION_PATH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "relationPath";
	public static final String PROPERTY_DIRECTION_OF_PATH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "direction";
	public static final String PROPERTY_SCORE_OF_PATH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "score";
	public static final String PROPERTY_LIST_OF_RELATIONS = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "listOfRelations";
	public static final String PROPERTY_RULE_SCORES = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "ruleScores";
	public static final String PROPERTY_RULE= NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "rule";
	public static final String PROPERTY_VARIABLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "variable";
	public static final String PROPERTY_VALUE_OF_VARIABLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "valueOfVariable";
	public static final String PROPERTY_PREDICATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "predicate";
	public static final String PROPERTY_PREDICATE_NAME = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "predicateName";
	public static final String PROPERTY_FIRST_VARIABLE_OF_PREDICATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "firstVariable";
	public static final String PROPERTY_SECOND_VARIABLE_OF_PREDICATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "secondVariable";
	public static final String PROPERTY_ACCURACY =  NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "accuracy";
	public static final String PROPERTY_NUMBER_CORRECT =  NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "nbCorrect";
	public static final String PROPERTY_NUMBER_INCORRECT =  NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "nbIncorrect";
	public static final String PROPERTY_NUMBER_UNKNOWN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "nbUnknown";	
	public static final String PROPERTY_SENTENCE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "sentence";
	public static final String PROPERTY_USER = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "user";
	public static final String PROPERTY_ENTITY = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "entity";
	public static final String PROPERTY_RELATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "relation";
	public static final String PROPERTY_VALUE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "value";
	public static final String PROPERTY_ACTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "action";
	public static final String PROPERTY_FILE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "file";
	
	public static final String CLASS_BELIEF = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Belief";
	public static final String CLASS_CANDIDATE_BELIEF = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "CandidateBelief";
	public static final String CLASS_PROMOTED_BELIEF = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "PromotedBelief";
	public static final String CLASS_TOKEN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Token";
	public static final String CLASS_TOKEN_GENERALIZATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "GeneralizationToken";
	public static final String CLASS_TOKEN_GEO = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "GeoToken";
	public static final String CLASS_TOKEN_RELATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RelationToken";
	
	public static final String CLASS_COMPONENT_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "ComponentExecution";
	public static final String CLASS_ALIAS_MATCHER_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "AliasMatcherExecution";
	public static final String CLASS_CMC_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "CMCExecution";
	public static final String CLASS_CPL_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "CPLExecution";
	public static final String CLASS_KB_MANIPULATION_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "KbManipulationExecution";
	public static final String CLASS_LE_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "LEExecution";
	public static final String CLASS_LATLONG_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "LatLongExecution";
	public static final String CLASS_MBL_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "MBLExecution";
	public static final String CLASS_OE_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "OEExecution";
	public static final String CLASS_ONTOLOGY_MODIFIER_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "OntologyModifierExecution";
	public static final String CLASS_PRA_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "PRAExecution";
	public static final String CLASS_RL_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RLExecution";
	public static final String CLASS_SEAL_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "SEALExecution";
	public static final String CLASS_SEMPARSE_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "SemparseExecution";
	public static final String CLASS_SPREADSHEETEDITS_EXECUTION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "SpreadsheetEditsExecution";
	
	public static final String CLASS_COMPONENT = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Component";
	public static final String RESOURCE_ALIAS_MATCHER = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "AliasMatcher";
	public static final String RESOURCE_CMC = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "CMC";
	public static final String RESOURCE_CPL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "CPL";
	public static final String RESOURCE_KB_MANIPULATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "KbManipulation";
	public static final String RESOURCE_LE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "LE";
	public static final String RESOURCE_LATLONG = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "LatLong";
	public static final String RESOURCE_MBL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "MBL";
	public static final String RESOURCE_OE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "OE";
	public static final String RESOURCE_ONTOLOGY_MODIFIER = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "OntologyModifier";
	public static final String RESOURCE_PRA = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "PRA";
	public static final String RESOURCE_RL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RL";
	public static final String RESOURCE_SEAL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "SEAL";
	public static final String RESOURCE_SEMPARSE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Semparse";
	public static final String RESOURCE_SPREADSHEETEDITS = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "SpreadsheetEdits";
	
	public static final String CLASS_MORPHOLOGICAL_PATTERN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "MorphologicalPatternScoreTriple";
	public static final String RESOURCE_MORPHOLOGICAL_PATTERN = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "MorphologicalPattern";
	public static final String CLASS_PATTERN_OCCURRENCE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "PatternNbOfOccurrencesPair";
	public static final String RESOURCE_PATTERN_OCCURRENCE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "PatternOccurrence";
	public static final String CLASS_NAMELATLONG_TRIPLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "NameLatLongTriple";
	public static final String RESOURCE_NAMELATLONG_TRIPLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "NameLatLong";
	public static final String CLASS_TEXT_URL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "TextUrlPair";
	public static final String RESOURCE_TEXT_URL = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "TextUrl";
	public static final String CLASS_RELATION_PATH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Path";
	public static final String RESOURCE_RELATION_PATH = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RelationPath";
	public static final String CLASS_RULE_SCORES_TUPLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RuleScoresTuple";
	public static final String RESOURCE_RULE_SCORES_TUPLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "RuleScores";
	public static final String CLASS_RULE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Rule";
	public static final String RESOURCE_RULE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Rule";
	public static final String CLASS_VARIABLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Variable";
	public static final String RESOURCE_VARIABLE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Variable";
	public static final String CLASS_PREDICATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Predicate";
	public static final String RESOURCE_PREDICATE = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "Predicate";
	
//	public static final String RESOURCE_BELIEF = "belief";
//	public static final String RESOURCE_TOKEN = "token";
	public static final String RESOURCE_TOKEN_RELATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "relationToken";
	public static final String RESOURCE_TOKEN_GENERALIZATION = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "generalizationToken";
	public static final String RESOURCE_TOKEN_GEO = NAMESPACE_PREFIX + NAMESPACE_END_METADATA + "geoToken";
		
	public static final String SUFFIX_PROMOTED = "Promoted";
	public static final String SUFFIX_CANDIDATE = "Candidate";
	
	public static final String NAMESPACE_NDFLUENTS = "http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#";
	public static final String CLASS_PROVENANCE_PART = NAMESPACE_NDFLUENTS + "ContextualPart";
	
	public static final String SINGLETON_PROPERTY_OF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#singletonPropertyOf";
	public static final String TYPE_SINGLETON_PROPERTY = "http://www.w3.org/1999/02/22-rdf-syntax-ns#SingletonProperty";
	
	public static final String ENGLISH_TAG = "en";

	public static final String createUri(final String name) {
		return name.replaceAll("\\s","").replaceAll(":", "_");
	}
	
	public static String createSequentialUri(final String name) {
        return name + "_" + numberSequences.compute(createUri(name), (K,V) -> V == null ? 1 : ++V);
    }
	
	public static String createAnchorUri(final String subject, final String predicate, final String object) {
		String subjectString = subject.split(":", 2)[1];
		String objectString = object.split(":", 2)[0].equals("concept") ? object.split(":", 2)[1] : String.valueOf(object.hashCode());
		return createUri(NAMESPACE_PREFIX + NAMESPACE_END_METADATA + subjectString	+ "_" + predicate + "_" + objectString);
	}
	
//	public static String createAnchorUri(final String name, final boolean candidate) {
//		return UriNell.createSequentialUri(PREFIX + NAMESPACE_END_METADATA + name + "_" + (candidate ? SUFFIX_CANDIDATE : SUFFIX_PROMOTED));
//	}
	
}
