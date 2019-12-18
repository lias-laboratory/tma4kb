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

import java.sql.Connection;
import java.sql.DriverManager;

import fr.ensma.lias.tma4kb.exception.TripleStoreException;
import fr.ensma.lias.tma4kb.query.AbstractQueryFactory;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.Session;

/**
 * @author Stephane JEAN
 */
public class HSQLDBQueryFactory extends AbstractQueryFactory {

    @Override
    public Query createQuery(String rdfQuery) {
    	return new HSQLDBQuery(this, rdfQuery);
    }
	

	@Override
	public Session createSession() {
		try {
			Class.forName(this.getConfig().jdbcDriver());
			Connection cnxJDBC = DriverManager.getConnection(this.getConfig().jdbcUrl(), this.getConfig().jdbcLogin(),
					this.getConfig().jdbcPassword());
			return new HSQLDBSession(cnxJDBC);
		} catch (Exception e) {
			System.out.println("Unable to create session");
			e.printStackTrace();
			throw new TripleStoreException(e.getMessage());
		}
	}

}
