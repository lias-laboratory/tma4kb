/*********************************************************************************
* This file is part of TMA4KB Project.
* Copyright (C) 2019 LIAS - ENSMA
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
* along with TMA4KB.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.tma4kb.triplestore.hsqlsb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import fr.ensma.lias.tma4kb.exception.TripleStoreException;
import fr.ensma.lias.tma4kb.query.AbstractQuery;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.query.TriplePattern;

/**
 * @author Stephane JEAN
 */
public class HSQLDBQuery extends AbstractQuery {
	
    public HSQLDBQuery(HSQLDBQueryFactory factory, String query) {
	super(factory, query);
    }

	@Override
	public int isFailing(Session session,int k) {
		try {
			Statement stmt = ((HSQLDBSession) session).getConnection().createStatement();
			ResultSet rset = stmt.executeQuery(toNativeQuery());
			int i=0;
			session.setExecutedQueryCount(session.getExecutedQueryCount() + 1);
			// This code is probably not efficient
			// since it's only used for test issue, it's fine
			while (rset.next() ) { // Stop once more than k answers are found : && i<=k) { the result is inaccurate but boolean failure is correct
				i++;
			}
			rset.close();
			stmt.close();
			return i;
		} catch (SQLException e) {
			System.out.println("Unable to execute the query: " + e.getMessage());
			e.printStackTrace();
			throw new TripleStoreException();
		}
	}

	private String toNativeQuery() {
		String res = "select * from ";
		List<TriplePattern> triplePatterns = getTriplePatterns();
		for (int i = 0; i < triplePatterns.size(); i++) {
			if (i > 0)
				res += " NATURAL JOIN ";
			res += "(" + triplePatterns.get(i).toSQL() + ") " + "t" + i;
		}
		return res;
	}

}
