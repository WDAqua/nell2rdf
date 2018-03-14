package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.AliasMatcher;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class AliasMatcherRDF extends ComponentRDF {
	
	public AliasMatcherRDF(final AliasMatcher aliasMatcher, Resource belief) {
		super(aliasMatcher, belief);
	}

	public void addTriples () {
		super.addTriples();
		addFreebaseDate();
	}
	
	void addFreebaseDate() {
		Property predicate = componentExecution.getModel().getProperty(PROPERTY_FREEBASE_DATE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getFreeBaseDate(), XSDDatatype.XSDdate);
		componentExecution.addProperty(predicate, object);
	}
	
	String getComponentName() {
		return RESOURCE_ALIAS_MATCHER;
	}
	
	String getExecutionType() {
		return CLASS_ALIAS_MATCHER_EXECUTION;
	}
	
	String getFreeBaseDate() {
		return ((AliasMatcher) componentNell).getMetadata_FreebaseDate().toString();
	}

}
