package fr.ensma.lias.tma4kb.query.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.AbstractSession.Counters;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;

public class Baseline implements Algorithm{
	

	protected List<Query> listQuery;
	protected Map<Query, Integer> executedQueries;
	protected Map<Query, Boolean> listFIS;
	
	protected void initialiseAlgo(Session session) {
		long time1 = System.currentTimeMillis();
		session.clearExecutedQueryCount();
		session.clearCountQueryTime();
		listQuery = new ArrayList<>();
		executedQueries = new HashMap<>();
		listFIS = new HashMap<>();
		long time4 = System.currentTimeMillis();
		session.addTimes(time4 - time1, Counters.initialisation);
	}
	

	protected boolean parentsFIS(Session session, Query qTemp) {
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
	public void runAlgo(Session session, int k, Query initialQuery) {
		initialiseAlgo(session);
		listQuery.add(initialQuery);
		initialQuery.setInitialQuery(initialQuery);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			int Nb = ((AbstractQuery) qTemp).nbResults(executedQueries, session, k);
			if (Nb>k) {//if (((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
				if (parentsFIS(session,qTemp)) {
					long time1 = System.currentTimeMillis();
					for (Query fis : listFIS.keySet()) {
						if (fis.includesSimple(qTemp)) {
							initialQuery.getAllMFIS().remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					initialQuery.getAllMFIS().add(qTemp);
					long time2 = System.currentTimeMillis();
					session.addTimes(time2 - time1, Counters.updateFIS);
				}
			} else { // Potential XSS
				if (parentsFIS(session,qTemp) && !qTemp.isTheEmptyQuery())
					initialQuery.getAllXSS().add(qTemp);
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
}
