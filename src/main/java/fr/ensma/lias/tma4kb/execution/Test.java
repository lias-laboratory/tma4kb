package fr.ensma.lias.tma4kb.execution;

public class Test {
	public static void main(String[] args) {
		int NB_EXEC = Integer.valueOf(args[1]); //5
		String FILE_QUERIES = args[2];//"queriesDBpedia.test";
		PropreAlgorithm t= new PropreAlgorithm(NB_EXEC,FILE_QUERIES);
		try {
			t.testGenAlgorithms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
