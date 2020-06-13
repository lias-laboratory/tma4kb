package fr.ensma.lias.tma4kb.query;

/**
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public interface Session {

	/**
	 * @throws Exception
	 */
	void close() throws Exception;

	/**
	 * Gives the number of executed queries to compute the MFS/XSS.
	 * 
	 * @return
	 */
	int getExecutedQueryCount();

	/**
	 * Set the number of queries executed
	 * 
	 * @param queryCount the number of queries executed
	 */
	void setExecutedQueryCount(int queryCount);

	/**
	 * Clear the number of executed queries
	 */
	void clearExecutedQueryCount();
	
	/**
	 * Set query execution time
	 * 
	 * @param queryCount the number of queries executed
	 */
	void setCountQueryTime(float queryCount);
	
	/**
	 * Gives the query execution time to compute the MFS/XSS.
	 * 
	 * @return
	 */
	float getCountQueryTime();
	

	/**
	 * Clear the executed query time
	 */
	void clearCountQueryTime();
}
