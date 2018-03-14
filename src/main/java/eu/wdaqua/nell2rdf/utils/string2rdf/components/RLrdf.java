package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.log4j.Logger;

import eu.wdaqua.nell2rdf.extract.metadata.models.RuleInference;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class RLrdf extends ComponentRDF {
	
	private final Logger		log						= Logger.getLogger(this.getClass().getName());
	
	public RLrdf(final RuleInference ruleInference, Resource belief) {
		super(ruleInference, belief);
	}

	public void addTriples () {
		super.addTriples();
		addRule();
	}
	
	void addRule() {
		Map<String, RDFNode> variables = new HashMap<>();
		
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_RULE_SCORES);
		RDFNode object = componentExecution.getModel().createResource(RESOURCE_RULE_SCORES_TUPLE + getCommonString(), componentExecution.getModel().getResource(createUri(CLASS_RULE_SCORES_TUPLE)));
		componentExecution.addProperty(predicate, object);
		Resource ruleScoresTuple = object.asResource();
		
		predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_ACCURACY);
		object = ruleScoresTuple.getModel().createTypedLiteral(getAccuracy(), XSDDatatype.XSDdecimal);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_NUMBER_CORRECT);
		object = ruleScoresTuple.getModel().createTypedLiteral(getCorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_NUMBER_INCORRECT);
		object = ruleScoresTuple.getModel().createTypedLiteral(getIncorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_NUMBER_UNKNOWN);
		object = ruleScoresTuple.getModel().createTypedLiteral(getUnknown(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_RULE);
		object = ruleScoresTuple.getModel().createResource(RESOURCE_RULE + getCommonString(), componentExecution.getModel().getResource(createUri(CLASS_RULE)));
		ruleScoresTuple.addProperty(predicate, object);
		Resource rule = object.asResource();
		
		Iterator<String> values = getValues().iterator();
		for (String variable : getVariables()) {
			predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_VARIABLE);
			object = ruleScoresTuple.getModel().createResource(RESOURCE_VARIABLE + getCommonString() + "_" + variable, componentExecution.getModel().getResource(createUri(CLASS_VARIABLE)));
			ruleScoresTuple.addProperty(predicate, object);
			
			if (values.hasNext()) {
				predicate = ruleScoresTuple.getModel().getProperty(PROPERTY_VALUE_OF_VARIABLE);
				object = ruleScoresTuple.getModel().createTypedLiteral(values.next(),XSDDatatype.XSDstring);
				ruleScoresTuple.addProperty(predicate, object);
			} else {
				log.warn("No value for variable " + variable + " in " + getCommonString());
			}
			
			variables.put(variable, object);
		}
		
		getPredicates().forEach(relation -> {
			Property predicate_λ = rule.getModel().getProperty(PROPERTY_PREDICATE);
			RDFNode object_λ = rule.getModel().createResource(createSequentialUri(RESOURCE_PREDICATE + getCommonString()), componentExecution.getModel().getResource(createUri(CLASS_PREDICATE)));
			rule.addProperty(predicate_λ, object_λ);
			
			Resource logicalPredicate = object_λ.asResource();
			
			predicate_λ = rule.getModel().getProperty(PROPERTY_PREDICATE_NAME);
			object_λ = rule.getModel().createTypedLiteral(relation[0],XSDDatatype.XSDstring);
			logicalPredicate.addProperty(predicate_λ, object_λ);
			
			predicate_λ = rule.getModel().getProperty(PROPERTY_FIRST_VARIABLE_OF_PREDICATE);
			object_λ = rule.getModel().createResource(RESOURCE_VARIABLE + getCommonString() + "_" + relation[1], componentExecution.getModel().getResource(createUri(CLASS_VARIABLE)));
			logicalPredicate.addProperty(predicate_λ, object_λ);
			
			if (relation.length > 2 ) {
				predicate_λ = rule.getModel().getProperty(PROPERTY_SECOND_VARIABLE_OF_PREDICATE);
				object_λ = rule.getModel().createResource(RESOURCE_VARIABLE + getCommonString() + "_" + relation[2], componentExecution.getModel().getResource(createUri(CLASS_VARIABLE)));
				logicalPredicate.addProperty(predicate_λ, object_λ);
			}
		});
	}
	
	String getComponentName() {
		return RESOURCE_RL;
	}
	
	String getExecutionType() {
		return CLASS_RL_EXECUTION;
	}
	
	List<String[]> getPredicates() {
		return ((RuleInference) componentNell).getMetaData_lRule();
	}
	
	List<String> getVariables() {
		return ((RuleInference) componentNell).getMetaData_lRefRule();
	}
	
	List<String> getValues() {
		return ((RuleInference) componentNell).getMetaData_lValuesRule();
	}
	
	double getAccuracy() {
		return ((RuleInference) componentNell).getMetaData_dAccuracy();
	}
	
	int getCorrect() {
		return ((RuleInference) componentNell).getMetaData_iNrCorrectionEstimation();
	}
	
	int getIncorrect() {
		return ((RuleInference) componentNell).getMetaData_iNrIncorrectEstimation();
	}
	
	int getUnknown() {
		return ((RuleInference) componentNell).getMetaData_iNrTrainingAssertation();
	}

}
