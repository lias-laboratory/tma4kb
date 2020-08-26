package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 */
public class JenaQueryFactory extends AbstractQueryFactory {

	private QueryMethod method;

	public JenaQueryFactory(QueryMethod method) {
		super();
		this.method = method;
	}

	@Override
	public fr.ensma.lias.tma4kb.query.Query createQuery(String rdfQuery) {
		return new JenaQuery(this, rdfQuery, method);
	}

	@Override
	public Session createSession() {
		// creates a link to the downloaded repository
		Dataset dataset = TDBFactory.createDataset(this.getConfig().jenaRepository());
		return new JenaSession(dataset);

	}

}
