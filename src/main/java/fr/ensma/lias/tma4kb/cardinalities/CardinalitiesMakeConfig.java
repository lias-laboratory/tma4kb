package fr.ensma.lias.tma4kb.cardinalities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CardinalitiesMakeConfig {
	
	public String root ;
	public String input;
	
	public CardinalitiesMakeConfig(String rt, String inpt) {
		root=rt;
		input=inpt;
	}

	public String getNiceName(String uri) {
		int indexOfSeparator = uri.indexOf('#');
		
		if (indexOfSeparator!=-1) {
			return uri.substring(indexOfSeparator + 1, uri.length());
	    }
		else {
			int indexOfSlash = uri.lastIndexOf("/");
			return uri.substring(indexOfSlash + 1, uri.length());
		}
	}
	
	public void makeGlobalConfig() throws IOException {
		File INPUT_FILE =  new File(root+"wd100card.config");
		BufferedReader br = Files.newBufferedReader(Paths.get(input+"globalcardinalities/part-00000"));
		FileWriter res = new FileWriter(INPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		String line = null;
		while ((line = br.readLine()) != null) {
			String p=line.substring(line.indexOf("<")+1, line.indexOf(">"));
			int cardMax=Integer.valueOf(line.substring(line.lastIndexOf(',')+1,line.length()-1));
			bw.write(getNiceName(p) + ".max=" + cardMax);
			bw.newLine();
		}
		br.close();
		bw.close();
	}	

}