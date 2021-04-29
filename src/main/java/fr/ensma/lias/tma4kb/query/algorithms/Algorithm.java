package fr.ensma.lias.tma4kb.query.algorithms;

import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;

public interface Algorithm {
	
	public void runAlgo(Session session, int k, Query initialQuery);
	
}
