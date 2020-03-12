package fr.ensma.lias.tma4kb.query;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Stephane JEAN (jean@ensma.fr)
 */
public abstract class SPARQLQueryHelper implements QueryHelper {

	/**
	 * The query that this helper uses
	 */
	protected Query q;

	/***
	 * Creates a SPARQLQueryHelper with query q
	 * 
	 * @param q the query
	 */
	public SPARQLQueryHelper(Query q) {
		this.q = q;
	}

}
