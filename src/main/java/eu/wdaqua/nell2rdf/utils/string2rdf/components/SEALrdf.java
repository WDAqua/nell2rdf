package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.SEAL;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class SEALrdf extends ComponentRDF {
	
	public SEALrdf(final SEAL seal) {
		super(seal);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
		addURLs();
	}
	
	void addURLs() {
		getURLs().forEach(url -> {
			Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_URL_SEAL);
			RDFNode object = componentExecution.getModel().createTypedLiteral(url, XSDDatatype.XSDanyURI);
			componentExecution.addProperty(predicate, object);
		});
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_SEAL;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_SEAL_EXECUTION;
	}
	
	List<String> getURLs() {
		return ((SEAL) componentNell).getMedatadata_URLList();
	}

}
