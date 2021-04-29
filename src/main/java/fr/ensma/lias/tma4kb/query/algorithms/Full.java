package fr.ensma.lias.tma4kb.query.algorithms;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class Full extends BFS {

	ComputeCardinalitiesConfig c;

	public Full(ComputeCardinalitiesConfig card) {
		c = card;
	}


	@Override
	protected void useProperties(TriplePattern tp, Query qNew, Query qTemp, int k, int Nb) {
		if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
			executedQueries.put(qNew, k + 1);
		}
		else if (!tp.isPredicateVariable() && tp.getCardMax() <= 1 && qNew.getVariables().contains(tp.getSubject())) { // cardinality
																													// property
			executedQueries.put(qNew, k + 1);
		}

	}

	@Override
	protected void calculateCard(Query qTemp) {
		try {
			c.computeMaxCardinalities(qTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

}
