package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import eu.wdaqua.nell2rdf.extract.metadata.models.Semparse;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class SemparseRDF extends ComponentRDF {
	
	public SemparseRDF(final Semparse semparse) {
		super(semparse);
	}

	public void addTriples (final Resource resource) {
		super.addTriples(resource);
		addSentence();
	}
	
	void addSentence() {
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_SENTENCE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(getSentence());
		componentExecution.addProperty(predicate, object);
	}
	
	String getComponentName() {
		return UriNell.RESOURCE_SEMPARSE;
	}
	
	String getExecutionType() {
		return UriNell.CLASS_SEMPARSE_EXECUTION;
	}

	String getSentence() {
		return ((Semparse) componentNell).getMetadata_SentenceList();
	}
}
