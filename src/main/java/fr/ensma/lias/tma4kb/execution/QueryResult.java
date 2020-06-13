package fr.ensma.lias.tma4kb.execution;

/**
 * A class to store the execution time and number of executed queries for each
 * algorithm execution
 * 
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class QueryResult {

	/**
	 * Execution time in ms
	 */
	private float time;

	private int nbExecutedQuery;
	
	private float queryCountTime;

	public QueryResult(float time, int nbExecutedQuery,  float countTime) {
		super();
		this.time = time;
		this.nbExecutedQuery = nbExecutedQuery;
		this.queryCountTime=countTime;
	}

	public float getTime() {
		return time;
	}

	public int getNbExecutedQuery() {
		return nbExecutedQuery;
	}

	public float getCountTime() {
		return queryCountTime;
	}

}
