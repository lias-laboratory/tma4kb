package fr.ensma.lias.tma4kb.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ensma.lias.tma4kb.query.algorithms.Algorithm;
import fr.ensma.lias.tma4kb.query.algorithms.Baseline;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Ibrahim DELLAL (ibrahim.dellal@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractQuery implements Query {

	/**
	 * Factory to create other queries
	 */
	private QueryFactory factory;

	/**
	 * String of this query
	 */
	private String rdfQuery;

	/**
	 * List of triple patterns of this query
	 */
	private List<TriplePattern> triplePatterns;

	/**
	 * Number of triple patterns of this query (this could be computed)
	 */
	private int nbTriplePatterns;

	/**
	 * List of the MFIS of this query
	 */
	private Set<Query> allMFIS = new HashSet<>();
	/**
	 * List of the XSS of this query
	 */
	private Set<Query> allXSS = new HashSet<>();

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
	 * Algorithm used to compute the MFIS and XSS
	 */
	private Algorithm algo;
	

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
		this.algo=new Baseline();
	}

	@Override
	public QueryFactory getFactory() {
		return factory;
	}
	
	@Override
	public void setAlgorithm (Algorithm alg) {
		algo = alg;
	}
	
	
	/**
	 * Decompose a SPARQL Query into a set of triple patterns.
	 */
	private void decomposeQuery() {
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

	
	@Override
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

	@Override
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

	@Override
	public Integer nbResults(Map<Query, Integer> executedQueries, Session s, int k) {
		Integer val = executedQueries.get(this);
		if (val == null) {
			if (this.isTheEmptyQuery()) {
				executedQueries.put(this, 1);
				return 1;
			}
			decomposeCP();
			if (decomp.size() == 1) {
				val = nbResults(s, k);
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
						val2 = q.nbResults(s, k);
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
	void decomposeCP() {
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
	
	public Query findAnMFS(Session session, Map<Query, Integer> executedQueries) {
		Query q1=factory.createQuery("");
		Query q2=factory.createQuery(rdfQuery);
		for (TriplePattern tp : getTriplePatterns()) {
			q2.removeTriplePattern(tp);
			Query q3=factory.createQuery(q2.toString());
			for (TriplePattern t:q1.getTriplePatterns()) {
				q3.addTriplePattern(t);
			}
			if (q3.nbResults(executedQueries, session, 0)==0)
				q1.addTriplePattern(tp);
		}
		return q1;
	}
	
	public List<Query> computePotentialXSS(Query mfs) {
		List<Query> res = new ArrayList<Query>();
		if (nbTriplePatterns == 1)
			return res;
		for (TriplePattern t : mfs.getTriplePatterns()) {
			Query q = factory.createQuery(rdfQuery, this);
			q.removeTriplePattern(t);
			res.add(q);
		}
		return res;
	}
	
	@Override
	public void runAlgo (Session session, int k) {
		algo.runAlgo(session, k, this);
	}

	
	
}