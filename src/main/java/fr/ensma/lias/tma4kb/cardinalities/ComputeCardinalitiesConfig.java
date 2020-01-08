package fr.ensma.lias.tma4kb.cardinalities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aeonbits.owner.ConfigFactory;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class ComputeCardinalitiesConfig {
	
	public CardinalitiesConfig config;
	//public GlobalConfigLUBM config;
	
	public ComputeCardinalitiesConfig() {
		config = ConfigFactory.create(CardinalitiesConfig.class);
		//config=ConfigFactory.create(GlobalConfigLUBM.class);
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
    		Class c = config.getClass();

    		Method method = c.getDeclaredMethod(getNiceName(p)+"Max");
    		Integer cardMax = Integer.parseInt(method.invoke(config).toString());	
    		//((AbstractQuery)query).setCardMax(i, cardMax);
    		if (cardMax<=1)
    			((AbstractQuery)query).setCardMax(i, true);
    		else
    			((AbstractQuery)query).setCardMax(i, false);
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
    		Method methode = c.getDeclaredMethod(getNiceName(p)+"Max");
    		Integer cardMax = Integer.parseInt(methode.invoke(config).toString());	//commencer par la cardinalité globale
    		if (cardMax>1) { // si la cardinalité globale max est 1, la cardinalité locale max est aussi 1 (on ne distingue pas le cas 0)
    			for (String classe : domains) {
    				Method method = c.getDeclaredMethod(classe+getNiceName(p)+"Max");
    				Integer newCard = Integer.parseInt(method.invoke(config).toString());
    				if (newCard<cardMax)
    					cardMax=newCard;
    			}
    		}
    		//((AbstractQuery)query).setCardMax(i, cardMax);
    		if (cardMax<=1)
    			((AbstractQuery)query).setCardMax(i, true);
    		else
    			((AbstractQuery)query).setCardMax(i, false);
    		i++;
    	}
    }

    public void computeMaxCSCardinalities(Query query) throws Exception {
    	List<CharacteristicSet> charSets=getCS();
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	List<CharacteristicSet> characSets = new ArrayList<CharacteristicSet>();
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    	}
    	for (CharacteristicSet cs:charSets) {
    		if(cs.getPredicates().keySet().containsAll(predicates)) {
    			characSets.add(cs);
    		}
    	}
    	
    	int i = 0;
    	for (String p : predicates) {	
        	Boolean maxCard1=true;
        	for (CharacteristicSet cs:charSets) {
        		if(cs.getPredicates().containsKey(p)&&cs.getPredicates().get(p)>cs.getSubjectNb()) {
        			maxCard1=false;
        		}
        	}
    		((AbstractQuery)query).setCardMax(i, maxCard1);
    		i++;
    	}
    }
    public List<CharacteristicSet> getCS() throws Exception{
    	Class c = config.getClass();
		Method methode = c.getDeclaredMethod("CharacteristicSet");
		String cs = methode.invoke(config).toString();
		List<CharacteristicSet> result= new ArrayList<CharacteristicSet>();
		int i=0;
		while(i<cs.length()) {
			Integer subjNb=Integer.parseInt(cs.substring(i, i+1));
			Map<String,Integer> map=new HashMap<String,Integer>();
			i++;
			while (!(cs.charAt(i)==';')) {
				int j=i+1;
				String predicate=cs.substring(j,j+cs.substring(j).indexOf(':'));
				j+=predicate.length();
				int card= Integer.parseInt(cs.substring(j+1, j+2));
				i=j+2;
				map.put(predicate,card);
			}
			i++;
			CharacteristicSet set = new CharacteristicSet(subjNb, map);
			result.add(set);
		}
		return result;
    }
    
}
