package eu.wdaqua.nell2rdf.utils.exceptions;

/**
 * When the subject in a triple is null
 * @author Christophe Gravier
 */
@SuppressWarnings("serial")
public class NullSubjectException extends Exception {

	public NullSubjectException(String string) {
		super(string);
	}
}
