package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class JenaQuery extends AbstractQuery {

	/**
	 * The helper that will execute the queries
	 */
	private JenaQueryHelper helper;

	/**
	 * Builds a JenaQuery, using the constructor of AbstractQuery, and initialises
	 * the helper
	 * 
	 * @param factory
	 * @param query
	 * @param method
	 */
	public JenaQuery(QueryFactory factory, String query, QueryMethod method) {
		super(factory, query);
		helper = new JenaQueryHelper(this, method);

	}

	@Override
	public int nbResults(Session session, int k) {
		return helper.executeQuery(session, k);

	}

}
