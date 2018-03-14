package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.OntologyModifier;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class OntologyModifierRDF extends ComponentRDF {

	public OntologyModifierRDF(final OntologyModifier ontologyModifier, Resource belief) {
		super(ontologyModifier, belief);
	}

	public void addTriples () {
		super.addTriples();
		addOntologyModification();
	}
	
	void addOntologyModification() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_ONTOLOGY_MODIFICATION);
		RDFNode object = componentExecution.getModel().getResource(createUri(NAMESPACE_PREFIX + NAMESPACE_END_METADATA + getOntologyModification()));
		componentExecution.addProperty(predicate, object);
	}
	
	String getComponentName() {
		return RESOURCE_ONTOLOGY_MODIFIER;
	}
	
	String getExecutionType() {
		return CLASS_ONTOLOGY_MODIFIER_EXECUTION;
	}
	
	String getOntologyModification() {
		String string = ((OntologyModifier) componentNell).getMetadata_From();
		if (string.length() > 0) {
			string = string.substring(1);
		}
		return string;
	}

}
