/*********************************************************************************
* This file is part of QARS4UKB Project.
* Copyright (C) 2017 LIAS - ENSMA
*   Teleport 2 - 1 avenue Clement Ader
*   BP 40109 - 86961 Futuroscope Chasseneuil Cedex - FRANCE
* 
* QARS4UKB is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* QARS4UKB is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License
* along with QARS4UKB.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.tma4kb.triplestore.sparqlendpoint;

import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Mickael BARON
 */
public class SPARQLEndpointQuery extends AbstractQuery {

    private SPARQLEndpointQueryHelper helper;

    public SPARQLEndpointQuery(QueryFactory factory, String query) {
	super(factory, query);
	helper = new SPARQLEndpointQueryHelper(this);
    }

    /*public SPARQLEndpointQuery(QueryFactory factory, List<TriplePattern> tps) {
	super(factory, tps);
	helper = new SPARQLEndpointQueryHelper(this);
    }*/

    /*@Override
    public Result getResult(Session session, Double alpha) {
	return helper.getResult(session, alpha);
    }*/

    //@Override
    public String toNativeQuery() {
	return helper.toNativeQuery();
    }

	@Override
	protected boolean isFailingAux(Session session, int k) {
		return helper.executeQuery(session,k);
	}

	@Override
	protected int isFailingNb(Session session, int k) {
		return helper.countQuery(session, k);
	}
}
