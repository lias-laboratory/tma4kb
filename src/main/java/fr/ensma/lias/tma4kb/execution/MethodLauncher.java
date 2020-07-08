package fr.ensma.lias.tma4kb.execution;

import fr.ensma.lias.tma4kb.triplestore.jenatdb.MethodExec;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * 
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)  
 * 
 */


public class MethodLauncher implements Runnable {
	//Parameters of command line
	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	@Option(names = { "-q",
			"--queries" }, required = true, description = "The file which contains the queries of dataset.")
	String queriesFile;

	@Option(names = { "-k",
			"--threshold" }, split=",",defaultValue = "100", description = "The threshold for overabundant answers. List is possible with comma between different threshold.")
	int[] k;
	
	@Option(names = { "-e", "--execution" }, defaultValue = "5", description = "The number of executions.")
	int numberExecution;

	public static void main(String[] args) {
		new CommandLine(new MethodLauncher()).execute(args);
	}

	@Override
	public void run() {
		MethodExec t = new MethodExec(queriesFile, k, numberExecution);
		try {
			t.methodRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

