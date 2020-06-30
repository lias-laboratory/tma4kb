package fr.ensma.lias.tma4kb.execution.jena;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class MethodLauncher implements Runnable {
	@Option(names = { "-h", "--help" }, usageHelp = true, description = "Print usage help and exit.")
	boolean usageHelpRequested;

	@Option(names = { "-q",
			"--queries" }, required = true, description = "The file which contains the queries of dataset.")
	String queriesFile;

	@Option(names = { "-k",
			"--threshold" }, defaultValue = "100", description = "The threshold for overabundant answers.")
	int k;

	public static void main(String[] args) {
		new CommandLine(new MethodLauncher()).execute(args);
	}

	@Override
	public void run() {
		MethodExec t = new MethodExec(queriesFile, k);
		try {
			t.methodRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
