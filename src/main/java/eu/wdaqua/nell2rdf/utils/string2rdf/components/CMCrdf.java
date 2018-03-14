package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.List;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.CMC;
import eu.wdaqua.nell2rdf.extract.metadata.models.CMC.CMCObjects;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class CMCrdf extends ComponentRDF {

	public CMCrdf(final CMC cmc, Resource belief) {
		super(cmc, belief);
	}

	public void addTriples () {
		super.addTriples();
		addMorphologicalPatterns();
	}
	
	void addMorphologicalPatterns() {
		getMorphologicalPatterns().forEach(pattern -> {
			Property predicate_λ = componentExecution.getModel().getProperty(PROPERTY_MORPHOLOGICAL_PATTERN);
			RDFNode object_λ = componentExecution.getModel().createResource(createSequentialUri(RESOURCE_MORPHOLOGICAL_PATTERN + getCommonString()),componentExecution.getModel().getResource(CLASS_MORPHOLOGICAL_PATTERN));
			componentExecution.addProperty(predicate_λ, object_λ);
			
			Resource patternResource = object_λ.asResource();
			
			predicate_λ = patternResource.getModel().getProperty(PROPERTY_MORPHOLOGICAL_PATTERN_NAME);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldName(),XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);
			
			predicate_λ = patternResource.getModel().getProperty(PROPERTY_MORPHOLOGICAL_PATTERN_VALUE);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldValue(),XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);
			
			predicate_λ = patternResource.getModel().getProperty(PROPERTY_MORPHOLOGICAL_PATTERN_SCORE);
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getScore(), XSDDatatype.XSDdecimal);
			patternResource.addProperty(predicate_λ, object_λ);
		});
	}
		

	String getComponentName() {
		return RESOURCE_CMC;
	}
	
	String getExecutionType() {
		return CLASS_CMC_EXECUTION;
	}
	
	List<CMCObjects> getMorphologicalPatterns() {
		return ((CMC) componentNell).getMetadata_CmcList();
	}
	
}
