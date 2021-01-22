package fr.ensma.lias.tma4kb.execution;

import fr.ensma.lias.tma4kb.execution.AlgorithmExec.AlgoChoice;
import fr.ensma.lias.tma4kb.query.AbstractQueryFactory.ChoiceOfTpst;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Mickael BARON (baron@ensma.fr)
 */
@Command(version = "TMA4KB 1.0", header = "%nCore for 'Too Many Answers Problem in Knowledge Bases' algorithms.%n", description = "", footer = "")
public class TMA4KBLauncher implements Runnable {

	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	@Option(names = { "-q",
			"--queries" }, required = true, description = "The file which contains the queries of dataset.")
	String queriesFile;

	@Option(names = { "-c",
			"--cardinalities" }, description = "The file which contains the dataset cardinalities.")
	String cardinalitiesFile;
	
	@Option(names = { "-l",
	"--local" }, description = "The file which contains the dataset local (class) cardinalities.")
	String localFile;
	
	@Option(names = { "-s",
	"--cs" }, description = "The file which contains the dataset characteristic sets.")
	String csFile;

	@Option(names = { "-e", "--execution" }, defaultValue = "5", description = "The number of executions.")
	int numberExecution;

	@Option(names = { "-k",
			"--threshold" }, defaultValue = "100", description = "The threshold for overabundant answers.")
	int k;

	@Option(names = { "-m",
			"--method" }, required=true, description = "The evaluation method in Jena. Methods possible: ${COMPLETION-CANDIDATES}")
	QueryMethod method;

	@Option(names = { "-a",
			"--algorithm" }, split = ",", defaultValue = "base,var,full", description = "The algorithm to run: ${COMPLETION-CANDIDATES}")
	AlgoChoice[] algorithm;

	@Option(names = { "-t",
			"--triplestore" }, defaultValue = "jena", description = "Choice of triplstore: ${COMPLETION-CANDIDATES}")
	ChoiceOfTpst triplestore;

	public static void main(String[] args) {
		new CommandLine(new TMA4KBLauncher()).execute(args);
	}

	@Override
	public void run() {
		AlgorithmExec t = new AlgorithmExec(numberExecution, queriesFile, cardinalitiesFile, localFile, csFile, k, method, algorithm,
				triplestore);
		try {
			t.testGenAlgorithms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
