package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.Dataset;
import org.apache.jena.tdb.TDBFactory;

import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN
 */
public class JenaQueryFactory extends AbstractQueryFactory {

    @Override
    public fr.ensma.lias.tma4kb.query.Query createQuery(String rdfQuery) {
	return new JenaQuery(this, rdfQuery);
    }

    @Override
    public Session createSession() {
    	Dataset dataset = TDBFactory
    			.createDataset(this.getConfig().jenaRepository());
    	return (Session) new JenaSession(dataset);
    }
}
