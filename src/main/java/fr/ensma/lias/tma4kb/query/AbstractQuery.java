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
	 * newInitialQuery represents the query on which algorithms was executed
	 */
	protected Query initialQuery;

	/**
	 * Builds a query from its string and a reference to its factory
	 * 
	 * @param factory a factory to create some queries
	 * @param query the string of this query
	 */
	public AbstractQuery(QueryFactory factory, String query) {
		this.factory = factory;
		this.rdfQuery = query;
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
			return false;
		return isFailingAux(session, k);
	}

	protected abstract boolean isFailingAux(Session session, int k);

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

	protected String toSimpleString(Query initialQuery) {
		TriplePattern temp;
		String res = "";
		for (int i = 0; i < triplePatterns.size(); i++) {
			if (i > 0)
				res += " ^ ";
			temp = triplePatterns.get(i);
			if (initialQuery != null)
				res += "t" + (initialQuery.getTriplePatterns().indexOf(temp) + 1);
		}
		return res;
	}

	public List<Query> getSubQueries() {
		List<Query> res = new ArrayList<Query>();
		for (TriplePattern tp : getTriplePatterns()) {
			Query qNew = factory.createQuery(toString(), initialQuery);
			qNew.removeTriplePattern(tp);
			res.add(qNew);
		}
		return res;
	}

	public List<Query> getSuperQueries() {
		List<Query> res = new ArrayList<Query>();
		for (TriplePattern tp : initialQuery.getTriplePatterns()) {
			if (!includes(tp)) {
				Query qNew = factory.createQuery(toString());
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
	 * 
	 * @param executedQueries a cache of already executed queries
	 * @param s connection to the KB
	 * @return true iff this query is failing
	 */
	private boolean isFailingForDFS(Map<Query, Boolean> executedQueries, Session s, int k) {
		if (this.equals(this.getInitialQuery())) {
			executedQueries.put(this, true);
			return true;
		}
		Boolean val = executedQueries.get(this);
		if (val == null) {
			val = isFailing(s, k);
			//System.out.println("Execute " + (this.toSimpleString(initialQuery)));
			executedQueries.put(this, val);
		}
		return val;
	}

	@Override
	public void runBaseline(Session session, int k) {
		//System.out.println("===================== RUN Baseline ==================");
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
			//System.out.println("Process " + (((AbstractQuery)
			 //qTemp).toSimpleString(initialQuery)));
			List<Query> subqueries = qTemp.getSubQueries();
			List<Query> superqueries = qTemp.getSuperQueries();
			if (((AbstractQuery) qTemp).isFailingForDFS(executedQueries, session, k)) {
				// this is a potential MFS
				// System.out.println("potential mfs");
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
				for (Query superquery : superqueries) {
					if (!((AbstractQuery) superquery).isFailingForDFS(executedQueries, session, k))
						isXSS = false;
				}
				if (isXSS && !qTemp.isTheEmptyQuery())
					allXSS.add(qTemp);
			}
			for (Query subquery : subqueries) {
				if (!markedQueries.containsKey(subquery)) {
					markedQueries.put(subquery, true);
					// System.out.println("Add " +
					// ((AbstractQuery)subquery).toSimpleString(initialQuery));
					listQuery.add(subquery);
				}
			}	
			
		}
	}
	@Override
	public void runBFS(Session session, int k) {
		//System.out.println("===================== RUN BFS ==================");
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
			//System.out.println("Process " + (((AbstractQuery)
			 //qTemp).toSimpleString(initialQuery)));
			List<Query> subqueries = qTemp.getSubQueries();
			List<Query> superqueries = qTemp.getSuperQueries();
			if (((AbstractQuery) qTemp).isFailingForDFS(executedQueries, session, k)) {
				// this is a potential MFS
				// System.out.println("potential mfs");
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
				for (Query superquery : superqueries) {
					if (!((AbstractQuery) superquery).isFailingForDFS(executedQueries, session, k))
						isXSS = false;
				}
				if (isXSS && !qTemp.isTheEmptyQuery())
					allXSS.add(qTemp);
				for (Query subquery : subqueries) {
					if (executedQueries.get(subquery)==null) {
						// We are not interested in any queries that have a succeeding superquery.
						// We put that any such query succeeds (this may not be the case) to avoid executing them. 
						executedQueries.put(subquery, false); 
						//System.out.println("Avoid " +
							//((AbstractQuery)subquery).toSimpleString(initialQuery));
					}
				}
			}
			for (Query subquery : subqueries) {
				if (!markedQueries.containsKey(subquery)) {
					markedQueries.put(subquery, true);
					//System.out.println("Add " +
					//((AbstractQuery)subquery).toSimpleString(initialQuery));
					listQuery.add(subquery);
				}
			}
			
		}
	}
}