package eu.wdaqua.nell2rdf.beans;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import eu.wdaqua.nell2rdf.utils.NellMapper;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * POJO for Nell in-memory model that contains triples as in the csv Nell ontology file.
 * 
 * @author Christophe Gravier
 */
public class NellModel {

	public static Logger					log			= Logger.getLogger(NellModel.class);

	/**
	 * The Nell ontology is stored in memory as (P, {(S,O)}), since we want to browse it predicate per predicate.
	 */
	private Multimap<String, PredicatePair>	ontology	= HashMultimap.create();

	/**
	 * Number of line in the Nell ontology CSV file.
	 */
	private int								nbLines		= 0;

	/**
	 * Add a new triple to the Nell in-memory model
	 * 
	 * @param s
	 *            subject in the triple
	 * @param p
	 *            predicate in the triple
	 * @param o
	 *            object in the triple
	 */
	private void addTriple(final String s, final String p, final String o) {
		this.ontology.put(p, new PredicatePair(s, o));
	}

	public Multimap<String, PredicatePair> getOntology() {
		return this.ontology;
	}

	public void setOntology(final Multimap<String, PredicatePair> ontology) {
		this.ontology = ontology;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.ontology == null) ? 0 : this.ontology.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NellModel other = (NellModel) obj;
		if (this.ontology == null) {
			if (other.ontology != null)
				return false;
		} else if (!this.ontology.equals(other.ontology))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuffer output = new StringBuffer();
		for (final String predicate : this.ontology.keySet()) {
			output.append("\"").append(predicate).append("\"").append(" : \n");
			for (final PredicatePair subobj : this.ontology.get(predicate)) {
				output.append("\t\t(\"").append(subobj.getSubject()).append("\", \"").append(subobj.getObject()).append("\")\n");
			}
		}
		return output.toString();
	}

	/**
	 * Parses the NELL ontology file and populates the {@link #ontology}
	 * 
	 * @param ontologyFile
	 *            location of NELL ontology file on the filesystem.
	 */
	public void createModel(final String ontologyFile) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ontologyFile));
		} catch (final FileNotFoundException e) {
			log.info("Cannot open NELL ontology file !" + e.getMessage());
		}
		String fullLine;
		this.nbLines = 0;
		// int nbDisjonction = 0;
		try {
			while ((fullLine = br.readLine()) != null) {
				final String[] lineArray = fullLine.split("\t");
				if (lineArray.length != 3) {
					log.info("Skipped line that was not forming a triple : " + fullLine);
				} else if (lineArray[1].toLowerCase().equals("relation")) {
					log.warn("Ignoring line : \"" + fullLine + "\" since it is most likely a header in the tab-separated value file.");
				} else {
					final String subject = clean(lineArray[0]);
					final String predicate = clean(lineArray[1]);
					final String object = clean(lineArray[2]);

					addTriple(subject, predicate, object);
					this.nbLines++;
				}
			}
			br.close();
		} catch (final IOException e) {
			log.error("Cannot read file " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Clean string that starts with "concept:"
	 * 
	 * @param entity
	 *            String that should be stripped of prefix "concept:", if any.
	 */
	private String clean(String entity) {
		entity = entity.trim();
		entity = (entity.startsWith("concept:")) ? entity.replaceAll("concept:", "") : entity;
		return entity;
	}

	/**
	 * Map the in-memory model to a Jena model, as a twofold process:
	 * <ol>
	 * <li>first identifiy classes and properties,</li>
	 * <li>and then process with all other property-related or class-related assertions</li>
	 * </ol>
	 * In between the two steps, the method calls for <code>NellRdfUtils.countNoneUniqueName()</code> for checking if all class and properties have unique names. If not, a warning message is sent to
	 * log.warn().
	 * 
	 * @param prefix
	 *            Prefix to use when creating new Linked Data IRI.
	 * @return a Jena model representing NELL ontology.
	 */
	public Model mapToRDFModel(final String prefix) {

		// create an empty model
		final Model nellRdf = NellMapper.createEmptyModel(prefix);

		// create classes and properties
		NellMapper.lookupClassesAndProperties(this, nellRdf);

		// safety checking if all classes and properties have unique names.
		NellMapper.countNoneUniqueName(this);

		// work on all other relations than classes and properties
		NellMapper.parseAllPredicates(this, nellRdf);

		// TODO : deal with instances. Add a fourth program parameter for the full KB file and launch the process here.
		// NellMapper.processNellKB();

		return nellRdf;
	}

	/**
	 * Count the number of triples in the nell in-memory ontology, as read in the CSV file.
	 * 
	 * @return the number of triples.
	 */
	public int getNbTriples() {
		int somme = 0;
		for (final String predicate : this.ontology.keySet()) {
			somme += this.ontology.get(predicate).size();
		}
		return somme;
	}

	/**
	 * yes, getter. Well, you know.
	 * 
	 * @return number of line of th CSV file.
	 */
	public int getNbLines() {
		return this.nbLines;
	}

}
