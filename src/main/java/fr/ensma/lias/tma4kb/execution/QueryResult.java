package fr.ensma.lias.tma4kb.execution;

/**
 * A class to store the execution time and number of executed queries for each algorithm execution
 * 
 */
public class QueryResult {

	/**
	 * execution time in ms
	 */
	private float time;

	private int nbExecutedQuery;

	public QueryResult(float time, int nbExecutedQuery) {	
		super();
		this.time = time;
		this.nbExecutedQuery = nbExecutedQuery;
	}

	public float getTime() {
		return time;
	}

	public int getNbExecutedQuery() {
		return nbExecutedQuery;
	}

}
