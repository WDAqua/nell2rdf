package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.KbManipulation;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class KbManipulationRDF extends ComponentRDF {

	public KbManipulationRDF(final KbManipulation kbManipulation, final Resource belief) {
		super(kbManipulation, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addOldBug();
	}

	void addOldBug() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_OLD_BUG));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getOldBug(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_KB_MANIPULATION);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_KB_MANIPULATION_EXECUTION);
	}

	String getOldBug() {
		return ((KbManipulation) this.componentNell).getMetadata_oldBug();
	}
}
