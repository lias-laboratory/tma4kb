package fr.ensma.lias.tma4kb.cardinalities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
		File INPUT_FILE =  new File(root+"cardinalitiesGlobal.config");
		File OUTPUT_FILE = new File(root+"GlobalConfig.java");
		OutputStream output = null;
		output = new FileOutputStream("cardinalities.properties");
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
		output.close();
		GenerateConfig gg = new GenerateConfig(INPUT_FILE,OUTPUT_FILE);
		gg.generateConfiguration();
	}
	
	public void makeLocalConfig() throws IOException {
		File INPUT_FILE =  new File(root+"cardinalitiesLocal.config");
		File OUTPUT_FILE = new File(root+"LocalConfig.java");
		OutputStream output = null;
		output = new FileOutputStream("cardinalities.properties");
		FileWriter res = new FileWriter(INPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		BufferedReader br = Files.newBufferedReader(Paths.get(input+"localcardinalities/part-00000"));
		String line = null;
		while ((line = br.readLine()) != null) {
			String p=getNiceName(line.substring(line.indexOf("<")+1, line.indexOf(">")));
			int i=line.indexOf(">");
			int j=line.indexOf("<", i);
			if (j>-1) {
				p+=getNiceName(line.substring(j, line.indexOf(">", j)));
				int cardMax=Integer.valueOf(line.substring(line.lastIndexOf(',')+1,line.length()-1));
				bw.write(p + ".max=" + cardMax);
				bw.newLine();
			}
		}
		br.close();
		BufferedReader br2 = Files.newBufferedReader(Paths.get(input+"domaincardinalities/part-00000"));
		while ((line = br2.readLine()) != null) {
			String p=getNiceName(line.substring(line.indexOf("<")+1, line.indexOf(">")));
			int i=line.indexOf(">");
			int j=line.indexOf("<", i);
			if (j>-1) {
				String dom=getNiceName(line.substring(j, line.indexOf(">", j)));
				bw.write(p + ".domain=" + dom);
				bw.newLine();
			}
		}
		br2.close();
		BufferedReader br3 = Files.newBufferedReader(Paths.get(input+"globalcardinalities/part-00000"));
		while ((line = br3.readLine()) != null) {
			String p=line.substring(line.indexOf("<")+1, line.indexOf(">"));
			int cardMax=Integer.parseInt(line.substring(line.lastIndexOf(',')+1,line.length()-1));
			bw.write(getNiceName(p) + ".max=" + cardMax);
			bw.newLine();
		}
		br2.close();
		bw.close();
		output.close();
		GenerateConfig gg = new GenerateConfig(INPUT_FILE,OUTPUT_FILE);
		gg.generateConfiguration();
	}
	
	public void makeCSConfig() throws IOException {
		File INPUT_FILE =  new File(root+"cardinalitiesCS.config");
		File OUTPUT_FILE = new File(root+"CSConfig.java");
		OutputStream output = null;
		output = new FileOutputStream("cardinalities.properties");
		BufferedReader br = Files.newBufferedReader(Paths.get(input+"cscardinalities/part-00000"));
		FileWriter res = new FileWriter(INPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		bw.write("Characteristic.set=");
		String line = null;
		while ((line = br.readLine()) != null) {
			String cs=line.substring(line.indexOf("SUBJECTnb"), line.length()-2);
			Integer equal=cs.indexOf("=");
			Integer open=cs.indexOf("<");
			Integer comma=cs.indexOf(",");
			bw.write(cs.substring(equal+1, comma)+",");
			comma=cs.indexOf(",",comma+1);
			equal=cs.indexOf("=",equal+1);
			String p="start";
			while(comma>-1) {
				p=getNiceName(cs.substring(open+1, equal-1));
				int card=Integer.parseInt(cs.substring(equal+1,comma));
				comma=cs.indexOf(",",comma+1);
				equal=cs.indexOf("=",equal+1);
				open=cs.indexOf("<",open+1);
				bw.write(p+":"+card+",");
			}
			p=getNiceName(cs.substring(open+1, equal-1));
			int card=Integer.parseInt(cs.substring(equal+1));
			bw.write(p+":"+card+";");
			/*example
			String start = "SUBJECTnb=1, <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>=1, <http://www.w3.org/2000/01/rdf-schema#label>=1";
			String end   = "CS1.value=1,type:1,label:1";*/
			
		}
		br.close();
		bw.close();
		output.close();
		GenerateConfig gg = new GenerateConfig(INPUT_FILE,OUTPUT_FILE);
		gg.generateConfiguration();
	}
	/**
	 * This method produces a file that is easier to read but is slower
	 * @throws IOException
	 */
	public void makeCSConfig2() throws IOException {
		File INPUT_FILE =  new File(root+"cardinalitiesCS.config");
		File OUTPUT_FILE = new File(root+"CSConfig.java");
		OutputStream output = null;
		output = new FileOutputStream("cardinalities.properties");
		BufferedReader br = Files.newBufferedReader(Paths.get(input+"cscardinalities/part-00000"));
		FileWriter res = new FileWriter(INPUT_FILE);
		BufferedWriter bw = new BufferedWriter(res);
		String line = null;
		int countCS=0;
		while ((line = br.readLine()) != null) {
			String cs=line.substring(line.indexOf("SUBJECTnb"), line.length()-2);
			Integer equal=cs.indexOf("=");
			Integer open=cs.indexOf("<");
			Integer comma=cs.indexOf(",");
			bw.write("CS"+countCS+".value="+cs.substring(equal+1, comma)+",");
			comma=cs.indexOf(",",comma+1);
			equal=cs.indexOf("=",equal+1);
			String p="start";
			while(comma>-1) {
				p=getNiceName(cs.substring(open+1, equal-1));
				int card=Integer.parseInt(cs.substring(equal+1,comma));
				comma=cs.indexOf(",",comma+1);
				equal=cs.indexOf("=",equal+1);
				open=cs.indexOf("<",open+1);
				bw.write(p+":"+card+",");
			}
			p=getNiceName(cs.substring(open+1, equal-1));
			int card=Integer.parseInt(cs.substring(equal+1));
			bw.write(p+":"+card);
			/*example
			String start = "SUBJECTnb=1, <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>=1, <http://www.w3.org/2000/01/rdf-schema#label>=1";
			String end   = "CS1.value=1,type:1,label:1";*/
			
			bw.newLine();
			countCS++;
		}
		bw.write("CharacteristicSet.nb="+countCS);
		br.close();
		bw.close();
		output.close();
		GenerateConfig gg = new GenerateConfig(INPUT_FILE,OUTPUT_FILE);
		gg.generateConfiguration();
	}
	
}
