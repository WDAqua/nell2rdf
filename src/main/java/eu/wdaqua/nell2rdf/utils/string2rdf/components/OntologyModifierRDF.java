package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.OntologyModifier;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class OntologyModifierRDF extends ComponentRDF {

	public OntologyModifierRDF(final OntologyModifier ontologyModifier, final Resource belief) {
		super(ontologyModifier, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addOntologyModification();
	}

	void addOntologyModification() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_ONTOLOGY_MODIFICATION));
		final RDFNode object = this.componentExecution.getModel().getResource(getMetadataUri(getOntologyModification()));
		this.componentExecution.addProperty(predicate, object);
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_ONTOLOGY_MODIFIER);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_ONTOLOGY_MODIFIER_EXECUTION);
	}

	String getOntologyModification() {
		String string = ((OntologyModifier) this.componentNell).getMetadata_From();
		if (string.length() > 0) {
			string = string.substring(1);
		}
		return string;
	}

}
