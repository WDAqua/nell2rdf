package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.RuleInference;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class RLrdf extends ComponentRDF {
	
	public RLrdf(final RuleInference ruleInference) {
		super(ruleInference);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_RL;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_RL_EXECUTION;
	}

}
