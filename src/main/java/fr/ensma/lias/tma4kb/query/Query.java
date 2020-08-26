package fr.ensma.lias.tma4kb.query;

import java.util.List;
import java.util.Set;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public interface Query {

	/**
	 * Counts number of answers of the query
	 * 
	 * @param s the connection to the triplestore
	 * @param k the maximum number of answers
	 * @return the number of answers
	 */
	int isFailing(Session session, int k);

	/**
	 * get the factory that created this query
	 * 
	 * @return the factory that created this query
	 */
	QueryFactory getFactory();

	/**
	 * Returns the triple patterns of the query
	 * 
	 * @return the triple patterns of the query
	 */
	List<TriplePattern> getTriplePatterns();

	/**
	 * Add a triple pattern to this query
	 * 
	 * @param tp the added triple pattern
	 */
	void addTriplePattern(TriplePattern tp);

	/**
	 * Remove the input triple pattern from this query
	 * 
	 * @param the triple pattern to remove
	 */
	void removeTriplePattern(TriplePattern t);

	/**
	 * Get all the subqueries of this query
	 * 
	 * @return the subqueries of this query
	 */
	List<Query> getSubQueries();

	/**
	 * Get all the superqueries of this query
	 * 
	 * @return the superqueries of this query
	 */
	List<Query> getSuperQueries();

	/**
	 * Return true if this query is empty
	 * 
	 * @return true if this query is empty
	 */
	boolean isTheEmptyQuery();

	/**
	 * Return the current MFISs of this query
	 * 
	 * @return the MFISs of this query
	 */
	Set<Query> getAllMFIS();

	/**
	 * Return the current XSSs of this query
	 * 
	 * @return the XSSs of this query
	 */
	Set<Query> getAllXSS();

	/**
	 * Return the variables of this query
	 * 
	 * @return the variables of this query
	 */
	Set<String> getVariables();

	/**
	 * Set the initial query on which algorithms was executed
	 * 
	 * @param query the initial query on which algorithms was executed
	 */
	public void setInitialQuery(Query query);

	/**
	 * Run the Baseline algorithm and fills allMFIS and allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 */
	void runBase(Session session, int k);

	/**
	 * Run the BFS algorithm and fills allMFIS and allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 */
	void runBFS(Session session, int k);

	/**
	 * Run the variable-based algorithm and fills allMFIS and allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 */
	void runVar(Session session, int k) throws Exception;

	/**
	 * Run the cardinality-based algorithm and fills allMFIS and allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 * @param card    the file containing cardinalities
	 */
	void runFull(Session session, int k, String card) throws Exception;

	/**
	 * Run the cardinality-based algorithm for any cardinality and fills allMFIS and
	 * allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 * @param card    the file containing cardinalities
	 */
	void runFull_AnyCard(Session session, int k, String card) throws Exception;

	/**
	 * Run the cardinality-based algorithm with local cardinalities and fills
	 * allMFIS and allXSS
	 * 
	 * @param session connection to the KB
	 * @param k       maximum number of results
	 * @param card    the file containing cardinalities
	 */
	void runFull_Local(Session session, int k, String card) throws Exception;

}
