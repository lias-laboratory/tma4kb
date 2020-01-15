package fr.ensma.lias.tma4kb.cardinalities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateConfig {
    public File INPUT_FILE;
    public File OUTPUT_FILE;

    public GenerateConfig(File input, File output) {
    	this.INPUT_FILE=input;
    	this.OUTPUT_FILE=output;
    }
    
    /**
     * Generate the .java config file from the .config file
     * @throws IOException
     */
    public void generateConfiguration() throws IOException {

        int indexOfEqual=0;
        int indexOfDot=0;
        
    	FileReader in = new FileReader(INPUT_FILE);
        BufferedReader br = new BufferedReader(in);
        
    	FileWriter out = new FileWriter(OUTPUT_FILE);
        BufferedWriter bw = new BufferedWriter(out);
        
        // leading hard coded
        bw.write("package fr.ensma.lias.tma4kb.cardinalities;");
        bw.newLine();
        bw.write("import org.aeonbits.owner.Config;");
        bw.newLine();
        
        bw.write("import org.aeonbits.owner.Config.Sources;");
        bw.newLine();
        bw.newLine();

        indexOfDot = OUTPUT_FILE.getName().indexOf("."); // remove the dot
   
        bw.write("@Sources(\"classpath:"+ INPUT_FILE.getName() +"\")");
        bw.newLine();
        bw.write("public interface " + OUTPUT_FILE.getName().
        		substring(0, indexOfDot)+ " extends Config { ");
        bw.newLine();
        bw.newLine();
        

        String line;
        while ((line=br.readLine())!=null) {
        	bw.flush();
        	indexOfEqual = line.indexOf("="); 
        	indexOfDot = line.indexOf(".");
        	bw.write("\t"+"@Key(");
        	bw.write("\""+line.substring(0, indexOfEqual)+"\")"); // write everything before the '='
        	bw.newLine();
        	line = replaceChar(line, Character.toUpperCase(line.charAt(indexOfDot+1)), indexOfDot+1); // capitalize the first character after the dot
        	line = line.substring(0, indexOfEqual); // remove the '='
        	bw.write("\t" + "String "+ line.replace(".","") + "();"); // write the line without the dot
        	bw.newLine();
        	bw.newLine();

        }
        bw.write("}");
        bw.close();
        br.close();
        
        }
    
    /**
     * Replace a character within a string
     * 
     * @param str the entire string
     * @param c the new character 
     * @param pos the position of the character we want to change
     * 
     * @return the new string
     */
    public String replaceChar(String str, Character c, int pos) {
    	return str.substring(0,pos) + c + str.substring(pos+1);
     }
}
