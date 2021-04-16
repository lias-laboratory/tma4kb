package fr.ensma.lias.tma4kb.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.query.AbstractSession.Counters;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Ibrahim DELLAL (ibrahim.dellal@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractQuery implements Query {

	/**
	 * Factory to create other queries
	 */
	protected QueryFactory factory;

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
	private Query initialQuery;

	/**
	 * decomposition of the query into sets of triples so that each variable appears
	 * in only one set decomp is used to avoid executing cartesian products
	 */
	protected List<Set<TriplePattern>> decomp;

	/**
	 * Builds a query from its string and a reference to its factory
	 * 
	 * @param factory a factory to create some queries
	 * @param query   the string of this query
	 */
	public AbstractQuery(QueryFactory factory, String query) {
		this.factory = factory;
		this.rdfQuery = query;
		this.decomp = new ArrayList<>();
		this.decomposeQuery();
		nbTriplePatterns = triplePatterns.size();
	}

	@Override
	public QueryFactory getFactory() {
		return factory;
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
		if (other.nbTriplePatterns != this.nbTriplePatterns) // same number of triple patterns
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

	@Override
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
		List<Query> res = new ArrayList<>();
		for (TriplePattern tp : getTriplePatterns()) {
			Query qNew = factory.createQuery(toString(), initialQuery);
			qNew.removeTriplePattern(tp);
			res.add(qNew);
		}
		return res;
	}

	/**
	 * Computes the direct superqueries of this query
	 * 
	 * @return the list of direct superqueries of the query
	 */
	public List<Query> getSuperQueries() {
		List<Query> res = new ArrayList<>();
		for (TriplePattern tp : initialQuery.getTriplePatterns()) {
			if (!includes(tp)) {
				Query qNew = factory.createQuery(toString(), initialQuery);
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
	 * Sets the maximum cardinality of triple
	 * 
	 * @param triple  the triple pattern
	 * @param cardMax the cardinality value
	 */
	public void setCardMax(int triple, int cardMax) {
		this.triplePatterns.get(triple).setCardMax(cardMax);
	}

	@Override
	public Set<String> getVariables() {
		Set<String> result = new HashSet<>();
		for (TriplePattern tp : this.getTriplePatterns())
			result.addAll(tp.getVariables());
		return result;
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
	 * Returns a boolean: true if the query fails (returns more than k results).
	 * Avoids executing Cartesian products by multiplying the number of results of
	 * the subqueries
	 * 
	 * @param executedQueries a cache of already executed queries associated with
	 *                        their nb of results
	 * @param s               connection to the KB
	 * @param k               the maximum number of results
	 * @return true iff this query is failing
	 */
	protected boolean isFailing(Map<Query, Integer> executedQueries, Session s, int k) {
		long start = System.currentTimeMillis();
		Integer val = executedQueries.get(this);
		if (val == null) {
			if (this.isTheEmptyQuery()) {
				executedQueries.put(this, 1);
				return true;
			}
			long time1 = System.currentTimeMillis();
			decomposeCP();
			long time2 = System.currentTimeMillis();
			s.addTimes(time2 - time1, Counters.decomposeCP);
			if (decomp.size() == 1) {
				val = isFailing(s, k);
				executedQueries.put(this, val);
			} else {
				List<Query> subQ = new ArrayList<>();
				for (Set<TriplePattern> set : decomp) {
					Query q = factory.createQuery("", initialQuery);
					for (TriplePattern t : set) {
						q.addTriplePattern(t);
					}
					subQ.add(q);
				}
				val = 1;
				Integer val2;
				while (!subQ.isEmpty() & val <= k) {
					Query q = subQ.remove(0);
					val2 = executedQueries.get(q);
					if (val2 == null) {
						val2 = q.isFailing(s, k);
						executedQueries.put(q, val2);
					}
					val *= val2;
				}
				executedQueries.put(this, val);
			}
		}
		long end = System.currentTimeMillis();
		s.addTimes(end - start, Counters.isFailing);
		return val > k;
	}

	protected Integer isFailingNb(Map<Query, Integer> executedQueries, Session s, int k) {
		Integer val = executedQueries.get(this);
		if (val == null) {
			if (this.isTheEmptyQuery()) {
				executedQueries.put(this, k + 1);
				return k + 1;
			}
			decomposeCP();
			if (decomp.size() == 1) {
				val = isFailing(s, k);
				executedQueries.put(this, val);
			} else {
				List<Query> subQ = new ArrayList<>();
				for (Set<TriplePattern> set : decomp) {
					Query q = factory.createQuery("", initialQuery);
					for (TriplePattern t : set) {
						q.addTriplePattern(t);
					}
					subQ.add(q);
				}
				val = 1;
				Integer val2;
				while (!subQ.isEmpty() && val <= k) {
					Query q = subQ.remove(0);
					val2 = executedQueries.get(q);
					if (val2 == null) {
						val2 = q.isFailing(s, k);
						executedQueries.put(q, val2);
					}
					val *= val2;
				}
				executedQueries.put(this, val);
			}
		}
		return val;
	}

	/**
	 * decompose Cartesian products and fill the decomp field with separate sets of
	 * triple patterns
	 * 
	 * @return
	 */
	public void decomposeCP() {
		int i = 0;
		Query initQuery = factory.createQuery(rdfQuery);
		List<TriplePattern> triples = new ArrayList<>();
		List<String> vars = new ArrayList<>();
		List<String> oldVars = new ArrayList<>();
		while (!initQuery.isTheEmptyQuery()) {
			TriplePattern t = initQuery.getTriplePatterns().get(0);
			decomp.add(new HashSet<>());
			decomp.get(i).add(t);
			initQuery.removeTriplePattern(t);
			for (String v : t.getVariables()) {
				vars.add(v);
			}
			while (!vars.isEmpty()) {
				String v2 = vars.remove(0);
				for (TriplePattern tp : initQuery.getTriplePatterns()) {
					if (tp.getVariables().contains(v2)) {
						triples.add(tp);
					}
				}
				oldVars.add(v2);
				while (!triples.isEmpty()) {
					TriplePattern tp = triples.remove(0);
					decomp.get(i).add(tp);
					initQuery.removeTriplePattern(tp);
					for (String v3 : tp.getVariables()) {
						if (!vars.contains(v3) && !oldVars.contains(v3)) {
							vars.add(v3);
						}
					}
				}
			}
			i++;
		}
	}
	
	
	/**
	 * Variables for the algorithms
	 */
	private List<Query> listQuery = new ArrayList<>();
	private Map<Query, Integer> executedQueries = new HashMap<>();
	private Map<Query, Boolean> listFIS = new HashMap<>();
	
	public void initialiseAlgo(Session session) {
		long time1 = System.currentTimeMillis();
		allMFIS = new HashSet<>();
		allXSS = new HashSet<>();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		initialQuery = this;
		listQuery = new ArrayList<>();
		executedQueries = new HashMap<>();
		listFIS = new HashMap<>();
		listQuery.add(this);
		long time4 = System.currentTimeMillis();
		session.addTimes(time4 - time1, Counters.initialisation);
	}
	
	public boolean parentsFIS(Session session, Query qTemp) {
		long time1 = System.currentTimeMillis();
		List<Query> superqueries = qTemp.getSuperQueries();
		long time2 = System.currentTimeMillis();
		session.addTimes(time2 - time1, Counters.getSuperQueries);
		boolean parentsFIS = true;
		while (parentsFIS && !superqueries.isEmpty()) {
			Query superquery = superqueries.remove(0);
			if (!listFIS.containsKey(superquery)) {
				parentsFIS = false;
			}
		} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
			// qTemp are FISs
		long time3 = System.currentTimeMillis();
		session.addTimes(time3 - time1, Counters.parentsFIS);
		return parentsFIS;
	}
	

	@Override
	public void runBase(Session session, int k) {
		initialiseAlgo(session);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
				if (parentsFIS(session,qTemp)) {
					long time1 = System.currentTimeMillis();
					//System.out.println("time1 "+time1);
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					long time2 = System.currentTimeMillis();
					//System.out.println("time2 "+time2);
					session.addTimes(time2 - time1, Counters.updateFIS);
				}
			} else { // Potential XSS
				if (parentsFIS(session,qTemp) && !qTemp.isTheEmptyQuery())
					allXSS.add(qTemp);
			}
			long time1 = System.currentTimeMillis();
			List<Query> subqueries = qTemp.getSubQueries();
			for (Query subquery : subqueries) {

				if (!listQuery.contains(subquery)) {
					listQuery.add(subquery);
				}
			}
			long time2 = System.currentTimeMillis();
			session.addTimes(time2 - time1, Counters.nextQueries);

		}
	}

	@Override
	public void runBFS(Session session, int k) {
		initialiseAlgo(session);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			if (parentsFIS(session,qTemp)) {
				if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					// FIS
					long time1 = System.currentTimeMillis();
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					long time2 = System.currentTimeMillis();
					List<Query> subqueries = qTemp.getSubQueries(); // we only study subqueries of FISs
					for (Query subquery : subqueries) {

						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
					long time3 = System.currentTimeMillis();
					session.addTimes(time2 - time1, Counters.updateFIS);
					session.addTimes(time3 - time2, Counters.nextQueries);
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}

	@Override
	public void runVar(Session session, int k) throws Exception {
		initialiseAlgo(session);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			if (parentsFIS(session,qTemp)) {
				if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					long time1 = System.currentTimeMillis();
					// FIS
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					long time2 = System.currentTimeMillis();
					List<Query> subqueries = new ArrayList<Query>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = factory.createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						long time4 = System.currentTimeMillis();
						if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
							executedQueries.put(qNew, k + 1);
						}
						long time5 = System.currentTimeMillis();
						session.addTimes(time5 - time4, Counters.varProp);
					}
					
					for (Query subquery : subqueries) {

						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
					long time3 = System.currentTimeMillis();
					session.addTimes(time2 - time1, Counters.updateFIS);
					session.addTimes(time3 - time2, Counters.nextQueries);
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}

	@Override
	public void runFull(Session session, int k, ComputeCardinalitiesConfig c) throws Exception {
		initialiseAlgo(session);
		long time1,time2,time3,time4=0;
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			time1 = System.currentTimeMillis();
			c.computeMaxCardinalities(qTemp);
			time2 = System.currentTimeMillis();
			session.addTimes(time2 - time1, Counters.computeCard);
			if (parentsFIS(session,qTemp)) {
				if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					time2 = System.currentTimeMillis();
					// FIS
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					time3 = System.currentTimeMillis();
					List<Query> subqueries = new ArrayList<>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = factory.createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						long time5 = System.currentTimeMillis();						
						if (!tp.isPredicateVariable() && tp.getCardMax() <= 1
								&& qNew.getVariables().contains(tp.getSubject())) { // cardinality property
							executedQueries.put(qNew, k + 1);
						}
						long time6 = System.currentTimeMillis();
						if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
							executedQueries.put(qNew, k + 1);
						}
						long time7 = System.currentTimeMillis();
						session.addTimes(time7 - time6, Counters.varProp);
						session.addTimes(time6 - time5, Counters.cardProp);
					}
					for (Query subquery : subqueries) {
						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
					time4 = System.currentTimeMillis();
					session.addTimes(time3 - time2, Counters.updateFIS);
					session.addTimes(time4 - time3, Counters.nextQueries);
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}

	@Override
	public void runFull_AnyCard(Session session, int k, ComputeCardinalitiesConfig c) throws Exception {
		allMFIS = new HashSet<>();
		allXSS = new HashSet<>();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		initialQuery = this;
		List<Query> listQuery = new ArrayList<>();
		Map<Query, Integer> executedQueries = new HashMap<>();
		Map<Query, Boolean> listFIS = new HashMap<>();
		listQuery.add(this);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			c.computeMaxCardinalities(qTemp);
			List<Query> superqueries = qTemp.getSuperQueries();
			boolean parentsFIS = true;
			while (parentsFIS && !superqueries.isEmpty()) {
				Query superquery = superqueries.remove(0);
				if (!listFIS.containsKey(superquery)) {
					parentsFIS = false;
				}
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
				// qTemp are FISs
			if (parentsFIS) {
				int Nb = ((AbstractQuery) qTemp).isFailingNb(executedQueries, session, k);
				if (Nb > k) {
					// FIS
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					List<Query> subqueries = new ArrayList<>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = factory.createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						if (!tp.isPredicateVariable() && Nb / tp.getCardMax() > k
								&& qNew.getVariables().contains(tp.getSubject())) { // cardinality property
							executedQueries.put(qNew, Nb / tp.getCardMax());
						}
						if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
							executedQueries.put(qNew, Nb);
						}
					}
					for (Query subquery : subqueries) {

						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}

	@Override
	public void runFull_Local(Session session, int k, ComputeCardinalitiesConfig c) throws Exception {
		allMFIS = new HashSet<>();
		allXSS = new HashSet<>();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		initialQuery = this;
		List<Query> listQuery = new ArrayList<>();
		Map<Query, Integer> executedQueries = new HashMap<>();
		Map<Query, Boolean> listFIS = new HashMap<>();
		listQuery.add(this);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			c.computeDomains(qTemp);
			c.computeMaxLocalCardinalities(qTemp);
			List<Query> superqueries = qTemp.getSuperQueries();
			boolean parentsFIS = true;
			while (parentsFIS && !superqueries.isEmpty()) {
				Query superquery = superqueries.remove(0);
				if (!listFIS.containsKey(superquery)) {
					parentsFIS = false;
				}
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
				// qTemp are FISs
			if (parentsFIS) {
				int Nb = ((AbstractQuery) qTemp).isFailingNb(executedQueries, session, k);
				if (Nb > k) {
					// FIS
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					List<Query> subqueries = new ArrayList<>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = factory.createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						if (!tp.isPredicateVariable() && Nb / tp.getCardMax() > k
								&& qNew.getVariables().contains(tp.getSubject())) { // cardinality property
							executedQueries.put(qNew, Nb / tp.getCardMax());
						}
						if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
							executedQueries.put(qNew, Nb);
						}
					}
					for (Query subquery : subqueries) {
						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}

	@Override
	public void runFull_CS(Session session, int k, ComputeCardinalitiesConfig c) throws Exception {
		allMFIS = new HashSet<>();
		allXSS = new HashSet<>();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		initialQuery = this;
		List<Query> listQuery = new ArrayList<>();
		Map<Query, Integer> executedQueries = new HashMap<>();
		Map<Query, Boolean> listFIS = new HashMap<>();
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
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
				// qTemp are FISs
			if (parentsFIS) {
				int Nb = ((AbstractQuery) qTemp).isFailingNb(executedQueries, session, k);
				if (Nb > k) {
					// FIS
					for (Query fis : listFIS.keySet()) {
						if (((AbstractQuery) fis).includesSimple(qTemp)) {
							allMFIS.remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					allMFIS.add(qTemp);
					List<Query> subqueries = new ArrayList<Query>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = factory.createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
							executedQueries.put(qNew, Nb);
						} else if (!tp.isPredicateVariable() && c.hasCard1(tp, qTemp)
								&& qNew.getVariables().contains(tp.getSubject())) { // cardinality property
							executedQueries.put(qNew, k + 1);
						}
					}
					for (Query subquery : subqueries) {
						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}
				} else { // XSS
					if (!qTemp.isTheEmptyQuery())
						allXSS.add(qTemp);
				}
			}
		}
	}
	
}