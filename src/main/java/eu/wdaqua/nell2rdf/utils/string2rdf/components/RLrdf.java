package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.RuleInference;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class RLrdf extends ComponentRDF {
	
	public RLrdf(final RuleInference ruleInference) {
		super(ruleInference);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
		addRule();
	}
	
	void addRule() {
		Map<String, RDFNode> variables = new HashMap<>();
		
		Iterator<String> values = getValues().iterator();
		for (String variable : getVariables()) {
			Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_VARIABLE);
			RDFNode object = componentExecution.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_VARIABLE + variable), componentExecution.getModel().getResource(UriNell.CLASS_VARIABLE));
			componentExecution.addProperty(predicate, object);
			
			predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_VALUE_OF_VARIABLE);
			object = componentExecution.getModel().createTypedLiteral(values.next());
			componentExecution.addProperty(predicate, object);
			
			variables.put(variable, object);
		}
		
		getPredicates().forEach(predicate -> {
			Property predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_PREDICATE);
			RDFNode object_λ = componentExecution.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_PREDICATE), componentExecution.getModel().getResource(UriNell.CLASS_PREDICATE));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_PREDICATE_NAME);
			object_λ = componentExecution.getModel().createTypedLiteral(predicate[0]);
			componentExecution.addProperty(predicate_λ, object_λ);
			
			predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_FIRST_VARIABLE_OF_PREDICATE);
			object_λ = componentExecution.getModel().createTypedLiteral(predicate[1]);
			componentExecution.addProperty(predicate_λ, object_λ);
			
			if (predicate.length > 2 ) {
				predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_SECOND_VARIABLE_OF_PREDICATE);
				object_λ = componentExecution.getModel().createTypedLiteral(predicate[2]);
				componentExecution.addProperty(predicate_λ, object_λ);
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
