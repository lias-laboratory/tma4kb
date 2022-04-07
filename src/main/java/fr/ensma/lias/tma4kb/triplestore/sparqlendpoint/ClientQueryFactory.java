package fr.ensma.lias.tma4kb.triplestore.sparqlendpoint;

import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * 
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 * 
 */
public class ClientQueryFactory extends AbstractQueryFactory {

	private QueryMethod method;

	public ClientQueryFactory(QueryMethod method) {
		super();
		this.method = method;
	}

	@Override
	public fr.ensma.lias.tma4kb.query.Query createQuery(String rdfQuery) {
		return new ClientQuery(this, rdfQuery, method);
	}

	public Session createSession(ChoiceOfTpst tripsto) {
		switch (tripsto) {
		case fuseki:
			// creates a link to the online knowledge base
			SPARQLEndpointClient fuseki = new SPARQLEndpointClient.Builder().url(this.getConfig().sparqlendpointUrl())
					.defaultGraphURI("").outputFormat(OutputFormat.TAB_SEPARATED).build();

			return new SPARQLEndpointSession(fuseki);
		case virtuoso:
			// creates a link to the online knowledge base
			SPARQLEndpointClient virtuoso = new SPARQLEndpointClient.Builder().url(this.getConfig().sparqlendpointUrl())
					.defaultGraphURI(this.getConfig().sparqlendpointDefaultGraphURI())
					.outputFormat(OutputFormat.TAB_SEPARATED).build();

			return new SPARQLEndpointSession(virtuoso);
		case jena:
		default:
			return null;
		}
	}
	
	public Session createSession(String url) {
		SPARQLEndpointClient virtuoso = new SPARQLEndpointClient.Builder().url(url)
				.outputFormat(OutputFormat.TAB_SEPARATED).build();

		return new SPARQLEndpointSession(virtuoso);
	}

	@Override
	public Session createSession() {
		// TODO Auto-generated method stub
		return null;
	}
}