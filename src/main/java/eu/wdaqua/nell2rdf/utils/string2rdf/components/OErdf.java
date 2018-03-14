package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.net.URL;
import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import eu.wdaqua.nell2rdf.extract.metadata.models.OE;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class OErdf extends ComponentRDF {
	
	public OErdf(final OE oe, Resource belief) {
		super(oe, belief);
	}

	public void addTriples () {
		super.addTriples();
		addTextUrlPairs();
	}
	
	void addTextUrlPairs() {
		getTextUrlPairs().forEach((K,V) -> {
			Property predicate_λ = componentExecution.getModel().getProperty(PROPERTY_TEXT_URL);
			RDFNode object_λ = componentExecution.getModel().createResource(createSequentialUri(RESOURCE_TEXT_URL + getCommonString()),componentExecution.getModel().getResource(createUri(CLASS_TEXT_URL)));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			Resource resource = object_λ.asResource();
			
			predicate_λ = resource.getModel().getProperty(PROPERTY_TEXT);
			object_λ = resource.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(K, ENGLISH_TAG));
			resource.addProperty(predicate_λ, object_λ);
			
			if (V != null) {
				predicate_λ = resource.getModel().getProperty(PROPERTY_URL);
				object_λ = resource.getModel().createTypedLiteral(V.toString(),XSDDatatype.XSDanyURI);
				resource.addProperty(predicate_λ, object_λ);
			}
		});
	}
	
	String getComponentName() {
		return RESOURCE_OE;
	}
	
	String getExecutionType() {
		return CLASS_OE_EXECUTION;
	}
	
	Map<String, URL>getTextUrlPairs() {
		return ((OE) componentNell).getMetadata_mapTextURL();
	}

}
