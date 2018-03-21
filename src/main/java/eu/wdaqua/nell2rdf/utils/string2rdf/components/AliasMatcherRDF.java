package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class AliasMatcherRDF extends ComponentRDF {

	public AliasMatcherRDF(final AliasMatcher aliasMatcher, final Resource belief) {
		super(aliasMatcher, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addFreebaseDate();
	}

	void addFreebaseDate() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_FREEBASE_DATE));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(getFreeBaseDate(), XSDDatatype.XSDdate);
		this.componentExecution.addProperty(predicate, object);
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_ALIAS_MATCHER);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_ALIAS_MATCHER_EXECUTION);
	}

	String getFreeBaseDate() {
		return ((AliasMatcher) this.componentNell).getMetadata_FreebaseDate().toString();
	}

}
