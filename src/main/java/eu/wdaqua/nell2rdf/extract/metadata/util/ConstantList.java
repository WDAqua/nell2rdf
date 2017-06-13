/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.wdaqua.nell2rdf.extract.metadata.util;

/**
 *
 * @author Maisa
 */
public abstract class ConstantList {

    public final static String RELATION = "relation";
    public final static String CATEGORY = "category";
    public final static String LOOK_GENERALIZATIONS = "generalizations";

    public final static String ONTOLOGYMODIFIER = "OntologyModifier";
    public final static String CPL = "CPL";
    public final static String SEAL = "SEAL";
    public final static String OE = "OE";
    public final static String CMC = "CMC";
    public final static String ALIASMATCHER = "AliasMatcher";
    public final static String MBL = "MBL";
    public final static String PRA = "PRA";
    public final static String RULEINFERENCE = "RuleInference";
    public final static String KBMANIPULATION = "KbManipulation";
    public final static String SEMPARSE = "Semparse";
    public final static String LE = "LE";
    public final static String SPREADSHEETEDITS = "SpreadsheetEdits";
    public final static String LATLONG = "LatLong";
    public final static String LATLONGTT = "LatLongTT";

    public final static String TEXT_ONTOLOGYMODIFIER = "OntologyModifier-Iter:";
    public final static String TEXT_CPL = "CPL-Iter:";
    public final static String TEXT_SEAL = "SEAL-Iter:";
    public final static String TEXT_OE = "OE-Iter:";
    public final static String TEXT_CMC = "CMC-Iter:";
    public final static String TEXT_ALIASMATCHER = "AliasMatcher-Iter:";
    public final static String TEXT_MBL = "MBL-Iter:";
    public final static String TEXT_PRA = "PRA-Iter:";
    public final static String TEXT_RULEINFERENCE = "RuleInference-Iter:";
    public final static String TEXT_KBMANIPULATION = "KbManipulation-Iter:";
    public final static String TEXT_SEMPARSE = "Semparse-Iter:";
    public final static String TEXT_LE = "LE-Iter:";
    public final static String TEXT_SPREADSHEETEDITS = "SpreadsheetEdits-Iter:";
    public final static String TEXT_LATLONG = "LatLong-Iter:";
    public final static String TEXT_LATLONGTT = "LatLongTT-Iter:";

    public static final String  PREFIX_RESOURCE = "nell";
    public static final String  PREFIX_ONTOLOGY = "nell.onto";
    public static final String  PREFIX_PROVENANCE_RESOURCE = "nell.prov";
    public static final String  PREFIX_PROVENANCE_ONTOLOGY = "nell.prov.onto";
    public static final String  PREFIX_NDFLUENTS = "nd";

    public static final String NAMESPACE_END_RESOURCE = "/resource";
    public static final String NAMESPACE_END_ONTOLOGY = "/ontology";
    public static final String NAMESPACE_END_PROVENANCE_ONTOLOGY = "/provenance/ontology";
    public static final String NAMESPACE_END_PROVENANCE_RESOURCE = "/provenance/resource";

    public static final String PROPERTY_ASSOCIATED_WITH = "associatedWith";
    public static final String PROPERTY_GENERATED_BY = "generatedBy";
    public static final String PROPERTY_ITERATION = "iteration";
    public static final String PROPERTY_ITERATION_OF_PROMOTION = "iterationOfPromotion";
    public static final String PROPERTY_PROBABILITY = "probability";
    public static final String PROPERTY_PROBABILITY_OF_BELIEF = "probabilityOfBelief";
    public static final String PROPERTY_AT_TIME = "atTime";
    public static final String PROPERTY_SOURCE = "source";
    public static final String PROPERTY_TOKEN = "hasToken";
    public static final String PROPERTY_TOKE_ENTITY = "tokenEntity";
    public static final String PROPERTY_RELATION_VALUE = "relationValue";
    public static final String PROPERTY_GENERALIZATION_VALUE = "generalizationValue";
    public static final String PROPERTY_LATITUDE_VALUE = "latitudeValue";
    public static final String PROPERTY_LONGITUDE_VALUE = "longitudeValue";

    public static final String CLASS_BELIEF = "Belief";
    public static final String CLASS_CANDIDATE_BELIEF = "CandidateBelief";
    public static final String CLASS_PROMOTED_BELIEF = "PromotedBelief";
    public static final String CLASS_COMPONENT = "Component";
    public static final String CLASS_COMPONENT_ITERATION = "ComponentIteration";
    public static final String CLASS_TOKEN = "Token";
    public static final String CLASS_TOKEN_RELATION = "RelationToken";
    public static final String CLASS_TOKEN_GENERALIZATION = "GeneralizationToken";
    public static final String CLASS_TOKEN_GEO = "GeoToken";

    public static final String RESOURCE_BELIEF = "belief";
    public static final String RESOURCE_TOKEN = "token";
    public static final String RESOURCE_TOKEN_RELATION = "relationToken";
    public static final String RESOURCE_TOKEN_GENERALIZATION = "generalizationToken";
    public static final String RESOURCE_TOKEN_GEO = "geoToken";

    public static final String SUFFIX_PROMOTED = "Promoted";
    public static final String SUFFIX_CANDIDATE = "Candidate";

    public static final String NAMESPACE_NDFLUENTS = "http://www.emse.fr/~zimmermann/Ontologies/ndfluents.ttl#";
    public static final String CLASS_PROVENANCE_PART = "ContextualPart";
    public static final String PROPERTY_PROVENANCE_EXTENT = "provenanceExtent";
    public static final String PROPERTY_PROVENANCE_PART_OF = "provenancePartOf";
    public static final String PROPERTY_PROVENANCE = "provenanceProperty";
    public static final String PROPERTY_PROVENANCE_DATATYPE = "provenanceDatatypeProperty";
}
