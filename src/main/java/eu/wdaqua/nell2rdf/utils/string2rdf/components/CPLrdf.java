package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import java.util.Map;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.CPL;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class CPLrdf extends ComponentRDF {

	public CPLrdf(final CPL cpl, final Resource belief) {
		super(cpl, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addPatternOccurrences();
	}

	void addPatternOccurrences() {
		getPatternOccurrences().forEach((K, V) -> {
			Property predicate_λ = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_PATTERN_OCCURRENCES));
			RDFNode object_λ = this.componentExecution.getModel().createResource(getMetadataUri(createSequentialName(RESOURCE_PATTERN_OCCURRENCE) + getCommonString()),
					this.componentExecution.getModel().getResource(getMetadataUri(CLASS_PATTERN_OCCURRENCE)));
			this.componentExecution.addProperty(predicate_λ, object_λ);

			final Resource patternResource = object_λ.asResource();

			predicate_λ = patternResource.getModel().getProperty(getMetadataUri(PROPERTY_TEXTUAL_PATTERN));
			object_λ = patternResource.getModel().createTypedLiteral(K, XSDDatatype.XSDstring);
			patternResource.addProperty(predicate_λ, object_λ);

			predicate_λ = patternResource.getModel().getProperty(getMetadataUri(PROPERTY_NUMBER_OF_OCCURRENCES));
			object_λ = patternResource.getModel().createTypedLiteral(V, XSDDatatype.XSDinteger);
			patternResource.addProperty(predicate_λ, object_λ);
		});
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_CPL);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_CPL_EXECUTION);
	}

	Map<String, Integer> getPatternOccurrences() {
		return ((CPL) this.componentNell).getMetadata_MapTPOccurence();
	}

}
