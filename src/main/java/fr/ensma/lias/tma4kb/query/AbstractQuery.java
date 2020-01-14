/*********************************************************************************
 * This file is part of TMA4KB Project.
* Copyright (C) 2019 LIAS - ENSMA
*   Teleport 2 - 1 avenue Clement Ader
*   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
* 
* ma4ukb is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* ma4ukb is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with TMA4KB.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.tma4kb.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;


/**
 * @author Stéphane JEAN
 * @author Ibrahim DELLAL
 */
public abstract class AbstractQuery implements Query {

	/**
	 * Factory to create other queries
	 */
	protected QueryFactory factory;

	@Override
	public QueryFactory getFactory() {
		return factory;
	}

	/**
	 * String of this query
	 */
	protected String rdfQuery;

	/**
	 * List of triple patterns of this query
	 */
	protected List<TriplePattern> triplePatterns;

	/**
	 * Number of triple patterns of this query (this could be computed)
	 */
	protected int nbTriplePatterns;

	/**
	 * List of the MFIS of this query
	 */
	public Set<Query> allMFIS;

	/**
	 * List of the XSS of this query
	 */
	public Set<Query> allXSS;

	/**
	 * Most queries will be created during the execution of algorithms the
	 * initialQuery represents the query on which algorithms was executed
	 */
	protected Query initialQuery;
	
	/**
	 * baseQuery is the starting point for CardAlgo
	 * This is the conjunction of triple patterns with predicates having maxCard = 1
	 */
	protected Query baseQuery;
	
	/**
	 * decomposition of the query into sets of triples so that each variable appears in only one set
	 * decomp is used to avoid executing cartesian products
	 */
	protected List<Set<TriplePattern>> decomp;
	   
	/**
	 * Builds a query from its string and a reference to its factory
	 * 
	 * @param factory a factory to create some queries
	 * @param query the string of this query
	 */
	public AbstractQuery(QueryFactory factory, String query) {
		this.factory = factory;
		this.rdfQuery = query;
		this.decomp = new ArrayList<Set<TriplePattern>>();
		this.decomposeQuery();
		nbTriplePatterns = triplePatterns.size();
	}
	

	/**
	 * Decompose a SPARQL Query into a set of triple patterns.
	 */
	protected void decomposeQuery() {
		triplePatterns = new ArrayList<TriplePattern>();

		if (!rdfQuery.equals("")) {
			int indiceOfTriplePattern = 1;
			int indexOfLeftEmbrace = rdfQuery.indexOf('{');
			int indexOfDot = rdfQuery.indexOf(" . ", indexOfLeftEmbrace);

			while (indexOfDot != -1) {
				triplePatterns.add(new TriplePattern(rdfQuery.substring(indexOfLeftEmbrace + 2, indexOfDot),
						indiceOfTriplePattern));
				indiceOfTriplePattern++;
				indexOfLeftEmbrace = indexOfDot + 1;
				indexOfDot = rdfQuery.indexOf(" . ", indexOfLeftEmbrace);
			}
			triplePatterns.add(new TriplePattern(rdfQuery.substring(indexOfLeftEmbrace + 2, rdfQuery.length() - 2),
					indiceOfTriplePattern));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((triplePatterns == null) ? 0 : new HashSet<TriplePattern>(triplePatterns).hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractQuery other = (AbstractQuery) obj;
		if (other.nbTriplePatterns != this.nbTriplePatterns) // same
			// size
			return false;
		if (!this.includesSimple(other)) // and one is included in the other
			return false;
		return true;
	}

	@Override
	public Set<Query> getAllMFIS() {
		return this.allMFIS;
	}

	@Override
	public Set<Query> getAllXSS() {
		return this.allXSS;
	}
	
	/**
	 * Get the decomposition of cartesian products
	 * 
	 * @return
	 */
	public List<Set<TriplePattern>> getDecomp() {
		if(decomp.size()==0) {
			setDecomp();
		}
		return this.decomp;
	}

	/**
	 * Get the initial query on which algorithms was executed
	 * 
	 * @return
	 */
	public Query getInitialQuery() {
		return this.initialQuery;
	}

	/**
	 * Set the initial query on which algorithms was executed
	 * 
	 * @param query the initial query on which algorithms was executed
	 */
	public void setInitialQuery(Query query) {
		this.initialQuery = query;
	}
	/**
	 * Set the decomposition of cartesian products
	 */
	public void setDecomp() {
		if (initialQuery==null || initialQuery==this) {
			decomposePC();
		}
		else {
			for (Set<TriplePattern> s :((AbstractQuery)initialQuery).getDecomp()) {
				Set<TriplePattern> newSet= new HashSet<TriplePattern>();
				for (TriplePattern t :s) {
					if (triplePatterns.contains(t)) {
						newSet.add(t);
					}
				}
				if(!newSet.isEmpty())
					this.decomp.add(newSet);
			}
		}
	}

	@Override
	public List<TriplePattern> getTriplePatterns() {
		return triplePatterns;
	}

	@Override
	public String toString() {
		return rdfQuery;
	}

	@Override
	public boolean isTheEmptyQuery() {
		return nbTriplePatterns == 0;
	}

	@Override
	public boolean isFailing(Session session, int k) {
		if (isTheEmptyQuery())
			return true;
		return isFailingAux(session, k);
	}
	
	
	public boolean isFailing2(Session session, int k) {
		if (isTheEmptyQuery())
			return true;
		return (isFailingNb(session, k)>k);
	}

	/**
	 * Checks whether this query has too many answers or not
	 * 
	 * @param s the connection to the triplestore
	 * @param k the maximum number of answers
	 * @return true is the result if this query has too many answers
	 */
	protected abstract boolean isFailingAux(Session session, int k);

	/**
	 * Counts number of answers of the query
	 * 
	 * @param s the connection to the triplestore
	 * @param k the maximum number of answers
	 * @return the number of answers
	 */
	protected abstract int isFailingNb(Session session,int k) ;
	
	/**
	 * Check whether a query includes another one
	 * 
	 * @param q a query
	 * @return true if this query includes q
	 */
	public boolean includesSimple(Query q) {
		for (TriplePattern tp : q.getTriplePatterns()) {
			if (!includes(tp))
				return false;
		}
		return true;
	}

	/**
	 * Check whether this query includes a triple pattern
	 * 
	 * @param t a triple pattern
	 * @return true if this query includes t
	 */
	private boolean includes(TriplePattern t) {
		if (rdfQuery.indexOf(t.toString()) == -1)
			return false;
		return true;
	}

	/**
	 * Creates a string representation of a query in the shape ti^tj^...^tk
	 * 
	 * @return the string representation of the query
	 */
	protected String toSimpleString(Query initialQuery) {
		TriplePattern temp;
		String res = "";
		for (int i = 0; i < triplePatterns.size(); i++) {
			if (i > 0)
				res += " ^ ";
			temp = triplePatterns.get(i);
			if (initialQuery != null) {
				res += "t" + (initialQuery.getTriplePatterns().indexOf(temp) + 1);
			}
		
		}
		return res;
	}

	/**
	 * Computes the direct subqueries of this query
	 * 
	 * @return the list of direct subqueries of the query
	 */
	public List<Query> getSubQueries() {
		List<Query> res = new ArrayList<Query>();
		for (TriplePattern tp : getTriplePatterns()) {
			Query qNew = factory.createQuery(toString(), initialQuery);
			qNew.removeTriplePattern(tp);
			res.add(qNew);
		}
		return res;
	}
	
	/**
	 * Computes the direct subqueries of this query that are superqueries of the base query
	 * 
	 * @param the base query
	 * @return the list of direct subqueries of this query that are superqueries of the base query
	 */
	public List<Query> getBaseSubQueries(Query base) {
		List<Query> res = new ArrayList<Query>();
		for (TriplePattern tp : getTriplePatterns()) {
			if (!((AbstractQuery)base).includes(tp)) {
				Query qNew = factory.createQuery(toString(), initialQuery);
				qNew.removeTriplePattern(tp);
				res.add(qNew);
			}
		}
		return res;
	}

	/**
	 * Computes the direct superqueries of this query
	 * 
	 * @return the list of direct superqueries of the query
	 */
	public List<Query> getSuperQueries() {
		List<Query> res = new ArrayList<Query>();
		for (TriplePattern tp : initialQuery.getTriplePatterns()) {
			if (!includes(tp)) {
				Query qNew = factory.createQuery(toString(),initialQuery);
				qNew.addTriplePattern(tp);
				res.add(qNew);
			}
		}
		return res;
	}

	@Override
	public void addTriplePattern(TriplePattern tp) {
		triplePatterns.add(tp);
		nbTriplePatterns++;
		rdfQuery = computeRDFQuery(triplePatterns);
	}

	@Override
	public void removeTriplePattern(TriplePattern t) {
		triplePatterns.remove(t);
		nbTriplePatterns--;
		rdfQuery = computeRDFQuery(triplePatterns);
	}
	
	/*public void setCardMax(int triple, int cardMax) {
		this.triplePatterns.get(triple).setCardMax(cardMax);
	}*/
	public void setCardMax(int triple, boolean cardMax) {
		this.triplePatterns.get(triple).setCardMax1(cardMax);
	}

	/**
	 * Computes the string of this query from a list of triple patterns
	 * 
	 * @param listTP a list of triple patterns
	 * @return the string of this query
	 */
	private String computeRDFQuery(List<TriplePattern> listTP) {
		String res = "";
		int nbTPs = listTP.size();
		if (nbTPs > 0) {
			res = "SELECT * WHERE { ";
			for (int i = 0; i < nbTPs; i++) {
				if (i > 0)
					res += " . ";
				res += listTP.get(i).toString();
			}
			res += " }";
		}
		return res;
	}

	/**
	 * Returns a boolean : true if the query fails (returns more than k results)
	 * 
	 * @param executedQueries a cache of already executed queries
	 * @param s connection to the KB
	 * @param k the maximum number of results
	 * @return true iff this query is failing
	 */
	private boolean isFailing(Map<Query, Boolean> executedQueries, Session s, int k) {
		/*if (this.equals(this.getInitialQuery())) { // No longer supposing the initial query fails
			executedQueries.put(this, true);
			return true;
		}*/
		Boolean val = executedQueries.get(this);
		if (val == null) {
			val = isFailing(s, k);
			executedQueries.put(this, val);
		}
		return val;
	}
	
	/**
	 * Returns the number of results to a query. Avoids executing cartesian products by multiplying the number of results of the subqueries
	 * 
	 * @param executedQueries a cache of already executed queries associated with their nb of results
	 * @param s connection to the KB
	 * @param k the maximum number of results
	 * @return true iff this query is failing
	 */
	protected boolean isFailingNb(Map<Query, Integer> executedQueries, Session s, int k) {
		/*if (this.equals(this.getInitialQuery())) { // No longer supposing the initial query fails
			executedQueries.put(this, true);
			return true;
		}*/
		Integer val = executedQueries.get(this);
		if (val == null) {
			if (this.isTheEmptyQuery()) {
				executedQueries.put(this, k+1);
				return true;
			}
			if (decomp.size()==0) {
				setDecomp();
			}
			if (decomp.size()==1) {
				val=isFailingNb(s, k);
				executedQueries.put(this, val);
			}
			else {
				List<Query> subQ=new ArrayList<Query>();
				for (Set<TriplePattern> set:decomp) {
					Query q = factory.createQuery("", initialQuery);
					for (TriplePattern t :set) {
						q.addTriplePattern(t);
					}
					subQ.add(q);
				}
				val=1;
				Integer val2;
				for (Query q:subQ) {
					val2= executedQueries.get(q);
					if (val2!=null) {
						val*=val2;
						subQ.remove(q);
					}
				}
				while (!subQ.isEmpty()&val<=k) {
					Query q = subQ.remove(0);
					val2=((AbstractQuery)q).isFailingNb(s,k);
					val*=val2;
				}
				executedQueries.put(this, val);
			}
		}
		return val>k;
	}
	
	/**
	 * decompose cartesian products and fill the decomp field with disjoined sets of triple patterns
	 * @return
	 */
	public void decomposePC(){
		int i =0;
		Query initQuery=factory.createQuery(rdfQuery);
		List<TriplePattern> triples= new ArrayList<TriplePattern>();
		List<String> vars= new ArrayList<String>();
		List<String> oldVars= new ArrayList<String>();
		while (!initQuery.isTheEmptyQuery()) {
			TriplePattern t = initQuery.getTriplePatterns().get(0);
			decomp.add(new HashSet<TriplePattern>());
			decomp.get(i).add(t);
			initQuery.removeTriplePattern(t);
			for (String v : t.getVariables()) {
				vars.add(v);
			}
			while (!vars.isEmpty()) {
				String v2=vars.remove(0);
				for (TriplePattern tp : initQuery.getTriplePatterns()) {
					if (tp.getVariables().contains(v2)) {
						triples.add(tp);
					}
				}
				oldVars.add(v2);
				while (!triples.isEmpty()) {
					TriplePattern tp=triples.remove(0);
					decomp.get(i).add(tp);
					initQuery.removeTriplePattern(tp);
					for (String v3:tp.getVariables()) {
						if (!vars.contains(v3)&&!oldVars.contains(v3)) {
							vars.add(v3);
						}
					}
				}
			}
			i++;
		}
	}

	@Override
	public void runBaseline(Session session, int k) {
		allMFIS = new HashSet<Query>();
		allXSS = new HashSet<Query>();
		session.clearExecutedQueryCount();
		initialQuery = this;
		List<Query> listQuery = new ArrayList<Query>();
		Map<Query, Boolean> executedQueries = new HashMap<Query, Boolean>();
		Map<Query, Boolean> markedQueries = new HashMap<Query, Boolean>();
		Map<Query, Boolean> listFIS = new HashMap<Query, Boolean>();
		markedQueries.put(this, true);
		listQuery.add(this);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			List<Query> subqueries = qTemp.getSubQueries();
			List<Query> superqueries = qTemp.getSuperQueries();
			if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
				boolean isAnFIS = true;
				while (isAnFIS && !superqueries.isEmpty()) {
					Query superquery = superqueries.remove(0);
					if (!listFIS.containsKey(superquery)) {
						isAnFIS = false;
					}
				}
                if (isAnFIS) {
                	for (Query fis : listFIS.keySet()) {
                		if (((AbstractQuery)fis).includesSimple(qTemp)) {
                			allMFIS.remove(fis);
                		}
                	}
                	listFIS.put(qTemp, true);
                	allMFIS.add(qTemp);
                }
			} else { // Potential XSS

				boolean isXSS = true;
				while (isXSS && !superqueries.isEmpty()) {
					Query superquery = superqueries.remove(0);
					if (!listFIS.containsKey(superquery)) {
						isXSS = false;
					}
				}
				if (isXSS && !qTemp.isTheEmptyQuery())
					allXSS.add(qTemp);
			}
			for (Query subquery : subqueries) {
				if (!markedQueries.containsKey(subquery)) {
					markedQueries.put(subquery, true);
					listQuery.add(subquery);
				}
			}	
			
		}
	}

	@Override
	public void runBFS(Session session, int k) {
		allMFIS = new HashSet<Query>();
		allXSS = new HashSet<Query>();
		session.clearExecutedQueryCount();
		initialQuery = this;
		List<Query> listQuery = new ArrayList<Query>();
		Map<Query, Integer> executedQueries = new HashMap<Query, Integer>();
		Map<Query, Boolean> markedQueries = new HashMap<Query, Boolean>();
		Map<Query, Boolean> listFIS = new HashMap<Query, Boolean>();
		markedQueries.put(this, true);
		listQuery.add(this);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			List<Query> superqueries = qTemp.getSuperQueries();
			boolean parentsFIS = true;
			while (parentsFIS && !superqueries.isEmpty()) {
				Query superquery = superqueries.remove(0);
				if (!listFIS.containsKey(superquery)) {
					parentsFIS = false;
				}
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of qTemp are FISs
			if (parentsFIS) {
				if (((AbstractQuery) qTemp).isFailingNb(executedQueries, session, k)) {
					// FIS
                	for (Query fis : listFIS.keySet()) {
                		if (((AbstractQuery)fis).includesSimple(qTemp)) {
                			allMFIS.remove(fis);
                		}
                	}
                	listFIS.put(qTemp, true);
                	allMFIS.add(qTemp);
                	List<Query> subqueries = qTemp.getSubQueries(); // we only study subqueries of FISs
    				for (Query subquery : subqueries) {
    					if (!markedQueries.containsKey(subquery)) {
    						markedQueries.put(subquery, true);
    						listQuery.add(subquery);
    					}
    				}
                }
				else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}
	
	/**
     * Uses filtering conditions (only one variable) to find Qbase starting point and fills Qbase
     * 
     * @param session connection to the KB
     * @throws Exception 
     */
	public void findQbaseGeneral(Session instance) throws Exception {
		baseQuery=(AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		for (TriplePattern t : this.getTriplePatterns()) {
			if (!(t.getVariables().size()==1))
				baseQuery.removeTriplePattern(t);
		}
	}

	@Override
	public void findQbase(Session instance) throws Exception {
		ComputeCardinalitiesConfig c =new ComputeCardinalitiesConfig();
		c.computeMaxCardinalities(this);
		baseQuery=(AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		for (TriplePattern t : this.getTriplePatterns()) {
			//if (t.getCardMax() > 1 && t.isObjectVariable())
			if (!t.getCardMax1() && t.isObjectVariable())
				baseQuery.removeTriplePattern(t);
		}
	}
	
	@Override
	public void findQbaseLocal(Session instance) throws Exception {
		ComputeCardinalitiesConfig c =new ComputeCardinalitiesConfig();
		Query currentQuery=(AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		baseQuery = (AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		do {
			currentQuery = (AbstractQuery) factory.createQuery(baseQuery.toString(),initialQuery);
			c.computeDomains(currentQuery);
			c.computeMaxLocalCardinalities(currentQuery); // fills maxCard with cardinality in the domain of currentQuery
			for (TriplePattern t : currentQuery.getTriplePatterns()) {
				if (!t.getCardMax1() && t.isObjectVariable()) {
					baseQuery.removeTriplePattern(t);
				}
			}
		} while (!baseQuery.equals(currentQuery));
	}
	
	public void findQbaseCS(Session instance) throws Exception {
		ComputeCardinalitiesConfig c =new ComputeCardinalitiesConfig();
		Query currentQuery=(AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		baseQuery = (AbstractQuery) factory.createQuery(rdfQuery,initialQuery);
		do {
			currentQuery = (AbstractQuery) factory.createQuery(baseQuery.toString(),initialQuery);
			c.computeMaxCSCardinalities(currentQuery); // fills maxCard with cardinality in the domain of currentQuery
			for (TriplePattern t : currentQuery.getTriplePatterns()) {
				if (!t.getCardMax1() && t.isObjectVariable()) {
					baseQuery.removeTriplePattern(t);
				}
			}
		} while (!baseQuery.equals(currentQuery));
	}
	
	@Override	
	public void runCardAlgo(Session session, int k) throws Exception {
		Set <Query> allMFISbase = new HashSet<Query>();
		allMFIS = new HashSet<Query>();
		allXSS = new HashSet<Query>();
		session.clearExecutedQueryCount();
		initialQuery = this;
		findQbaseGeneral(session);
		System.out.println("Qbase : "+baseQuery);
		List<Query> listQuery = new ArrayList<Query>();
		Map<Query, Boolean> executedQueries = new HashMap<Query, Boolean>();
		Map<Query, Boolean> markedQueries = new HashMap<Query, Boolean>();
		Map<Query, Boolean> listFIS = new HashMap<Query, Boolean>();
		markedQueries.put(this, true);
		listQuery.add(this);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			List<Query> superqueries = qTemp.getSuperQueries();
			boolean parentsFIS = true;
			while (parentsFIS && !superqueries.isEmpty()) {
				Query superquery = superqueries.remove(0);
				if (!listFIS.containsKey(superquery)) {
					parentsFIS = false;
				}
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of qTemp are FISs
			if (parentsFIS) {
				if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					// FIS
	            	for (Query fis : listFIS.keySet()) {
	            		if (((AbstractQuery)fis).includesSimple(qTemp)) {
	            			allMFISbase.remove(fis);
	            		}
	            	}
	            	listFIS.put(qTemp, true);
	            	allMFISbase.add(qTemp);
	            	List<Query> subqueries = qTemp.getBaseSubQueries(baseQuery); // we only study subqueries of FISs
					for (Query subquery : subqueries) {
						if (!markedQueries.containsKey(subquery)) {
							markedQueries.put(subquery, true);
							listQuery.add(subquery);
						}
					}
				
	            }
				else { // XSS
				if (!qTemp.isTheEmptyQuery())
					allXSS.add(qTemp);
				}
			}
		}
		//System.out.println(this.toString());
		for (Query mfisb:allMFISbase) {
			Query mfis = (AbstractQuery) factory.createQuery(mfisb.toString(),initialQuery);
			for (TriplePattern t: baseQuery.getTriplePatterns()) {
				mfis.removeTriplePattern(t);
			}
			allMFIS.add(mfis);
		}
	}


}