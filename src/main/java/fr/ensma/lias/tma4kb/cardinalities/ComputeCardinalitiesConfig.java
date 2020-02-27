package fr.ensma.lias.tma4kb.cardinalities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class ComputeCardinalitiesConfig {
	
	public Config config;

	public ComputeCardinalitiesConfig(String type) throws Exception {
		switch(type) {
		case ("local") :{
			config=ConfigFactory.create(LocalConfig.class);
			break;
		}
		case ("global"):{
			config=ConfigFactory.create(GlobalConfig.class);
			break;}
		case ("cs"):{
			config = ConfigFactory.create(CSConfig.class);
			break;}
		case ("wd"):{
			config = ConfigFactory.create(WD100GlobalConfig.class);
			break;}
		case ("test"):{
			config = ConfigFactory.create(CardinalitiesConfig.class);
			break;}
		default:
			throw (new Exception("Not a valide algorithm type : "+type));
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
    		String domain;
    		if (predicate.equals("type")) {
    			domain=t.getObject();
    			t.setDomain(domain);
    		}
    		else {
    			try {
        		Method method = c.getDeclaredMethod(predicate+"Domain");
    			domain = method.invoke(config).toString();
        		t.setDomain(domain);
    			}catch (NoSuchMethodException e) {
					// if there is no domain, do nothing
				}
    		}
    	}
    }
    
    public void computeMaxLocalCardinalities(Query query) throws Exception {
 	   
    	List<TriplePattern> triples = ((AbstractQuery)query).getTriplePatterns();
    	List<String> predicates = new ArrayList<String>();
    	List<String> domains=new ArrayList<String>(); 
    	for (TriplePattern t : triples) {
    		predicates.add(t.getPredicate());
    		String domain=t.getDomain();
    		int i=0;
    		int j=domain.indexOf(',');
    		while (j>-1) {
    			if(!domains.contains(domain.substring(i, j))) {
        			domains.add(domain.substring(i, j));
        		}
    			i=j+1;
    			j= domain.indexOf(',', i);
    		}
    		if(!domains.contains(domain.substring(i))) {
    			domains.add(domain.substring(i));
    		}
    	}
    	
    	int i = 0;
    	for (String p : predicates) {
    		Class c = config.getClass();
    		Method methode = c.getDeclaredMethod(getNiceName(p)+"Max");
    		Integer cardMax = Integer.parseInt(methode.invoke(config).toString());	//commencer par la cardinalité globale
    		int j=0;
    		while (cardMax>1 && j<domains.size()) { // si la cardinalité globale max est 1, la cardinalité locale max est aussi 1
    			String classe=domains.get(j);
    			try {
    				Method method = c.getDeclaredMethod(classe+getNiceName(p)+"Max");
    				Integer newCard = Integer.parseInt(method.invoke(config).toString());
    				if (newCard<cardMax)
    					cardMax=newCard;
    			}catch (NoSuchMethodException e) {
    				//if there is no domain, do nothing
				}
    			j++;
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
        		if(cs.getPredicates().containsKey(getNiceName(p))&&cs.getPredicates().get(getNiceName(p))>cs.getSubjectNb()) {
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
		int col=cs.indexOf(":");
		int comma=cs.indexOf(",");
		int sem=0;
		while(comma>-1) {
			if (!(sem==0))
				sem++;
			Integer subjNb=Integer.parseInt(cs.substring(sem, comma));
			Map<String,Integer> map=new HashMap<String,Integer>();
			sem=cs.indexOf(";",sem);
			while (comma<sem&&comma>-1) {
				String predicate=cs.substring(comma+1,col);
				int nextCom=cs.indexOf(",",comma+1);
				int card;
				if (nextCom<sem&&nextCom>-1) {
					card= Integer.parseInt(cs.substring(col+1,nextCom));
				}
				else
					card= Integer.parseInt(cs.substring(col+1,sem));
				map.put(predicate,card);
				col=cs.indexOf(":",col+1);
				comma=nextCom;
			}
			CharacteristicSet set = new CharacteristicSet(subjNb, map);
			result.add(set);
		}
		return result;
    }

    public List<CharacteristicSet> getCS2() throws Exception{
    	Class c = config.getClass();
		Method methode = c.getDeclaredMethod("CharacteristicSetNb");
		Integer countCS=Integer.parseInt(methode.invoke(config).toString());
		List<CharacteristicSet> result= new ArrayList<CharacteristicSet>();
		int i=0;
		while(i<countCS) {
			methode = c.getDeclaredMethod("CS"+i+"Value");
			String cs = methode.invoke(config).toString();
			int j=cs.indexOf(",");
			Integer subjNb=Integer.parseInt(cs.substring(0,j));
			Map<String,Integer> map=new HashMap<String,Integer>();
			while (j>-1) {
				int k=cs.indexOf(":", j);
				String predicate=cs.substring(j+1,k);
				int l=cs.indexOf(",", j+1);
				j=l;
				if (l==-1) {
					l=cs.length();
					j=-1;
				}
				Integer card =Integer.parseInt(cs.substring(k+1,l));
				map.put(predicate,card);
			}
			i++;
			CharacteristicSet set = new CharacteristicSet(subjNb, map);
			result.add(set);
		}
		return result;
    }
    
}
