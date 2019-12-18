package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import org.apache.jena.query.Dataset;

import fr.ensma.lias.tma4kb.query.*;

/**
 * @author Mickael BARON
 */
public class JenaSession extends AbstractSession {

    private Dataset dataset;
    
    public JenaSession(Dataset pDataset) {
    	this.dataset = pDataset;
    }
    
    public Dataset getDataset() {
    	return dataset;
    }
    
    @Override
    public void close() throws Exception {
    	if (dataset != null) 
    		dataset.close();
    }
}
