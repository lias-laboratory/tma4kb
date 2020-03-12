package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
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
	public JenaQueryHelper(fr.ensma.lias.tma4kb.query.Query q) {
		super(q);
	}

	@Override
	public int executeQuery(Session session, int k) {
		int i = 0;
		String sparqlQueryString = q.toString();
		org.apache.jena.query.Query query = org.apache.jena.query.QueryFactory.create(sparqlQueryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, ((JenaSession) session).getDataset());

		ResultSet results = qexec.execSelect();

		(session).setExecutedQueryCount((session).getExecutedQueryCount() + 1);
		while (results.hasNext()) {
			i++;
			results.next();
		}
		qexec.close();

		return i;
	}
}
