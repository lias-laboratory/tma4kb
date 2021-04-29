package fr.ensma.lias.tma4kb.query;

import org.aeonbits.owner.ConfigFactory;

import fr.ensma.lias.tma4kb.query.algorithms.Algorithm;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractQueryFactory implements QueryFactory {

	public enum ChoiceOfTpst {
		jena, fuseki, virtuoso;
	
	}

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

	@Override
	public Query createQuery(String rdfQuery, Algorithm alg) {
		final Query createQuery = this.createQuery(rdfQuery);
		createQuery.setAlgorithm(alg);
		return createQuery;
	}
}
