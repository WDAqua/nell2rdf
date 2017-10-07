package eu.wdaqua.nell2rdf.utils;

import java.util.HashMap;
import java.util.Map;

public class UriNell {

	private static Map<String,Integer> numberSequences = new HashMap<>();
	
	public static final String PREFIX = "https://w3id.org/nellrdf/";
	public static final String NAMESPACE_END_RESOURCE = "resource/";
	public static final String NAMESPACE_END_ONTOLOGY = "ontology/";
	public static final String NAMESPACE_END_METADATA = "metadata/";
	
	public static final String PREFIX_METADATA = "nellmeta";
	public static final String PREFIX_NDFLUENTS = "nd";
	public static final String PREFIX_ONTOLOGY = "nellonto";
	public static final String PREFIX_RESOURCE = "nell";
	
	public static final String PROPERTY_ASSOCIATED_WITH = PREFIX + NAMESPACE_END_METADATA + "associatedWith";
	public static final String PROPERTY_AT_TIME = PREFIX + NAMESPACE_END_METADATA + "atTime";
	public static final String PROPERTY_GENERALIZATION_VALUE = PREFIX + NAMESPACE_END_METADATA + "generalizationValue";
	public static final String PROPERTY_GENERATED_BY = PREFIX + NAMESPACE_END_METADATA + "generatedBy";
	public static final String PROPERTY_ITERATION = PREFIX + NAMESPACE_END_METADATA + "iteration";
	public static final String PROPERTY_ITERATION_OF_PROMOTION = PREFIX + NAMESPACE_END_METADATA + "iterationOfPromotion";
	public static final String PROPERTY_LATITUDE_VALUE = PREFIX + NAMESPACE_END_METADATA + "latitudeValue";
	public static final String PROPERTY_LONGITUDE_VALUE = PREFIX + NAMESPACE_END_METADATA + "longitudeValue";
	public static final String PROPERTY_PROBABILITY = PREFIX + NAMESPACE_END_METADATA + "probability";
	public static final String PROPERTY_PROBABILITY_OF_BELIEF = PREFIX + NAMESPACE_END_METADATA + "probabilityOfBelief";
	public static final String PROPERTY_PROVENANCE = "provenanceProperty";
	public static final String PROPERTY_PROVENANCE_DATATYPE = "provenanceDatatypeProperty";
	public static final String PROPERTY_PROVENANCE_EXTENT = "provenanceExtent";
	public static final String PROPERTY_PROVENANCE_PART_OF = "provenancePartOf";
	public static final String PROPERTY_RELATION_VALUE = PREFIX + NAMESPACE_END_METADATA + "relationValue";
	public static final String PROPERTY_SOURCE = PREFIX + NAMESPACE_END_METADATA + "source";
	public static final String PROPERTY_TOKE_ENTITY = PREFIX + NAMESPACE_END_METADATA + "tokenEntity";
	public static final String PROPERTY_TOKEN = PREFIX + NAMESPACE_END_METADATA + "hasToken";
	
	public static final String PROPERTY_FREEBASE_DATE = PREFIX + NAMESPACE_END_METADATA + "freebaseData";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN = PREFIX + NAMESPACE_END_METADATA + "morphologicalPattern";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_NAME = PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternName";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_VALUE = PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternValue";
	public static final String PROPERTY_MORPHOLOGICAL_PATTERN_SCORE = PREFIX + NAMESPACE_END_METADATA + "morphologicalPatternScore";
	public static final String PROPERTY_PATTERN_OCCURRENCES = PREFIX + NAMESPACE_END_METADATA + "patternOccurrences";
	public static final String PROPERTY_TEXTUAL_PATTERN = PREFIX + NAMESPACE_END_METADATA + "textualPattern";
	public static final String PROPERTY_NUMBER_OF_OCCURRENCES = PREFIX + NAMESPACE_END_METADATA + "nbOfOccurrences";
	public static final String PROPERTY_OLD_BUG = PREFIX + NAMESPACE_END_METADATA + "oldBug";
	public static final String PROPERTY_LOCATION = PREFIX + NAMESPACE_END_METADATA + "location";
	public static final String PROPERTY_PLACE_NAME = PREFIX + NAMESPACE_END_METADATA + "name";
	public static final String PROPERTY_TEXT_URL = PREFIX + NAMESPACE_END_METADATA + "textUrl";
	public static final String PROPERTY_TEXT = PREFIX + NAMESPACE_END_METADATA + "text";
	public static final String PROPERTY_URL_OE = PREFIX + NAMESPACE_END_METADATA + "urlOE";
	public static final String PROPERTY_ONTOLOGY_MODIFICATION = PREFIX + NAMESPACE_END_METADATA + "ontologyModification";
	public static final String PROPERTY_RELATION_PATH = PREFIX + NAMESPACE_END_METADATA + "relationPath";
	public static final String PROPERTY_DIRECTION_OF_PATH = PREFIX + NAMESPACE_END_METADATA + "direction";
	public static final String PROPERTY_SCORE_OF_PATH = PREFIX + NAMESPACE_END_METADATA + "score";
	public static final String PROPERTY_LIST_OF_RELATIONS = PREFIX + NAMESPACE_END_METADATA + "listOfRelations";
	public static final String PROPERTY_URL_SEAL = PREFIX + NAMESPACE_END_METADATA + "urlSeal";
	public static final String PROPERTY_SENTENCE = PREFIX + NAMESPACE_END_METADATA + "sentence";
	public static final String PROPERTY_USER = PREFIX + NAMESPACE_END_METADATA + "user";
	public static final String PROPERTY_ENTITY = PREFIX + NAMESPACE_END_METADATA + "entity";
	public static final String PROPERTY_RELATION = PREFIX + NAMESPACE_END_METADATA + "relation";
	public static final String PROPERTY_VALUE = PREFIX + NAMESPACE_END_METADATA + "value";
	public static final String PROPERTY_FROM_ITERATION = PREFIX + NAMESPACE_END_METADATA + "fromIteration";
	
	public static final String CLASS_BELIEF = PREFIX + NAMESPACE_END_METADATA + "Belief";
	public static final String CLASS_CANDIDATE_BELIEF = PREFIX + NAMESPACE_END_METADATA + "CandidateBelief";
	public static final String CLASS_PROMOTED_BELIEF = PREFIX + NAMESPACE_END_METADATA + "PromotedBelief";
	public static final String CLASS_TOKEN = PREFIX + NAMESPACE_END_METADATA + "Token";
	public static final String CLASS_TOKEN_GENERALIZATION = PREFIX + NAMESPACE_END_METADATA + "GeneralizationToken";
	public static final String CLASS_TOKEN_GEO = PREFIX + NAMESPACE_END_METADATA + "GeoToken";
	public static final String CLASS_TOKEN_RELATION = PREFIX + NAMESPACE_END_METADATA + "RelationToken";
	
	public static final String CLASS_COMPONENT_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "ComponentExecution";
	public static final String CLASS_ALIAS_MATCHER_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "AliasMatcherExecution";
	public static final String CLASS_CMC_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "CMCExecution";
	public static final String CLASS_CPL_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "CPLExecution";
	public static final String CLASS_KB_MANIPULATION_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "KbManipulationExecution";
	public static final String CLASS_LE_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "LEExecution";
	public static final String CLASS_LATLONG_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "LatLongExecution";
	public static final String CLASS_OE_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "OEExecution";
	public static final String CLASS_ONTOLOGY_MODIFIER_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "OntologyModifierExecution";
	public static final String CLASS_PRA_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "PRAExecution";
	public static final String CLASS_RL_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "RLExecution";
	public static final String CLASS_SEAL_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "SEALExecution";
	public static final String CLASS_SEMPARSE_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "SemparseExecution";
	public static final String CLASS_SPREADSHEETEDITS_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "SpreadsheetEditsExecution";
	
	public static final String CLASS_COMPONENT = PREFIX + NAMESPACE_END_METADATA + "Component";
	public static final String RESOURCE_ALIAS_MATCHER = PREFIX + NAMESPACE_END_METADATA + "AliasMatcher";
	public static final String RESOURCE_CMC = PREFIX + NAMESPACE_END_METADATA + "CMC";
	public static final String RESOURCE_CPL = PREFIX + NAMESPACE_END_METADATA + "CPL";
	public static final String RESOURCE_KB_MANIPULATION = PREFIX + NAMESPACE_END_METADATA + "KbManipulation";
	public static final String RESOURCE_LE = PREFIX + NAMESPACE_END_METADATA + "LE";
	public static final String RESOURCE_LATLONG = PREFIX + NAMESPACE_END_METADATA + "LatLong";
	public static final String RESOURCE_OE = PREFIX + NAMESPACE_END_METADATA + "OE";
	public static final String RESOURCE_ONTOLOGY_MODIFIER = PREFIX + NAMESPACE_END_METADATA + "OntologyModifier";
	public static final String RESOURCE_PRA = PREFIX + NAMESPACE_END_METADATA + "PRA";
	public static final String RESOURCE_RL = PREFIX + NAMESPACE_END_METADATA + "RL";
	public static final String RESOURCE_SEAL = PREFIX + NAMESPACE_END_METADATA + "SEAL";
	public static final String RESOURCE_SEMPARSE = PREFIX + NAMESPACE_END_METADATA + "Semparse";
	public static final String RESOURCE_SPREADSHEETEDITS = PREFIX + NAMESPACE_END_METADATA + "SpreadsheetEdits";
	
	public static final String CLASS_MORPHOLOGICAL_PATTERN = PREFIX + NAMESPACE_END_METADATA + "MophologicalPatternScoreTriple";
	public static final String RESOURCE_MORPHOLOGICAL_PATTERN = PREFIX + NAMESPACE_END_METADATA + "MorphologicalPattern";
	public static final String CLASS_PATTERN_OCCURRENCE = PREFIX + NAMESPACE_END_METADATA + "PatternNbOfOccurrencesPair";
	public static final String RESOURCE_PATTERN_OCCURRENCE = PREFIX + NAMESPACE_END_METADATA + "PatternOccurrence";
	public static final String CLASS_NAMELATLONG_TRIPLE = PREFIX + NAMESPACE_END_METADATA + "NameLatLongTriple";
	public static final String RESOURCE_NAMELATLONG_TRIPLE = PREFIX + NAMESPACE_END_METADATA + "NameLatLong";
	public static final String CLASS_TEXT_URL = PREFIX + NAMESPACE_END_METADATA + "TextUrlPair";
	public static final String RESOURCE_TEXT_URL = PREFIX + NAMESPACE_END_METADATA + "TextUrl";
	public static final String CLASS_RELATION_PATH = PREFIX + NAMESPACE_END_METADATA + "Path";
	public static final String RESOURCE_RELATION_PATH = PREFIX + NAMESPACE_END_METADATA + "RelationPath";
	
	public static final String RESOURCE_BELIEF = "belief";
	public static final String RESOURCE_TOKEN = "token";
	public static final String RESOURCE_TOKEN_RELATION = "relationToken";
	public static final String RESOURCE_TOKEN_GENERALIZATION = "generalizationToken";
	public static final String RESOURCE_TOKEN_GEO = "geoToken";
		
	public static final String SUFFIX_PROMOTED = "Promoted";
	public static final String SUFFIX_CANDIDATE = "Candidate";
	
	public static final String NAMESPACE_NDFLUENTS = "http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#";
	public static final String CLASS_PROVENANCE_PART = NAMESPACE_NDFLUENTS + "ContextualPart";
	
	public static final String SINGLETON_PROPERTY_OF = "http://www.w3.org/2000/01/rdf-schema#rdf:singletonPropertyOf";

	public static String createSequentialUri(final String name) {
        return name + "_" + numberSequences.compute(name, (K,V) -> V == null ? 1 : ++V);
    }
	
	public static String createAnchorUri(final String name, final boolean candidate) {
		return UriNell.createSequentialUri(PREFIX + NAMESPACE_END_METADATA + name + "_" + (candidate ? SUFFIX_CANDIDATE : SUFFIX_PROMOTED));
	}
	
}
