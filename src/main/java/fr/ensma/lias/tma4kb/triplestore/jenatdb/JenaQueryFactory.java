package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.sparqlendpoint.OutputFormat;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.sparqlendpoint.SPARQLEndpointClient;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.sparqlendpoint.SPARQLEndpointSession;

/**
 * @author Stephane JEAN (jean@ensma.fr)
 */
public class JenaQueryFactory extends AbstractQueryFactory {

	private int method;

	public JenaQueryFactory(int method) {
		super();
		this.method = method;
	}

	@Override
	public fr.ensma.lias.tma4kb.query.Query createQuery(String rdfQuery) {
		return new JenaQuery(this, rdfQuery, method);
	}

	public Session createSession(int rep) {
		if (rep == 0) {
			Dataset dataset = TDBFactory.createDataset(this.getConfig().jenaRepository());
			return new JenaSession(dataset);
		} else if (rep == 1) {
			SPARQLEndpointClient fuseki = new SPARQLEndpointClient.Builder().url("http://localhost:3030/jenatdb")
					.defaultGraphURI(this.getConfig().sparqlendpointDefaultGraphURI())
					.outputFormat(OutputFormat.TAB_SEPARATED).build();

			return new SPARQLEndpointSession(fuseki);
		}
		else {
			return null;
		}
	}

	@Override
	public Session createSession() {
		// TODO Auto-generated method stub
		return null;
	}
}
