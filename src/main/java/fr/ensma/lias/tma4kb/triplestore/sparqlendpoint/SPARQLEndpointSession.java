package fr.ensma.lias.tma4kb.triplestore.sparqlendpoint;

import fr.ensma.lias.tma4kb.query.AbstractSession;

public class SPARQLEndpointSession extends AbstractSession {

	private SPARQLEndpointClient currentSession;

	public SPARQLEndpointSession(SPARQLEndpointClient pCurrentSession) {
		this.currentSession = pCurrentSession;
	}

    public SPARQLEndpointClient getSPARQLEndpointClient() {
        return currentSession;
    }

	@Override
	public void close() throws Exception {
	}
}