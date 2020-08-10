package fr.ensma.lias.tma4kb.triplestore.jenatdb.sparqlendpoint;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 */
public class FusekiQueryHelper extends fr.ensma.lias.tma4kb.query.SPARQLQueryHelper {

	/**
	 * Creates a FusekiQueryHelper for query q
	 * 
	 * @param q the query that this helper uses
	 */
	public FusekiQueryHelper(fr.ensma.lias.tma4kb.query.Query q, int method) {
		super(q,method);
	}
	
	@Override
	public int execute_ALL(Session session, int k) throws IOException {

		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		String query = fusekiSession.query(sparqlQueryString);

		String[] split = query.split("\n");
		i = split.length - 1;
		return i;
	}
	@Override
	public int execute_STOPK(Session session, int k) throws IOException{
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		String query = fusekiSession.query(sparqlQueryString);

		String[] split = query.split("\n");
		while (i + 1 < split.length && i < k + 1) {
			i++;
		}
		return i;
	}
	@Override
	public int execute_COUNT(Session session, int k) throws IOException{
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
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
		return i;
	}
	@Override
	public int execute_LIMIT(Session session, int k) throws IOException{
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit);
		String query = fusekiSession.query(sparqlQueryString);

		String[] split = query.split("\n");
		i = split.length - 1;
		return i;
	}
	@Override
	public int execute_COUNTLIMIT(Session session, int k) throws IOException{
		int i = 0;
		String sparqlQueryString = q.toString();
		SPARQLEndpointClient fusekiSession = ((SPARQLEndpointSession) session).getSPARQLEndpointClient();
		int limit = k + 1;
		sparqlQueryString = sparqlQueryString.replace("SELECT * ", "SELECT (COUNT(*) as ?count) WHERE {SELECT * ");
		sparqlQueryString = sparqlQueryString.replace("}", "} LIMIT " + limit + " }");
		String query = fusekiSession.query(sparqlQueryString);

		int pos = query.indexOf("\n");
		int pos2 = query.indexOf("\n", pos + 1);
		String nbAns = query.substring(pos + 1, query.length()-1);
		if (nbAns.length() == 1) {
			i = Character.getNumericValue(nbAns.charAt(0));
		} else {
			i = Integer.parseInt(nbAns);
		}
		return i;
	}

}