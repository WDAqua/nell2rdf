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
import eu.wdaqua.nell2rdf.utils.UriNell;

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
		
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_RULE_SCORES);
		RDFNode object = componentExecution.getModel().createResource(UriNell.RESOURCE_RULE_SCORES_TUPLE + getCommonString(), componentExecution.getModel().getResource(UriNell.CLASS_RULE_SCORES_TUPLE));
		componentExecution.addProperty(predicate, object);
		Resource ruleScoresTuple = object.asResource();
		
		predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_ACCURACY);
		object = ruleScoresTuple.getModel().createTypedLiteral(getAccuracy(), XSDDatatype.XSDdecimal);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_NUMBER_CORRECT);
		object = ruleScoresTuple.getModel().createTypedLiteral(getCorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_NUMBER_INCORRECT);
		object = ruleScoresTuple.getModel().createTypedLiteral(getIncorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_NUMBER_UNKNOWN);
		object = ruleScoresTuple.getModel().createTypedLiteral(getUnknown(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);
		
		predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_RULE);
		object = ruleScoresTuple.getModel().createResource(UriNell.RESOURCE_RULE + getCommonString(), componentExecution.getModel().getResource(UriNell.CLASS_RULE));
		ruleScoresTuple.addProperty(predicate, object);
		Resource rule = object.asResource();
		
		Iterator<String> values = getValues().iterator();
		for (String variable : getVariables()) {
			predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_VARIABLE);
			object = ruleScoresTuple.getModel().createResource(UriNell.RESOURCE_VARIABLE + getCommonString() + "_" + variable, componentExecution.getModel().getResource(UriNell.CLASS_VARIABLE));
			ruleScoresTuple.addProperty(predicate, object);
			
			if (values.hasNext()) {
				predicate = ruleScoresTuple.getModel().getProperty(UriNell.PROPERTY_VALUE_OF_VARIABLE);
				object = ruleScoresTuple.getModel().createTypedLiteral(values.next(),XSDDatatype.XSDstring);
				ruleScoresTuple.addProperty(predicate, object);
			} else {
				log.warn("No value for variable " + variable + " in " + getCommonString());
			}
			
			variables.put(variable, object);
		}
		
		getPredicates().forEach(relation -> {
			Property predicate_λ = rule.getModel().getProperty(UriNell.PROPERTY_PREDICATE);
			RDFNode object_λ = rule.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_PREDICATE + getCommonString()), componentExecution.getModel().getResource(UriNell.CLASS_PREDICATE));
			rule.addProperty(predicate_λ, object_λ);
			
			Resource logicalPredicate = object_λ.asResource();
			
			predicate_λ = rule.getModel().getProperty(UriNell.PROPERTY_PREDICATE_NAME);
			object_λ = rule.getModel().createTypedLiteral(relation[0],XSDDatatype.XSDstring);
			logicalPredicate.addProperty(predicate_λ, object_λ);
			
			predicate_λ = rule.getModel().getProperty(UriNell.PROPERTY_FIRST_VARIABLE_OF_PREDICATE);
			object_λ = rule.getModel().createResource(UriNell.RESOURCE_VARIABLE + getCommonString() + "_" + relation[1], componentExecution.getModel().getResource(UriNell.CLASS_VARIABLE));
			logicalPredicate.addProperty(predicate_λ, object_λ);
			
			if (relation.length > 2 ) {
				predicate_λ = rule.getModel().getProperty(UriNell.PROPERTY_SECOND_VARIABLE_OF_PREDICATE);
				object_λ = rule.getModel().createResource(UriNell.RESOURCE_VARIABLE + getCommonString() + "_" + relation[2], componentExecution.getModel().getResource(UriNell.CLASS_VARIABLE));
				logicalPredicate.addProperty(predicate_λ, object_λ);
			}
		});
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_RL;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_RL_EXECUTION;
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
