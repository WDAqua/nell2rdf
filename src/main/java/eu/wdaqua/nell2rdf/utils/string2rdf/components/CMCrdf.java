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

	public CMCrdf(final CMC cmc, final Resource belief) {
		super(cmc, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addMorphologicalPatterns();
	}

	void addMorphologicalPatterns() {
		getMorphologicalPatterns().forEach(pattern -> {
			Property predicate_λ = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_MORPHOLOGICAL_PATTERN));
			RDFNode object_λ = this.componentExecution.getModel().createResource(createSequentialName(getMetadataUri(RESOURCE_MORPHOLOGICAL_PATTERN + getCommonString())),
					this.componentExecution.getModel().getResource(getMetadataUri(CLASS_MORPHOLOGICAL_PATTERN)));
			this.componentExecution.addProperty(predicate_λ, object_λ);

			final Resource patternResource = object_λ.asResource();

			predicate_λ = patternResource.getModel().getProperty(getMetadataUri(PROPERTY_MORPHOLOGICAL_PATTERN_NAME));
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldName(), XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);

			predicate_λ = patternResource.getModel().getProperty(getMetadataUri(PROPERTY_MORPHOLOGICAL_PATTERN_VALUE));
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getFieldValue(), XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);

			predicate_λ = patternResource.getModel().getProperty(getMetadataUri(PROPERTY_MORPHOLOGICAL_PATTERN_SCORE));
			object_λ = patternResource.getModel().createTypedLiteral(pattern.getScore(), XSDDatatype.XSDdecimal);
			patternResource.addProperty(predicate_λ, object_λ);
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_CMC);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_CMC_EXECUTION);
	}

	List<CMCObjects> getMorphologicalPatterns() {
		return ((CMC) this.componentNell).getMetadata_CmcList();
	}

}
