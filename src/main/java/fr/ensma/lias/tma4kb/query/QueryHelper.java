package fr.ensma.lias.tma4kb.query;

public interface QueryHelper {

    /**
     * Returns a string representation of the query this helper uses
     * @return the query in string form
     */
	String toNativeQuery();

	/**
	 * Executes a query and returns a boolean indicating if the query succeeds (fewer than k answers) or fails (more than k answers)
	 * @param session
	 * @param k the maximum number of answers
	 * @return true if the query fails, false otherwise
	 */
    boolean executeQuery(Session session,int k);

    /**
	 * Executes a query and returns the number of answers
	 * @param session
	 * @return true if the query fails, false otherwise
	 */
	int countQuery(Session session,int k);
    
}
