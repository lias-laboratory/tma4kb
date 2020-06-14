package fr.ensma.lias.tma4kb.execution;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Mickael BARON (baron@ensma.fr)
 */
@Command(version = "TMA4KB 1.0", header = "%nCore for 'Too Many Answers Problem in Knowledge Bases' algorithms.%n", description = "TBA", footer = "Coded with ♥ by Louise PARKIN.")
public class TMA4KBLauncher implements Runnable {

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	@Option(names = { "-q",
			"--queries" }, required = true, description = "The file which contains the queries of dataset.")
	String queriesFile;

	@Option(names = { "-c",
			"--cardinalities" }, required = true, description = "The file which contains the dataset cardinalities.")
	String cardinalitiesFile;

	@Option(names = { "-e", "--execution" }, defaultValue = "5", description = "The number of executions.")
	int numberExecution;
	
	@Option(names = { "-k", "--threshold" }, defaultValue = "100", description = "The threshold for overabundant answers.")
	int k;
	
	@Option(names = { "-m", "--method"}, defaultValue = "0", description = "The evaluation method in Jena. 0: SELECT_ALL, 1: SELECT_K, 2: COUNT, 3: LIMIT, 4: COUNT&LIMIT")
	int method;

	public static void main(String[] args) {
		new CommandLine(new TMA4KBLauncher()).execute(args);
	}

	@Override
	public void run() {
		AlgorithmExec t = new AlgorithmExec(numberExecution, queriesFile, cardinalitiesFile, k, method);
		try {
			t.testGenAlgorithms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
