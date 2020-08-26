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
	public JenaQueryHelper(fr.ensma.lias.tma4kb.query.Query q, QueryMethod method) {
		super(q, method);
	}

	@Override
	public int execute_ALL(Session session, int k) {
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(q.toString());
		int i = 0;
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();
		while (results.hasNext()) {
			i++;
			results.next();
		}
		qexec.close();
		return i;
	}

	@Override
	public int execute_STOPK(Session session, int k) {
		int i = 0;
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(q.toString());
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();
		while (results.hasNext() && i < k + 1) {
			i++;
			results.next();
		}
		qexec.close();
		return i;
	}

	@Override
	public int execute_COUNT(Session session, int k) {
		int i = 0;
		String sparqlQueryString = q.toString();
		sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) ");
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();
		QuerySolution sol = results.next();
		i = (int) sol.get("count").asLiteral().getValue();

		qexec.close();
		return i;
	}

	@Override
	public int execute_LIMIT(Session session, int k) {
		int i = 0;
		String sparqlQueryString = q.toString();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit);
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();
		while (results.hasNext()) {
			i++;
			results.next();
		}

		qexec.close();
		return i;
	}

	@Override
	public int execute_COUNTLIMIT(Session session, int k) {

		int i = 0;
		String sparqlQueryString = q.toString();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) WHERE {SELECT * ");
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit + " }");
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();
		QuerySolution sol = results.next();
		i = (int) sol.get("count").asLiteral().getValue();

		qexec.close();
		return i;
	}

}
