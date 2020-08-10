package fr.ensma.lias.tma4kb.query;

import java.io.IOException;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public interface QueryHelper {

	/**
	 * Executes a query and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int executeQuery(Session session, int k);
	
	/**
	 * Executes a query using method ALL and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int execute_ALL(Session session, int k) throws IOException;
	
	/**
	 * Executes a query using method STOP_K and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int execute_STOPK(Session session, int k) throws IOException;
	
	/**
	 * Executes a query using method COUNT and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int execute_COUNT(Session session, int k) throws IOException;
	
	/**
	 * Executes a query using method LIMIT and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int execute_LIMIT(Session session, int k) throws IOException;
	
	/**
	 * Executes a query using method COUNT+LIMIT and returns the number of answers
	 * 
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int execute_COUNTLIMIT(Session session, int k) throws IOException;

}
