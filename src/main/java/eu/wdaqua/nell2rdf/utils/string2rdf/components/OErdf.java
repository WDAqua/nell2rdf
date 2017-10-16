package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.net.URL;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.OE;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class OErdf extends ComponentRDF {
	
	public OErdf(final OE oe, Resource belief) {
		super(oe, belief);
	}

	public void addTriples (final Resource resource) {
		super.addTriples();
		addTextUrlPairs();
	}
	
	void addTextUrlPairs() {
		getTextUrlPairs().forEach((K,V) -> {
			Property predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_TEXT_URL);
			RDFNode object_λ = componentExecution.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_TEXT_URL + getCommonString()),componentExecution.getModel().getResource(UriNell.CLASS_TEXT_URL));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			Resource resource = object_λ.asResource();
			
			predicate_λ = resource.getModel().getProperty(UriNell.PROPERTY_TEXT);
			object_λ = resource.getModel().createTypedLiteral(K);
			resource.addProperty(predicate_λ, object_λ);
			
			if (V != null) {
				predicate_λ = resource.getModel().getProperty(UriNell.PROPERTY_URL_OE);
				object_λ = resource.getModel().createTypedLiteral(V.toString(),XSDDatatype.XSDanyURI);
				resource.addProperty(predicate_λ, object_λ);
			}
		});
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_OE;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_OE_EXECUTION;
	}
	
	Map<String, URL>getTextUrlPairs() {
		return ((OE) componentNell).getMetadata_mapTextURL();
	}

}
