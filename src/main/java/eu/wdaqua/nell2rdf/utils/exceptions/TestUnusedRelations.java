package eu.wdaqua.nell2rdf.utils.exceptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class TestUnusedRelations {

	public static void main(String[] args) {
		File inputFile = new File("/tmp/uniqrelations.txt");
		File pairFile = new File("/tmp/pairs.txt");

		BiMap<String, String> pairs = HashBiMap.create();

		Set<String> usedRelations = new HashSet<>();

		try {
			InputStream ips = new FileInputStream(pairFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String fullLine;
			while ((fullLine = br.readLine()) != null) {
				String[] lineArray = fullLine.split("\t");
				pairs.put(lineArray[0], lineArray[1]);
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			InputStream ips = new FileInputStream(inputFile);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String relation;
			while ((relation = br.readLine()) != null) {
				// y'a-t-il une paire contenant cette relation
				// if (pairs.containsValue(relation)) {
				// System.out.println("removing "+relation);
				pairs.remove(relation);
				// }
				//
				// if (pairs.containsKey(relation)) {
				// System.out.println("removing "+relation);
				pairs.inverse().remove(relation);
				// }
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println(pairs.size()+" unused one");
		for (String rel : pairs.keySet()) {
			String inverseRel = pairs.get(rel);
			System.out.println("Unused : " + rel + " (inv. " + inverseRel + ")");
		}

	}
}