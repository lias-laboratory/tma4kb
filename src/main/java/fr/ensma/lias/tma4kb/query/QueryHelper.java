package fr.ensma.lias.tma4kb.query;

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

}
