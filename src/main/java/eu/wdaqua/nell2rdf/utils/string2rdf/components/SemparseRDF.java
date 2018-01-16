package eu.wdaqua.nell2rdf.utils.string2rdf.components;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import eu.wdaqua.nell2rdf.extract.metadata.models.Semparse;
import eu.wdaqua.nell2rdf.utils.UriNell;

public class SemparseRDF extends ComponentRDF {
	
	public SemparseRDF(final Semparse semparse, Resource belief) {
		super(semparse, belief);
	}

	public void addTriples () {
		super.addTriples();
		addSentence();
	}
	
	void addSentence() {
		Property predicate = componentExecution.getModel().getProperty(UriNell.PROPERTY_SENTENCE);
		RDFNode object = componentExecution.getModel().createTypedLiteral(ResourceFactory.createLangLiteral(getSentence(), UriNell.ENGLISH_TAG));
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
