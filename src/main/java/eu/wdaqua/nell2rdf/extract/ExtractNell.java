package eu.wdaqua.nell2rdf.extract;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import eu.wdaqua.nell2rdf.utils.string2rdf.StringTranslate;

/**
 * @author Quentin Cruzille & Jose M. Gimenez-Garcia
 */
public class ExtractNell {
	/**
	 * Permet d'ignorer la premiere ligne qui contient les noms des colonnes.
	 */
	private boolean first;

	/**
	 * Module de conversion des donnees Nell en model Jena.
	 */
	private StringTranslate translator;

	/**
	 * Constructeur, ne prend pas d'arguement, initialise first et translator.
	 */
	public ExtractNell(final String prefix, final String metadata, String separator, String lang, boolean candidates, String file, boolean deleteOriginalTriples) {
		this.first = true;
		this.translator = new StringTranslate(prefix, metadata, separator, lang, candidates, file, deleteOriginalTriples);
	}

	/**
	 * Extrait les informations de Nell, les convertis et les traduits ligne par ligne. Modified to pass the whole line to the translator to process all metadata.
	 */
	public void extraction(String instancesFileName) {
		try {
			String line;
			InputStream ips = new FileInputStream(instancesFileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			while ((line = br.readLine()) != null) {
				if (this.first) {
					this.first = false;
				} else {
					String[] lineSplit = line.split("\t");
					if (lineSplit.length >= 12) {
						// String[] lineRDF = { lineSplit[0], lineSplit[1], lineSplit[2], lineSplit[6], lineSplit[7], lineSplit[8], lineSplit[9], lineSplit[10], lineSplit[11] };
						this.translator.stringToRDF(lineSplit);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Renvoi le translator de l'extract, necessaire pour recuperer le model du translator dans le module principal.
	 * @return
	 */
	public StringTranslate getTranslator() {
		return translator;
	}
}
