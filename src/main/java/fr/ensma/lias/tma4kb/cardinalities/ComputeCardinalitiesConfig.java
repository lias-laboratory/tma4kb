package fr.ensma.lias.tma4kb.cardinalities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aeonbits.owner.Config;

import fr.ensma.lias.tma4kb.execution.MyTest;
import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class ComputeCardinalitiesConfig {
	
	public Config config;
	Properties properties = new Properties();

	public ComputeCardinalitiesConfig(String source) throws Exception {
		InputStream input = MyTest.class.getResourceAsStream(source);
		try{
			properties.load(input);
		}
		finally{
			input.close();
		} 
	}
	
	public String getNiceName(String uri) {
		int indexOfSeparator = uri.indexOf('#');
		
		if (indexOfSeparator!=-1) {
			return uri.substring(indexOfSeparator + 1, uri.length());
	    }
		else {
			int indexOfSlash = uri.lastIndexOf("/");
			return uri.substring(indexOfSlash + 1, uri.length());
		}
	}

/**
 * Calculates maximum cardinalities of the predicates of all triple patterns of a query and 
 * fills the cardMax attributes of these triple patterns
 * Uses the .config file included in the resources 
 * @param the query that contains all triple patterns whose predicates we want to determine the cardinality of
 * @throws Exception
 */
    public void computeMaxCardinalities(Query query) throws Exception {
    	   
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    	}
    	
    	int i = 0;
    	for (String p : predicates) {
    		Integer cardMax = Integer.parseInt(properties.get(getNiceName(p)+".max").toString());
    		//((AbstractQuery)query).setCardMax(i, cardMax);
    		if (cardMax<=1)
    			((AbstractQuery)query).setCardMax(i, true);
    		else
    			((AbstractQuery)query).setCardMax(i, false);
    		i++;
    	}
    }
    
}
