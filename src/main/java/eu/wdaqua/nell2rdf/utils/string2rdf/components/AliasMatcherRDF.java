package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class AliasMatcherRDF extends ComponentRDF {
	
	public AliasMatcherRDF(final AliasMatcher aliasMatcher) {
		super(aliasMatcher);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
		addFreebaseDate();
	}
	
	void addFreebaseDate() {
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_FREEBASE_DATE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getFreeBaseDate());
		componentExecution.addProperty(predicate, object);
	}
	
	String getExecutionType() {
		return UriNell.CLASS_ALIAS_MATCHER_EXECUTION;
	}
	
	String getComponentType() {
		return UriNell.RESOURCE_ALIAS_MATCHER;
	}
	
	String getFreeBaseDate() {
		return ((AliasMatcher) componentNell).getMetadata_FreebaseDate().toString();
	}

}