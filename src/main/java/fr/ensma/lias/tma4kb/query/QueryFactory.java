package fr.ensma.lias.tma4kb.query;

import fr.ensma.lias.tma4kb.query.algorithms.Algorithm;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public interface QueryFactory {

    /**
     * Creates a query
     * 
     * @param rdfQuery the string of the query
     * @return the resulting query
     */
    Query createQuery(String rdfQuery);
    
    /**
     * Creates a subquery of a query
     * 
     * @param rdfQuery string of the subquery
     * @param initialQuery the intial query
     * @return a new query
     */
    Query createQuery(String rdfQuery, Query initialQuery);


    /**
     * Creates a connection to the KB
     * 
     * @return a connection to the KB
     */
    Session createSession();

	Query createQuery(String rdfQuery, Algorithm alg);
}
