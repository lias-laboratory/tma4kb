package fr.ensma.lias.tma4kb.query.algorithms;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.TriplePattern;

public class AnyCard extends Full{
	
	public AnyCard(ComputeCardinalitiesConfig card) {
		super(card);
	}


	@Override
	protected void useProperties(TriplePattern tp, Query qNew, Query qTemp, int k, int Nb) {
		if (qNew.getVariables().size() == qTemp.getVariables().size()) { // variable property
			executedQueries.put(qNew, k + 1);
		}
		else if (!tp.isPredicateVariable() && Nb / tp.getCardMax() > k
				&& qNew.getVariables().contains(tp.getSubject())) { // cardinality property
			executedQueries.put(qNew, Nb / tp.getCardMax());
		}

	}

}
