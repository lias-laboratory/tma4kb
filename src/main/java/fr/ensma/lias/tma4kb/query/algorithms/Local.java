package fr.ensma.lias.tma4kb.query.algorithms;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.query.Query;

public class Local extends Full{
	public Local(ComputeCardinalitiesConfig c) {
		super(c);
	}
	
	@Override
	protected void calculateCard(Query qTemp) {
		try {
			c.computeDomains(qTemp);
			c.computeMaxLocalCardinalities(qTemp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};

}
