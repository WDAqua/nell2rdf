package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.SEAL;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class SEALrdf extends ComponentRDF {

	public SEALrdf(final SEAL seal, final Resource belief) {
		super(seal, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addURLs();
	}

	void addURLs() {
		getURLs().forEach(url -> {
			final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_URL));
			final RDFNode object = this.componentExecution.getModel().createTypedLiteral(url, XSDDatatype.XSDanyURI);
			this.componentExecution.addProperty(predicate, object);
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_SEAL);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_SEAL_EXECUTION);
	}

	List<String> getURLs() {
		return ((SEAL) this.componentNell).getMedatadata_URLList();
	}

}
