package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBSession;

public class BFSTest {

	@Test
	public void testBFS() throws Exception {
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();
		
		Query t1t2t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }"); 
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");
		
		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);
		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		q.runBFS(instance, 3);
		for (Query mfis : q.getAllMFIS()) {
			System.out.println("MFIS : " + ((AbstractQuery)mfis).toSimpleString(q));
		}
		for (Query xss : q.getAllXSS()) {
			System.out.println("XSS : " + ((AbstractQuery)xss).toSimpleString(q));
		}
		
		assertEquals(8, instance.getExecutedQueryCount()); 
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}

}
