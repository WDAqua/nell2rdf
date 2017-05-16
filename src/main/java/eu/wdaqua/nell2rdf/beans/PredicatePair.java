package eu.wdaqua.nell2rdf.beans;

/**
 * POJO for Nell in-memory model of the CSV ontology file for representing a Pair < S, O >, where :
 * <ul>
 * <li>S stands for Subject</li>
 * <li>P stands for Predicate</li>
 * </ul>
 * @author Christophe Gravier
 */
public class PredicatePair {
	
	/**
	 * Subject of the Pair.
	 */
	private String subject;
	
	/**
	 * Object of the Pair.
	 */
	private String object;

	public PredicatePair(String subject, String object) {
		super();
		this.subject = subject;
		this.object = object;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredicatePair other = (PredicatePair) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		return true;
	}

}
