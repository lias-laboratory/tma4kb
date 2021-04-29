package fr.ensma.lias.tma4kb.query.algorithms;

import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.AbstractSession.Counters;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class BFS extends Baseline{
	

	protected void calculateCard(Query qTemp) {};
		
	protected void useProperties(TriplePattern tp, Query qNew, Query qTemp, int k, int Nb) {}
	
	@Override
	public void runAlgo(Session session, int k, Query initialQuery) {
		initialiseAlgo(session);
		listQuery.add(initialQuery);
		initialQuery.setInitialQuery(initialQuery);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			calculateCard(qTemp);
			if (parentsFIS(session,qTemp)) {
				int Nb = ((AbstractQuery) qTemp).nbResults(executedQueries, session, k);
				if (Nb>k) {//((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					// FIS
					long time1 = System.currentTimeMillis();
					for (Query fis : listFIS.keySet()) {
						if (fis.includesSimple(qTemp)) {
							initialQuery.getAllMFIS().remove(fis);
						}
					}
					listFIS.put(qTemp, true);
					initialQuery.getAllMFIS().add(qTemp);
					long time2 = System.currentTimeMillis();
					List<Query> subqueries = new ArrayList<Query>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = qTemp.getFactory().createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						useProperties(tp,qNew,qTemp,k,Nb);
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
						initialQuery.getAllXSS().add(qTemp);
				}
			}
		}
	}
}
