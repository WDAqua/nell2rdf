package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.LE;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class LErdf extends ComponentRDF {

	public LErdf(final LE le, Resource belief) {
		super(le, belief);
	}

	public void addTriples () {
		super.addTriples();
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_LE;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_LE_EXECUTION;
	}

}
