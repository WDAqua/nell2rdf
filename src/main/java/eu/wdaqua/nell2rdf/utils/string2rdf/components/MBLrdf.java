package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.MBL;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class MBLrdf extends ComponentRDF {

	public MBLrdf(final MBL mbl, final Resource belief) {
		super(mbl, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addEntity();
		addRelation();
		addValue();
	}

	void addEntity() {
		Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROMOTED_ENTITY));
		RDFNode object = this.componentExecution.getModel().createTypedLiteral(getEntity(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);

		predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROMOTED_ENTITY_CATEGORY));
		object = this.componentExecution.getModel().createTypedLiteral(getEntityCategory(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addRelation() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROMOTED_RELATION));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getRelation(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);
	}

	void addValue() {
		Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROMOTED_VALUE));
		RDFNode object = this.componentExecution.getModel().createTypedLiteral(getValue(), XSDDatatype.XSDstring);
		this.componentExecution.addProperty(predicate, object);

		if (getValueCategory() != null) {
			predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PROMOTED_VALUE_CATEGORIE));
			object = this.componentExecution.getModel().createTypedLiteral(getValueCategory(), XSDDatatype.XSDstring);
			this.componentExecution.addProperty(predicate, object);
		}
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_MBL);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_MBL_EXECUTION);
	}

	String getEntity() {
		return ((MBL) this.componentNell).getMetadata_Entity();
	}

	String getEntityCategory() {
		return ((MBL) this.componentNell).getMetadata_EntityCategory();
	}

	String getRelation() {
		return ((MBL) this.componentNell).getMetadata_Relation();
	}

	String getValue() {
		return ((MBL) this.componentNell).getMetadata_Value();
	}

	String getValueCategory() {
		return ((MBL) this.componentNell).getMetadata_ValueCategory();
	}

}
