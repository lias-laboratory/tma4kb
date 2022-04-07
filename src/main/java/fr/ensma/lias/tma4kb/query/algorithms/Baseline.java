package fr.ensma.lias.tma4kb.query.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.AbstractSession.Counters;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class Baseline implements Algorithm {

	protected List<Query> listQuery;
	protected Map<Query, Integer> executedQueries;
	protected Map<Query, Boolean> listFIS;

	protected void initialiseAlgo(Session session) {
		long time1 = System.nanoTime();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		listQuery = new ArrayList<>();
		executedQueries = new HashMap<>();
		listFIS = new HashMap<>();
		long time4 = System.nanoTime();
		session.addTimes(time4 - time1, Counters.initialisation);
	}

	protected void makeLattice(Query initialQuery) {
		listQuery = new ArrayList<>();
		List<List<Query>> levels = new ArrayList<>();
		List<TriplePattern> S = initialQuery.getTriplePatterns();
		int n=S.size();
		for (int i =0;i<=n;i++) {
			levels.add(new ArrayList<>());
		}

		// `N` stores the total number of subsets
		long N = (long) Math.pow(2, n);

		// generate each subset one by one
		for (int i = 0; i < N; i++) {
			int count=0;
			Query qNew = initialQuery.getFactory().createQuery(initialQuery.toString(), initialQuery);
			// check every bit of `i`
			for (int j = 0; j < n; j++) {
				// if j'th bit of `i` is set, add `S[j]` to the current set
				if ((i & (1 << j)) != 0) {
					count++;
					qNew.removeTriplePattern(S.get(j));
				}
			}
			levels.get(count).add(qNew);
		}
		for (int i=0;i<=n;i++) {
			for (Query qNew : levels.get(i)) {
				listQuery.add(qNew);
			}
		}
	}

	protected boolean parentsFIS(Session session, Query qTemp) {
		long time1 = System.nanoTime();
		List<Query> superqueries = qTemp.getSuperQueries();
		long time2 = System.nanoTime();
		session.addTimes(time2 - time1, Counters.getSuperQueries);
		boolean parentsFIS = true;
		while (parentsFIS && !superqueries.isEmpty()) {
			Query superquery = superqueries.remove(0);
			if (!listFIS.containsKey(superquery)) {
				parentsFIS = false;
			}
		} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
			// qTemp are FISs
		long time3 = System.nanoTime();
		session.addTimes(time3 - time1, Counters.parentsFIS);
		return parentsFIS;
	}

	@Override
	public void runAlgo(Session session, int k, Query initialQuery) {
		initialiseAlgo(session);
		long time4 = System.nanoTime();
		makeLattice(initialQuery);
		long time3= System.nanoTime();
		session.addTimes(time3 - time4, Counters.makeLattice);
		initialQuery.setInitialQuery(initialQuery);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			int Nb = ((AbstractQuery) qTemp).nbResults(executedQueries, session, k);
			long time1 = System.nanoTime();
			List<Query> superqueries = qTemp.getSuperQueries();
			long time2 = System.nanoTime();
			session.addTimes(time2 - time1, Counters.getSuperQueries);
			boolean parentsFIS = true;
			int i=0;
			while (parentsFIS && i<superqueries.size()) {
				Query superquery = superqueries.get(i);
				if (!listFIS.containsKey(superquery)) {
					parentsFIS = false;
				}
				i++;
			} // at the end of the loop, parentsFIS=true, if and only if all superqueries of
				// qTemp are FISs
			time3 = System.nanoTime();
			session.addTimes(time3 - time2, Counters.parentsFIS);
			if (Nb > k) {// if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
				if (parentsFIS) {
					time1 = System.nanoTime();
					//superqueries = qTemp.getSuperQueries();
					for (Query q:superqueries) {
						initialQuery.getAllMFIS().remove(q);
					}
					listFIS.put(qTemp, true);
					initialQuery.getAllMFIS().add(qTemp);
					time2 = System.nanoTime();
					session.addTimes(time2 - time1, Counters.updateFIS);
				}
			} else { // Potential XSS
				if (parentsFIS && !qTemp.isTheEmptyQuery())
					initialQuery.getAllXSS().add(qTemp);
			}
		}
	}
}
