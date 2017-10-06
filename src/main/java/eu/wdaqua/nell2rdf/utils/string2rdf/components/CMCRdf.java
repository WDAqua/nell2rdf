package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.CMC;
import eu.wdaqua.nell2rdf.extract.metadata.models.CMC.CMCObjects;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class CMCRdf extends ComponentRDF {

	public CMCRdf(final CMC cmc) {
		super(cmc);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
		addMorphologicalPatterns();
	}
	
	void addMorphologicalPatterns() {
		getMorphologicalPatterns().forEach(pattern -> {
			Property predicate_λ = componentExecution.getModel().getProperty(UriNell.PROPERTY_MORPHOLOGICAL_PATTERN);
			RDFNode object_λ = componentExecution.getModel().createResource(UriNell.createSequentialUri(UriNell.RESOURCE_MORPHOLOGICAL_PATTERN));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			Resource patternResource = object_λ.asResource();
			
			predicate_λ = patternResource.getModel().getProperty(UriNell.PROPERTY_MORPHOLOGICAL_PATTERN_NAME);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldName());
			patternResource.addProperty(predicate_λ, object_λ);
			
			predicate_λ = patternResource.getModel().getProperty(UriNell.PROPERTY_MORPHOLOGICAL_PATTERN_VALUE);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldValue());
			patternResource.addProperty(predicate_λ, object_λ);
			
			predicate_λ = patternResource.getModel().getProperty(UriNell.PROPERTY_MORPHOLOGICAL_PATTERN_SCORE);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getScore(), XSDDatatype.XSDdecimal);
			patternResource.addProperty(predicate_λ, object_λ);
		});
	}
		

	String getComponentName() {
		return UriNell.RESOURCE_CMC;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_CMC_EXECUTION;
	}
	
	List<CMCObjects> getMorphologicalPatterns() {
		return ((CMC) componentNell).getMetadata_CmcList();
	}
	
}
