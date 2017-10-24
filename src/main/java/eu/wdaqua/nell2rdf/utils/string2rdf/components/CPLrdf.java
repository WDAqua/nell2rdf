package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.CPL;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class CPLrdf extends ComponentRDF {
	
	public CPLrdf(final CPL cpl, Resource belief) {
		super(cpl, belief);
	}

	public void addTriples () {
		super.addTriples();
		addPatternOccurrences();
	}
	
	void addPatternOccurrences() {
		getPatternOccurrences().forEach((K,V) -> {
			Property predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_PATTERN_OCCURRENCES);
			RDFNode object_λ = componentExecution.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_PATTERN_OCCURRENCE + getCommonString()), componentExecution.getModel().getResource(UriNell.CLASS_PATTERN_OCCURRENCE));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			Resource patternResource = object_λ.asResource();
			
			predicate_λ = patternResource.getModel().getProperty(UriNell.PROPERTY_TEXTUAL_PATTERN);
			object_λ = patternResource.getModel().createTypedLiteral(K,XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);
			
			predicate_λ = patternResource.getModel().getProperty(UriNell.PROPERTY_NUMBER_OF_OCCURRENCES);
			object_λ = patternResource.getModel().createTypedLiteral(V,XSDDatatype.XSDinteger);
			patternResource.addProperty(predicate_λ, object_λ);
		});
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_CPL;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_CPL_EXECUTION;
	}
	
	Map<String,Integer> getPatternOccurrences() {
		return ((CPL) componentNell).getMetadata_MapTPOccurence();
	}
	
}
