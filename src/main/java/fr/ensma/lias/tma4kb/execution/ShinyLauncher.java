package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fr.ensma.lias.tma4kb.execution.AlgorithmExec.AlgoChoice;
import fr.ensma.lias.tma4kb.query.AbstractQueryFactory.ChoiceOfTpst;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class ShinyLauncher implements Runnable {

		@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
		boolean usageHelpRequested;

		@Option(names = { "-i", "--iteration" }, defaultValue = "5", description = "The number of iterations.")
		int numberExecution;
		
		@Option(names = { "-u", "--url" }, defaultValue = "jena", description = "The url of the endpoint")
		String endpoint;
		
		@Option(names = { "-q", "--query" }, description = "The initial query.")
		String query;

		@Option(names = { "-k", "--threshold" }, defaultValue = "100", description = "The threshold for overabundant answers.")
		int k;

		@Option(names = { "-a",
				"--algos" }, split = ",", defaultValue = "base,var,full", description = "The algorithm to run: ${COMPLETION-CANDIDATES}")
		AlgoChoice[] algorithm;

		@Option(names = { "-c", "--cards" }, description = "The file which contains the dataset cardinalities.")
		// TODO
		String cardinalities;
		
		@Option(names = { "-o",
		"--output" }, required=true, description = "The file to store the results")
		String outputFile;


		public static void main(String[] args) {
			new CommandLine(new ShinyLauncher()).execute(args);
		}

		@Override
		public void run() {
			BufferedWriter fichier;
			try {
				fichier = new BufferedWriter(new FileWriter(outputFile));
				fichier.write("log:\n");
				fichier.close();
			} catch (IOException e) {
				System.err.println("Unable to create the file with the experiment results.");
				e.printStackTrace();
			}
			String cardinalitiesFile = "src/main/samples/card1";
			try {
				fichier = new BufferedWriter(new FileWriter(cardinalitiesFile));
				for (String s : cardinalities.split("\\\\n"))
					fichier.write(s+"\n");
				fichier.close();
			} catch (IOException e) {
				System.err.println("Unable to create the file with the experiment results.");
				e.printStackTrace();
			}
			AlgorithmExec t = new AlgorithmExec(numberExecution, query, cardinalitiesFile, k, algorithm, outputFile, endpoint);
			try {
				t.shinyExec();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
