package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import eu.wdaqua.nell2rdf.extract.metadata.models.Semparse;
import static eu.wdaqua.nell2rdf.utils.UriNell.*;

public class SemparseRDF extends ComponentRDF {

	public SemparseRDF(final Semparse semparse, final Resource belief) {
		super(semparse, belief);
	}

	@Override
	public void addTriples() {
		super.addTriples();
		addSentence();
	}

	void addSentence() {
		final Property predicate = this.componentExecution.getModel().getProperty(getMetadataUri(PROPERTY_SENTENCE));
		final RDFNode object = this.componentExecution.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(getSentence(), ENGLISH_TAG));
		this.componentExecution.addProperty(predicate, object);
	}

	@Override
	String getComponentName() {
		return getMetadataUri(RESOURCE_SEMPARSE);
	}

	@Override
	String getExecutionType() {
		return getMetadataUri(CLASS_SEMPARSE_EXECUTION);
	}

	String getSentence() {
		return ((Semparse) this.componentNell).getMetadata_SentenceList();
	}
}
