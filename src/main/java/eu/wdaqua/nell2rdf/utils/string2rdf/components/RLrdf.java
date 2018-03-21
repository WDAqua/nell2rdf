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

	private final Logger log = Logger.getLogger(this.getClass().getName());

	public RLrdf(final RuleInference ruleInference, final Resource belief) {
		super(ruleInference, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addRule();
	}

	void addRule() {
		final Map<String, RDFNode> variables = new HashMap<>();

		Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_RULE_SCORES));
		RDFNode object = this.componentExecution.getModel().createResource(getMetadataUri(RESOURCE_RULE_SCORES_TUPLE + getCommonString()),
				this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_RULE_SCORES_TUPLE))));
		this.componentExecution.addProperty(predicate, object);
		final Resource ruleScoresTuple = object.asResource();

		predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_ACCURACY));
		object = ruleScoresTuple.getModel().createTypedLiteral(getAccuracy(), XSDDatatype.XSDdecimal);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_NUMBER_CORRECT));
		object = ruleScoresTuple.getModel().createTypedLiteral(getCorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_NUMBER_INCORRECT));
		object = ruleScoresTuple.getModel().createTypedLiteral(getIncorrect(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_NUMBER_UNKNOWN));
		object = ruleScoresTuple.getModel().createTypedLiteral(getUnknown(), XSDDatatype.XSDnonNegativeInteger);
		ruleScoresTuple.addProperty(predicate, object);

		predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_RULE));
		object = ruleScoresTuple.getModel().createResource(getMetadataUri(RESOURCE_RULE + getCommonString()), this.componentExecution.getModel().getResource(getMetadataUri(CLASS_RULE)));
		ruleScoresTuple.addProperty(predicate, object);
		final Resource rule = object.asResource();

		final Iterator<String> values = getValues().iterator();
		for (final String variable : getVariables()) {
			predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_VARIABLE));
			object = ruleScoresTuple.getModel().createResource(getMetadataUri(RESOURCE_VARIABLE + getCommonString() + "_" + variable),
					this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_VARIABLE))));
			ruleScoresTuple.addProperty(predicate, object);

			if (values.hasNext()) {
				predicate = ruleScoresTuple.getModel().getProperty(getMetadataUri(PROPERTY_VALUE_OF_VARIABLE));
				object = ruleScoresTuple.getModel().createTypedLiteral(values.next(), XSDDatatype.XSDstring);
				ruleScoresTuple.addProperty(predicate, object);
			} else {
				this.log.warn("No value for variable " + variable + " in " + getCommonString());
			}

			variables.put(variable, object);
		}

		getPredicates().forEach(relation -> {
			Property predicate_λ = rule.getModel().getProperty(getMetadataUri(PROPERTY_PREDICATE));
			RDFNode object_λ = rule.getModel().createResource(getMetadataUri(createSequentialName(RESOURCE_PREDICATE) + getCommonString()),
					this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_PREDICATE))));
			rule.addProperty(predicate_λ, object_λ);

			final Resource logicalPredicate = object_λ.asResource();

			predicate_λ = rule.getModel().getProperty(getMetadataUri(PROPERTY_PREDICATE_NAME));
			object_λ = rule.getModel().createTypedLiteral(relation[0], XSDDatatype.XSDstring);
			logicalPredicate.addProperty(predicate_λ, object_λ);

			predicate_λ = rule.getModel().getProperty(getMetadataUri(PROPERTY_FIRST_VARIABLE_OF_PREDICATE));
			object_λ = rule.getModel().createResource(getMetadataUri(RESOURCE_VARIABLE + getCommonString() + "_" + relation[1]),
					this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_VARIABLE))));
			logicalPredicate.addProperty(predicate_λ, object_λ);

			if (relation.length > 2) {
				predicate_λ = rule.getModel().getProperty(getMetadataUri(PROPERTY_SECOND_VARIABLE_OF_PREDICATE));
				object_λ = rule.getModel().createResource(getMetadataUri(RESOURCE_VARIABLE + getCommonString() + "_" + relation[2]),
						this.componentExecution.getModel().getResource(getMetadataUri(CLASS_VARIABLE)));
				logicalPredicate.addProperty(predicate_λ, object_λ);
			}
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_RL);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_RL_EXECUTION);
	}

	List<String[]> getPredicates() {
		return ((RuleInference) this.componentNell).getMetaData_lRule();
	}

	List<String> getVariables() {
		return ((RuleInference) this.componentNell).getMetaData_lRefRule();
	}

	List<String> getValues() {
		return ((RuleInference) this.componentNell).getMetaData_lValuesRule();
	}

	double getAccuracy() {
		return ((RuleInference) this.componentNell).getMetaData_dAccuracy();
	}

	int getCorrect() {
		return ((RuleInference) this.componentNell).getMetaData_iNrCorrectionEstimation();
	}

	int getIncorrect() {
		return ((RuleInference) this.componentNell).getMetaData_iNrIncorrectEstimation();
	}

	int getUnknown() {
		return ((RuleInference) this.componentNell).getMetaData_iNrTrainingAssertation();
	}

}
