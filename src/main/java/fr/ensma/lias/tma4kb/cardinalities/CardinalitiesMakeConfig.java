package fr.ensma.lias.tma4kb.cardinalities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class CardinalitiesMakeConfig {

	/**
	 * path to save the output cardinalities file
	 */
	public String output;

	/**
	 * input cardinalities file
	 */
	public String input;

	/**
	 * 
	 * @param rt   path to store the cardinalities config file
	 * @param inpt input of the cardinality calculation
	 */
	public CardinalitiesMakeConfig(String out, String in) {
		output = out;
		input = in;
	}

	/**
	 * Removes the prefix from an URI
	 * 
	 * @param uri the URI to remove the prefix from
	 * @return the URI without its prefix
	 */
	public String getNiceName(String uri) {
		int indexOfSeparator = uri.indexOf('#');

		if (indexOfSeparator != -1) {
			return uri.substring(indexOfSeparator + 1, uri.length());
		} else {
			int indexOfSlash = uri.lastIndexOf("/");
			return uri.substring(indexOfSlash + 1, uri.length());
		}
	}

	/**
	 * Reads an input file containing cardinalities and writes the cardinalities in
	 * the format predicate.max=N in the output file
	 * 
	 * @throws IOException
	 */
	public void makeGlobalConfig() throws IOException {
		File OUTPUT_FILE = new File(output);
		BufferedReader br = Files.newBufferedReader(Paths.get(input));
		FileWriter res = new FileWriter(OUTPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		String line = null;
		while ((line = br.readLine()) != null) {
			String p = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
			int cardMax = Integer.valueOf(line.substring(line.lastIndexOf(',') + 1, line.length() - 1));
			bw.write(getNiceName(p) + ".max=" + cardMax);
			bw.newLine();
		}
		br.close();
		bw.close();
	}
}