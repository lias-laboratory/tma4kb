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
		//listQuery.add(initialQuery);
		long time4 = System.nanoTime();
		makeLattice(initialQuery);

		long time3= System.nanoTime();
		session.addTimes(time3 - time4, Counters.makeLattice);
		initialQuery.setInitialQuery(initialQuery);
		while (!listQuery.isEmpty()) {
			Query qTemp = listQuery.remove(0);
			long time1 = System.nanoTime();
			calculateCard(qTemp);
			long time2 = System.nanoTime();
			session.addTimes(time2 - time1, Counters.computeCard);
			time1 = System.nanoTime();
			List<Query> superqueries = qTemp.getSuperQueries();
			time2 = System.nanoTime();
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
			if (parentsFIS) {
				int Nb = ((AbstractQuery) qTemp).nbResults(executedQueries, session, k);
				if (Nb>k) {//((AbstractQuery) qTemp).isFailing(executedQueries, session, k)) {
					// FIS
					time1 = System.nanoTime();
					for (Query q:superqueries) {
						initialQuery.getAllMFIS().remove(q);
					}
					listFIS.put(qTemp, true);
					initialQuery.getAllMFIS().add(qTemp);
					time2 = System.nanoTime();
					List<Query> subqueries = new ArrayList<Query>();
					for (TriplePattern tp : qTemp.getTriplePatterns()) {
						Query qNew = qTemp.getFactory().createQuery(qTemp.toString(), initialQuery);
						qNew.removeTriplePattern(tp);
						subqueries.add(qNew);
						time4 = System.nanoTime();
						useProperties(tp,qNew,qTemp,k,Nb);
						long time5 = System.nanoTime();
						session.addTimes(time5 - time4, Counters.properties);
					}
					/*long time4 = System.currentTimeMillis();
					for (Query q:listQuery) {
						subqueries.remove(q);
					}
					listQuery.addAll(subqueries);
					/*for (Query subquery : subqueries) {
						if (!listQuery.contains(subquery)) {
							listQuery.add(subquery);
						}
					}*/
					time3 = System.nanoTime();
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
