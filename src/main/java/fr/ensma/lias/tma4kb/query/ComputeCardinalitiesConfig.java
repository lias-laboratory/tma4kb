package fr.ensma.lias.tma4kb.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;

public class ComputeCardinalitiesConfig {

/**
 * Calculates maximum cardinalities of the predicates of all triple patterns of a query and 
 * fills the cardMax attributes of these triple patterns
 * Uses the .config file included in the resources 
 * @param the query that contains all triple patterns whose predicates we want to determine the cardinality of
 * @throws Exception
 */
    public void computeMaxCardinalities(Query query) throws Exception {
    	   
    	CardinalitiesConfig config = ConfigFactory.create(CardinalitiesConfig.class);
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    	}
    	
    	int i = 0;
    	for (String p : predicates) {
    		Class c = config.getClass();

    		Method method = c.getDeclaredMethod(p+"Max");
    		Integer cardMax = Integer.parseInt(method.invoke(config).toString());	
		
    		((AbstractQuery)query).setCardMax(i, cardMax);
    		i++;
    	}
    }
}
