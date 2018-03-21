package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.LE;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class LErdf extends ComponentRDF {

	public LErdf(final LE le, final Resource belief) {
		super(le, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_LE);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_LE_EXECUTION);
	}

}
