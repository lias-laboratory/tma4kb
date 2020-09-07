package fr.ensma.lias.tma4kb.query;

import java.io.IOException;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Stephane JEAN (jean@ensma.fr)
 */
public abstract class SPARQLQueryHelper implements QueryHelper {

	/**
	 * The query that this helper uses
	 */
	protected Query q;

	protected QueryMethod method;

	/***
	 * Creates a SPARQLQueryHelper with query q
	 * 
	 * @param q      the query
	 * @param method
	 */
	public SPARQLQueryHelper(Query q, QueryMethod method) {
		this.q = q;
		this.method = method;
	}

	public enum QueryMethod {
		all, stopK, count, limit, countlimit;

	}

	@Override
	public int executeQuery(Session session, int k) {
		int i = 0;
		long time = System.currentTimeMillis();
		switch (method) {
		case all:
			try {
				i = execute_ALL(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case stopK:
			try {
				i = execute_STOPK(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case count:
			try {
				i = execute_COUNT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case limit:
			try {
				i = execute_LIMIT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case countlimit:
			try {
				i = execute_COUNTLIMIT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		long end = System.currentTimeMillis();
		float tps = ((float) (end - time));
		session.setExecutedQueryCount(session.getExecutedQueryCount() + 1);
		session.setCountQueryTime(session.getCountQueryTime() + tps);

		return i;
	}

}
