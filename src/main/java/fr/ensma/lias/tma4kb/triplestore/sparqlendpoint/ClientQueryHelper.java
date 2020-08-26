package fr.ensma.lias.tma4kb.triplestore.sparqlendpoint;

import java.io.IOException;

import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 */
public class ClientQueryHelper extends fr.ensma.lias.tma4kb.query.SPARQLQueryHelper {

	/**
	 * Creates a ClientQueryHelper for query q
	 * 
	 * @param q the query that this helper uses
	 */
	public ClientQueryHelper(fr.ensma.lias.tma4kb.query.Query q, QueryMethod method) {
		super(q, method);
	}

	@Override
	public int execute_ALL(Session session, int k) throws IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient clientsession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		String query = clientsession.query(sparqlQueryString);

		String[] split = query.split("\n");
		i = split.length - 1;
		return i;
	}

	@Override
	public int execute_STOPK(Session session, int k) throws IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient clientsession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		String query = clientsession.query(sparqlQueryString);

		String[] split = query.split("\n");
		while (i + 1 < split.length && i < k + 1) {
			i++;
		}
		return i;
	}

	@Override
	public int execute_COUNT(Session session, int k) throws IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient clientsession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) ");
		String query = clientsession.query(sparqlQueryString);

		int pos = query.indexOf("\n");
		int pos2 = 0;
		for (int r = pos + 1; r < query.length(); r++) {
			char t = query.charAt(r);
			if (t == '0' || t == '1' || t == '2' || t == '3' || t == '4' || t == '5' || t == '6' || t == '7' || t == '8'
					|| t == '9') {
				pos2 = r + 1;
			}
		}
		String nbAns = query.substring(pos + 1, pos2);
		if (nbAns.length() == 1) {
			i = Character.getNumericValue(nbAns.charAt(0));
		} else {
			i = Integer.parseInt(nbAns);
		}
		return i;
	}

	@Override
	public int execute_LIMIT(Session session, int k) throws IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient clientsession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit);
		String query = clientsession.query(sparqlQueryString);

		String[] split = query.split("\n");
		i = split.length - 1;
		return i;
	}

	@Override
	public int execute_COUNTLIMIT(Session session, int k) throws IOException {
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient clientsession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) WHERE { {SELECT * ");
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit + " } }");
		String query = clientsession.query(sparqlQueryString);

		int pos = query.indexOf("\n");
		int pos2 = 0;
		for (int r = pos + 1; r < query.length(); r++) {
			char t = query.charAt(r);
			if (t == '0' || t == '1' || t == '2' || t == '3' || t == '4' || t == '5' || t == '6' || t == '7' || t == '8'
					|| t == '9') {
				pos2 = r + 1;
			}
		}
		String nbAns = query.substring(pos + 1, pos2);
		if (nbAns.length() == 1) {
			i = Character.getNumericValue(nbAns.charAt(0));
		} else {
			i = Integer.parseInt(nbAns);
		}
		return i;
	}

}