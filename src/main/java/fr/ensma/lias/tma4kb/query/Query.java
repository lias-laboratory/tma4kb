package fr.ensma.lias.tma4kb.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ensma.lias.tma4kb.query.algorithms.Algorithm;

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
	int nbResults(Session session, int k);

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

		void setAlgorithm(Algorithm alg);

	boolean includesSimple(Query q);

	void setCardMax(int triple, int cardMax);

	Integer nbResults(Map<Query, Integer> executedQueries, Session s, int k);

	void runAlgo(Session session, int k);

}
