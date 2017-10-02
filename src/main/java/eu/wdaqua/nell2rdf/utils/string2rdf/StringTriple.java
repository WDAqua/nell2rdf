package eu.wdaqua.nell2rdf.utils.string2rdf;

public class StringTriple {
	
	private String subject, predicate, object;
	
	StringTriple (String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public String getPredicate() {
		return this.predicate;
	}
	
	public String getObject() {
		return this.object;
	}
	
}
