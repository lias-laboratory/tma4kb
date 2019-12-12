package fr.ensma.lias.tma4kb.query;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aeonbits.owner.ConfigFactory;

public class ComputeCardinalitiesConfig {
	
	public CardinalitiesConfig config;
	
	public ComputeCardinalitiesConfig() {
		config = ConfigFactory.create(CardinalitiesConfig.class);
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
    		Class c = config.getClass();

    		Method method = c.getDeclaredMethod(p+"Max");
    		Integer cardMax = Integer.parseInt(method.invoke(config).toString());	
		
    		((AbstractQuery)query).setCardMax(i, cardMax);
    		i++;
    	}
    }
    
    public void computeDomains(Query query) throws Exception {
 	   
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    	}
    	
    	for (TriplePattern t : triples) {
    		String predicate=t.getPredicate();
    		Class c = config.getClass();

    		Method method = c.getDeclaredMethod(predicate+"Domain");
    		String domain = method.invoke(config).toString();	
    		t.setDomain(domain);
    	}
    }
    
    public void computeMaxLocalCardinalities(Query query) throws Exception {
 	   
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	Set<String> domains=new HashSet<String>(); 
    	Set<String> superclasses=new HashSet<String>();
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    		String domain=t.getDomain();
    		if(!domains.contains(domain)) {
    			domains.add(domain);
        		for (String sc:t.getSuperclasses()) {
        			if(!superclasses.contains(sc))
        				superclasses.add(sc);
        		}
    		}
    	}
    	for (String sc:superclasses) {
   			for (String dom: domains) {
    			if (dom.equals(sc))
        			domains.remove(dom);
    		}
    	}
    	
    	int i = 0;
    	for (String p : predicates) {
    		Class c = config.getClass();
    		Method methode = c.getDeclaredMethod(p+"Max");
    		Integer cardMax = Integer.parseInt(methode.invoke(config).toString());	//commencer par la cardinalité globale
    		if (cardMax>1) { // si la cardinalité globale max est 1, la cardinalité locale max est aussi 1 (on ne distingue pas le cas 0)
    			for (String classe : domains) {
    				Method method = c.getDeclaredMethod(classe+p+"Max");
    				Integer newCard = Integer.parseInt(method.invoke(config).toString());
    				if (newCard<cardMax)
    					cardMax=newCard;
    			}
    		}
    		((AbstractQuery)query).setCardMax(i, cardMax);
    		i++;
    	}
    }
}
