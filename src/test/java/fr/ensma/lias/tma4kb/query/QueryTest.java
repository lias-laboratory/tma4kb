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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;



import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBSession;

public class QueryTest {

	@Test
	public void testFailing() throws Exception {
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		// t1 ^ t2 ^ t3 ^ t4
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 1));
		assertTrue(q.isFailing(instance, 2));
		assertTrue(q.isFailing(instance, 3));
		assertTrue(q.isFailing(instance, 4));
		assertTrue(q.isFailing(instance, 5));
		assertTrue(q.isFailing(instance, 6));
		assertFalse(q.isFailing(instance, 7));
		assertFalse(q.isFailing(instance, 8));
		
		// t1 ^ t2 ^ t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3));
		
		// t1 ^ t2 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));

		// t1 ^ t3 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		
		// t2 ^ t3 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		
		// t1 ^ t2
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a }");
		assertFalse(q.isFailing(instance, 3));
		
		// t1 ^ t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3));
		
		// t1 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		
		// t2 ^ t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3));
		
		// t2 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		
		// t3 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		
		// t1
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> }");
		assertFalse(q.isFailing(instance, 3));
		
		// t2
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a }");
		assertFalse(q.isFailing(instance, 3));
		
		// t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <nationality> ?n }");
		assertTrue(q.isFailing(instance, 3));
		
		// t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3));
		

		
	}

}
