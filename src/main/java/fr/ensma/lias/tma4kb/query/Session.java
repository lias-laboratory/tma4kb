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
package fr.ensma.lias.tma4kb.query;

/**
 * @author Mickael BARON
 */
public interface Session {

    /**
     * @throws Exception
     */
    void close() throws Exception;

    /**
     * Gives the number of executed queries to compute the MFS/XSS.
     * 
     * @return
     */
    int getExecutedQueryCount();
    
    
    /**
     * Set the number of queries executed
     * 
     * @param queryCount the number of queries executed
     */
    void setExecutedQueryCount(int queryCount);

    /**
     * Clear the number of executed queries
     */
    void clearExecutedQueryCount();
}
