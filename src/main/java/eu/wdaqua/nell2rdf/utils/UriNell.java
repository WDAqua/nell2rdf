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
	
	public static final String CLASS_BELIEF = PREFIX + NAMESPACE_END_METADATA + "Belief";
	public static final String CLASS_CANDIDATE_BELIEF = PREFIX + NAMESPACE_END_METADATA + "CandidateBelief";
	public static final String CLASS_PROMOTED_BELIEF = PREFIX + NAMESPACE_END_METADATA + "PromotedBelief";
	public static final String CLASS_TOKEN = PREFIX + NAMESPACE_END_METADATA + "Token";
	public static final String CLASS_TOKEN_GENERALIZATION = PREFIX + NAMESPACE_END_METADATA + "GeneralizationToken";
	public static final String CLASS_TOKEN_GEO = PREFIX + NAMESPACE_END_METADATA + "GeoToken";
	public static final String CLASS_TOKEN_RELATION = PREFIX + NAMESPACE_END_METADATA + "RelationToken";
	
	public static final String CLASS_COMPONENT_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "ComponentExecution";
	public static final String CLASS_ALIAS_MATCHER_EXECUTION = PREFIX + NAMESPACE_END_METADATA + "AliasMatcherExecution";

	public static final String CLASS_COMPONENT = PREFIX + NAMESPACE_END_METADATA + "Component";
	public static final String RESOURCE_ALIAS_MATCHER = PREFIX + NAMESPACE_END_METADATA + "AliasMatcher";
	
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
