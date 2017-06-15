package eu.wdaqua.nell2rdf.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import eu.wdaqua.nell2rdf.utils.string2rdf.StringTranslate;
import org.apache.log4j.Logger;

/**
 * @author Quentin Cruzille & Jose M. Gimenez-Garcia
 */
public class Extract {
	
	public static Logger log = Logger.getLogger(Extract.class);
	
	/**
	 * Permet d'ignorer la premiere ligne qui contient les noms des colonnes.
	 */
	private boolean first;

	/**
	 * Module de conversion des donnees Nell en model Jena.
	 */
	private StringTranslate translator;

	/**
	 * Fichier contenant les donnees Nell.
	 */
	private String inputFile;

	/**
	 * Prefixe.
	 */
	private String prefix;

	/**
	 * Constructeur, ne prend pas d'arguement, initialise first et translator.
	 */
	public Extract(final String inputFile, final String prefix, final String metadata, final String separator, final String lang, final boolean candidates, String file, boolean deleteOriginalTriples) {
		this.first = true;
		this.translator = new StringTranslate(prefix, metadata, separator, lang, candidates, file, deleteOriginalTriples);
		this.inputFile = inputFile;
		this.prefix = prefix;
	}

	/**
	 * Extrait les informations de Nell, les convertis et les traduits ligne par ligne.
	 */
	public void extraction() {
		try {
			String line;
			InputStream ips = new FileInputStream(this.inputFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			while ((line = br.readLine()) != null) {
				if (this.first) {
					this.first = false;
				} else {
					String[] lineSplit = line.split("\t");
					if (lineSplit.length >= 12) {
					String[] lineRDF = { lineSplit[0], lineSplit[1], lineSplit[2], lineSplit[6], lineSplit[7], lineSplit[8], lineSplit[9], lineSplit[10], lineSplit[11] };
					this.clean(lineRDF);
					this.translator.stringToRDF(lineRDF);}
					else {
						log.info("Cannot process line :"+line+" : insufficient number of column values ! (<12)");
					}
				}
			}
			br.close();
			ipsr.close();
			ips.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prends en entree un tableau de chaines de caracteres contenant les informations extraites d'une ligne de Nell et les transforment pour qu'elles soient valides dans le model Jena.
	 * @param resources
	 */
	private void clean(String[] resources) {
		
		String[] resourcesSplit = resources[0].split(":", 2);
		if (resourcesSplit[0].equals("concept")) {
			resourcesSplit[1] = resourcesSplit[1].replaceAll(":", "/");
			resources[0] = prefix + resourcesSplit[1];
		}

		resourcesSplit = resources[2].split(":", 2);
		if (resourcesSplit[0].equals("concept")) {
			resourcesSplit[1] = resourcesSplit[1].replaceAll(":", "/");
			resources[2] = prefix + resourcesSplit[1];
		}

		if (resources[7].equals("")) {
			resources[7] = null;
		}

		if (resources[8].equals("")) {
			resources[8] = null;
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
