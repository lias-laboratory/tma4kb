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
	

	protected final int SELECT_ALL = 0;
	protected final int SELECT_K = 1;
	protected final int COUNT = 2;
	protected final int LIMIT = 3;
	protected final int LIMITCOUNT = 4;
	protected int method;

	/***
	 * Creates a SPARQLQueryHelper with query q
	 * 
	 * @param q the query
	 */
	public SPARQLQueryHelper(Query q, int method) {
		this.q = q;
		this.method = method;
	}
	
	@Override
	public int executeQuery(Session session, int k) {
		int i = 0;
		long time = System.currentTimeMillis();

		if (method == SELECT_ALL) {
			try {
				i=execute_ALL(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (method == SELECT_K) {
			try {
				i=execute_STOPK(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (method == COUNT) {
			try {
				i=execute_COUNT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (method == LIMIT) {
			try {
				i=execute_LIMIT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (method == LIMITCOUNT) {
			try {
				i=execute_COUNTLIMIT(session, k);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		float tps = ((float) (end - time));
		session.setExecutedQueryCount(session.getExecutedQueryCount() + 1);
		session.setCountQueryTime(session.getCountQueryTime() + tps);

		return i;
	}

}
