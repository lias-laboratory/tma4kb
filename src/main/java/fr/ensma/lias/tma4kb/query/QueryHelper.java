package fr.ensma.lias.tma4kb.query;

public interface QueryHelper {


    /**
	 * Executes a query and returns the number of answers
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int executeQuery(Session session,int k);
    
}
