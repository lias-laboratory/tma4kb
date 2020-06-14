package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class JenaQueryHelper extends fr.ensma.lias.tma4kb.query.SPARQLQueryHelper {

	/**
	 * Creates a JenaQueryHelper for query q
	 * 
	 * @param q the query that this helper uses
	 */
	public JenaQueryHelper(fr.ensma.lias.tma4kb.query.Query q, int method) {
		super(q);
		this.method = method;
	}

	private final int SELECT_ALL = 0;
	private final int SELECT_K = 1;
	private final int COUNT = 2;
	private final int LIMIT = 3;
	private final int LIMITCOUNT = 4;
	private int method;

	@Override
	public int executeQuery(Session session, int k) {
		int i = 0;
		String sparqlQueryString = q.toString();
		long time = System.currentTimeMillis();

		if (method == SELECT_ALL) {
			org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());
			
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				i++;
				results.next();
			}
			qexec.close();
		} else if (method == SELECT_K) {
			org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());
			
			ResultSet results = qexec.execSelect();
			while (results.hasNext() && i <= k + 1) {
				i++;
				results.next();
			}
			qexec.close();
		} else if (method == COUNT) {
			sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) ");
			org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());
			
			ResultSet results = qexec.execSelect();
			QuerySolution sol = results.next();
			i = (int) sol.get("count").asLiteral().getValue();
			
			qexec.close();
		} else if (method == LIMIT) {
			int limit = k + 1;
			sparqlQueryString = sparqlQueryString.replace("} ", "} LIMIT " + limit);
			org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				i++;
				results.next();
			}

			qexec.close();
		} else if (method == LIMITCOUNT) {
			int limit = k + 1;
			sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) ");
			sparqlQueryString = sparqlQueryString.replace("} ", "} LIMIT " + limit);
			org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
			QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

			ResultSet results = qexec.execSelect();
			QuerySolution sol = results.next();
			i = (int) sol.get("count").asLiteral().getValue();
			
			qexec.close();
		}
		long end = System.currentTimeMillis();
		float tps = ((float) (end - time));
		session.setExecutedQueryCount(session.getExecutedQueryCount() + 1);
		session.setCountQueryTime(session.getCountQueryTime() + tps);

		return i;
	}
}
