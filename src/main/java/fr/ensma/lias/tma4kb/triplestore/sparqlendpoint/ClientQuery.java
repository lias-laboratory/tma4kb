package fr.ensma.lias.tma4kb.triplestore.sparqlendpoint;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * 
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 *
 */

public class ClientQuery extends AbstractQuery {

	/**
	 * The helper that will execute the queries
	 */
	private ClientQueryHelper helper;

	/**
	 * Builds a ClientQuery, using the constructor of AbstractQuery, and initialises
	 * the helper
	 * 
	 * @param factory
	 * @param query
	 * @param method
	 */
	public ClientQuery(QueryFactory factory, String query, QueryMethod method) {
		super(factory, query);
		helper = new ClientQueryHelper(this, method);

	}

	@Override
	public int isFailing(Session session, int k) {
		return helper.executeQuery(session, k);

	}
}