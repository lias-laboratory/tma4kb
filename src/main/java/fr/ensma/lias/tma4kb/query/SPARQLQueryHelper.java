package fr.ensma.lias.tma4kb.query;


public abstract class SPARQLQueryHelper implements QueryHelper {
	
	/**
	 * The query that this helper uses
	 */
	protected Query q;

	/***
	 * Creates a SPARQLQueryHelper with query q
	 * @param q the query
	 */
	public SPARQLQueryHelper(Query q) {
		this.q = q;
	}
	
}
