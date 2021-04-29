package fr.ensma.lias.tma4kb.query.algorithms;

import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class Var extends BFS{
	
	@Override
	protected void useProperties(TriplePattern tp, Query qNew, Query qTemp, int k, int Nb) {
		if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
			executedQueries.put(qNew, k + 1);
		}
	}

}
