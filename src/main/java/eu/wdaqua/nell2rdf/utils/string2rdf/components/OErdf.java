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

	public OErdf(final OE oe, final Resource belief) {
		super(oe, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addTextUrlPairs();
	}

	void addTextUrlPairs() {
		getTextUrlPairs().forEach((K, V) -> {
			Property predicate_λ = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_TEXT_URL));
			RDFNode object_λ = this.componentExecution.getModel().createResource(getMetadataUri(createSequentialName(RESOURCE_TEXT_URL) + getCommonString()),
					this.componentExecution.getModel().getResource(getMetadataUri(getMetadataUri(CLASS_TEXT_URL))));
			this.componentExecution.addProperty(predicate_λ, object_λ);

			final Resource resource = object_λ.asResource();

			predicate_λ = resource.getModel().getProperty(getMetadataUri(PROPERTY_TEXT));
			object_λ = resource.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(K, getMetadataUri(ENGLISH_TAG)));
			resource.addProperty(predicate_λ, object_λ);

			if (V != null) {
				predicate_λ = resource.getModel().getProperty(getMetadataUri(PROPERTY_URL));
				object_λ = resource.getModel().createTypedLiteral(V.toString(), XSDDatatype.XSDanyURI);
				resource.addProperty(predicate_λ, object_λ);
			}
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_OE);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_OE_EXECUTION);
	}

	Map<String, URL> getTextUrlPairs() {
		return ((OE) this.componentNell).getMetadata_mapTextURL();
	}

}
