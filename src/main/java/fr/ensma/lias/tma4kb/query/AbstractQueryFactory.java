package fr.ensma.lias.tma4kb.query;

import org.aeonbits.owner.ConfigFactory;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractQueryFactory implements QueryFactory {

	private TMA4KBConfig config;

	public AbstractQueryFactory() {
		config = ConfigFactory.create(TMA4KBConfig.class);
	}

	protected TMA4KBConfig getConfig() {
		return this.config;
	}

	@Override
	public Query createQuery(String rdfQuery, Query initialQuery) {
		final Query createQuery = this.createQuery(rdfQuery);
		((AbstractQuery) createQuery).setInitialQuery(initialQuery);
		return createQuery;
	}
}
