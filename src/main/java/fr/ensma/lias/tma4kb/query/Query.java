/*********************************************************************************
* This file is part of TMA4KB Project.
* Copyright (C) 2019 LIAS - ENSMA
*   Teleport 2 - 1 avenue Clement Ader
*   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
* 
* QARS4UKB is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* QARS4UKB is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with TMA4KB.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.tma4kb.query;

import java.util.List;
import java.util.Set;



/**
 * @author Stephane JEAN
 */
public interface Query {

	/**
	 * Checks whether this query has too many answers or not
	 * 
	 * @param s the connection to the triplestore
	 * @param k the number maximum of answers
	 * @return true is the result if this query has too many answers
	 */
	boolean isFailing(Session session, int k);    
    
	/**
	 * get the factory that created this query
	 * 
	 * @return the factory that created this query
	 */
	QueryFactory getFactory() ;
	
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
     * @return the subqueries of this query
     */
    List<Query> getSubQueries();
    
    /**
     * Get all the superqueries of this query
     * @return the superqueries of this query
     */
    List<Query> getSuperQueries();
    
    /**
     * Get all the subqueries of this query, which are superqueries of base
     * @return the subqueries of this query, which are superqueries of base
     */
	List<Query> getBaseSubQueries(Query base);
    
    /**
     * Return true if this query is empty
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
     * Run the Baseline algorithm and fills allMFIS and allXSS
     * 
     * @param session connection to the KB
     * @param k maximum number of results
     */
    void runBaseline(Session session, int k);
    
    /**
     * Run the BFS algorithm and fills allMFIS and allXSS
     * 
     * @param session connection to the KB
     * @param k maximum number of results
     */
    void runBFS(Session session, int k);

    /**
     * Use global cardinalities to calculate Qbase starting point for the cardinalities algorithm and fills Qbase
     * 
     * @param session connection to the KB
     * @throws Exception 
     */
	void findQbase(Session instance) throws Exception;

	/**
     * Use local cardinalities to calculate Qbase starting point for the cardinalities algorithm and fills Qbase
     * 
     * @param session connection to the KB
	 * @throws Exception 
     */
	void findQbaseLocal(Session instance) throws Exception;
	
	/**
     * Run the CardAlgo algorithm and fills allMFIS and allXSS
     * 
     * @param session connection to the KB
     * @param k maximum number of results
	 * @throws Exception 
     */
    void runCardAlgo(Session session, int k) throws Exception;

}
