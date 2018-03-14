package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.SpreadsheetEdits;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class SpreadsheetEditsRDF extends ComponentRDF {
	
	public SpreadsheetEditsRDF(final SpreadsheetEdits spreadsheetEdits, Resource belief) {
		super(spreadsheetEdits, belief);
	}

	public void addTriples () {
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
			Property predicate = componentExecution.getModel().getProperty(PROPERTY_USER);
			RDFNode object = componentExecution.getModel().getResource(createUri(NAMESPACE_PREFIX + NAMESPACE_END_METADATA + getUser()));
			componentExecution.addProperty(predicate, object);
		}
	}
	
	void addEntity() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_ENTITY);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getEntity(),XSDDatatype.XSDstring);
		componentExecution.addProperty(predicate, object);
	}
	
	void addRelation() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_RELATION);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getRelation(),XSDDatatype.XSDstring);
		componentExecution.addProperty(predicate, object);
	}
	
	void addValue() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_VALUE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getValue(),XSDDatatype.XSDstring);
		componentExecution.addProperty(predicate, object);
	}
	
	void addAction() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_ACTION);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getAction(),XSDDatatype.XSDstring);
		componentExecution.addProperty(predicate, object);
	}
	
	void addFromIteration () {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_FILE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getFromIteration(),XSDDatatype.XSDstring);
		componentExecution.addProperty(predicate, object);
	}
	
	String getComponentName() {
		return RESOURCE_SPREADSHEETEDITS;
	}
	
	String getExecutionType() {
		return CLASS_SPREADSHEETEDITS_EXECUTION;
	}
	
	String getUser() {
        return ((SpreadsheetEdits) componentNell).getMetadata_UserFeedback();
    }

    String getEntity() {
        return ((SpreadsheetEdits) componentNell).getMetadata_Entity();
    }

    String getRelation() {
        return ((SpreadsheetEdits) componentNell).getMetadata_Relation();
    }

    String getValue() {
        return ((SpreadsheetEdits) componentNell).getMetadata_Value();
    }

    String getAction() {
        return ((SpreadsheetEdits) componentNell).getMetadata_Action();
    }

    String getFromIteration() {
        return ((SpreadsheetEdits) componentNell).getMetadata_From();
    }

}
