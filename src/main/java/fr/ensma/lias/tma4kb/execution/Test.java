package fr.ensma.lias.tma4kb.execution;

public class Test {
	public static void main(String[] args) {
		String FILE_QUERIES = "queriesWatDiv100.test";
		int NB_EXEC = 5;
		PropreAlgorithm t= new PropreAlgorithm(NB_EXEC,FILE_QUERIES);
		try {
			t.testGenAlgorithms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
