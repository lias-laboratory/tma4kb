package fr.ensma.lias.tma4kb.triplestore.jenatdb.sparqlendpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.JenaSession;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class FusekiQueryHelper extends fr.ensma.lias.tma4kb.query.SPARQLQueryHelper {

	/**
	 * Creates a JenaQueryHelper for query q
	 * 
	 * @param q the query that this helper uses
	 */
	public FusekiQueryHelper(fr.ensma.lias.tma4kb.query.Query q, int method) {
		super(q);
		this.method = method;
	}

	private final int SELECT_ALL = 0;
	private final int SELECT_K = 1;
	private final int COUNT = 2;
	private final int LIMIT = 3;
	private final int LIMITCOUNT = 4;
	private int method;

	public int executeFuseki(Session session, int k) throws MalformedURLException, ProtocolException, IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		long time = System.currentTimeMillis();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();

		if (method == SELECT_ALL) {
			String query = fusekiSession.query(sparqlQueryString);

			String[] split = query.split("\n");
			i = split.length - 1;

		} else if (method == SELECT_K) {
			String query = fusekiSession.query(sparqlQueryString);

			String[] split = query.split("\n");
			while (i + 1 < split.length && i < k + 1) {
				i++;
			}

		} else if (method == COUNT) {
			sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) ");
			String query = fusekiSession.query(sparqlQueryString);

			int pos = query.indexOf("\n");
			int pos2 = query.indexOf("\n", pos + 1);
			String nbAns = query.substring(pos + 1, pos2 - 1);
			if (nbAns.length() == 1) {
				i = Character.getNumericValue(nbAns.charAt(0));
			} else {
				i = Integer.parseInt(nbAns);
			}

		} else if (method == LIMIT) {
			int limit = k + 1;
			sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit);
			String query = fusekiSession.query(sparqlQueryString);

			String[] split = query.split("\n");
			i = split.length - 1;

		} else if (method == LIMITCOUNT) {
			int limit = k + 1;
			sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) WHERE {SELECT * ");
			sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit + " }");
			String query = fusekiSession.query(sparqlQueryString);

			int pos = query.indexOf("\n");
			int pos2 = query.indexOf("\n", pos + 1);
			String nbAns = query.substring(pos + 1, pos2 - 1);
			if (nbAns.length() < 2) {
				i = Character.getNumericValue(nbAns.charAt(0));
			} else {
				i = Integer.parseInt(nbAns);
			}

		}
		long end = System.currentTimeMillis();
		float tps = ((float) (end - time));
		session.setCountQueryTime(session.getCountQueryTime() + tps);

		return i;
	}

	@Override
	public int executeQuery(Session session, int k) {
		// TODO Auto-generated method stub
		return 0;
	}
}