package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.SpreadsheetEdits;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class SpreadsheetEditsRDF extends ComponentRDF {

	public SpreadsheetEditsRDF(final SpreadsheetEdits spreadsheetEdits, final Resource belief) {
		super(spreadsheetEdits, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addUser();
		addEntity();
		addRelation();
		addValue();
		addAction();
		addFromIteration();
	}

	void addUser() {
		if (getUser() != null) {
			final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_USER));
			final RDFNode object = this.componentExecution.getModel().getResource(getMetadataUri(getUser()));
			this.componentExecution.addProperty(predicate, object);
		}
	}

	void addEntity() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_ENTITY));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getEntity(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addRelation() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_RELATION));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getRelation(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addValue() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_VALUE));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getValue(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addAction() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_ACTION));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getAction(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addFromIteration() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_FILE));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getFromIteration(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_SPREADSHEETEDITS);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_SPREADSHEETEDITS_EXECUTION);
	}

	String getUser() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_UserFeedback();
	}

	String getEntity() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_Entity();
	}

	String getRelation() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_Relation();
	}

	String getValue() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_Value();
	}

	String getAction() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_Action();
	}

	String getFromIteration() {
		return ((SpreadsheetEdits) this.componentNell).getMetadata_From();
	}

}
