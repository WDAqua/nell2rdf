package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.KbManipulation;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class KbManipulationRDF extends ComponentRDF {

	public KbManipulationRDF(final KbManipulation kbManipulation, Resource belief) {
		super(kbManipulation, belief);
	}

	public void addTriples (final Resource resource) {
		super.addTriples();
		addOldBug();
	}
	
	void addOldBug() {
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_OLD_BUG);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getOldBug());
		componentExecution.addProperty(predicate, object);
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_KB_MANIPULATION;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_KB_MANIPULATION_EXECUTION;
	}
	
	String getOldBug() {
		return ((KbManipulation) componentNell).getMetadata_oldBug();
	}
}
