package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBSession;

public class HybridTest {

	@Test
	public void testHybrid() throws Exception {
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing_composite.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		

		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?a <teacherOf> ?c . ?e <type> <Student> . ?e <nationality> ?g }");

		q.runHybrid(instance, 4);
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();
		
		Query t1t2t4t5 = currentQueryFactory.createQuery("SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?e <type> <Student> . ?e <nationality> ?g }"); 
		Query t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?a <teacherOf> ?c }");
		
		expectedMFIS.add(t3);
		expectedXSS.add(t1t2t4t5);
		
		for (Query mfis : q.getAllMFIS()) {
			System.out.println("MFIS : " + ((AbstractQuery)mfis).toSimpleString(q));
		}
		for (Query xss : q.getAllXSS()) {
			System.out.println("XSS : " + ((AbstractQuery)xss).toSimpleString(q));
		}
		
		assertEquals(4, instance.getExecutedQueryCount()); //8 if not executing initial query, 9 if executing initial query
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
		
		q.runHybrid(instance, 3);
		
		expectedMFIS = new ArrayList<>();
		expectedXSS = new ArrayList<>();
		
		Query t1t2t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?e <type> <Student> }"); 
		Query t5 = currentQueryFactory.createQuery("SELECT * WHERE { ?e <nationality> ?g }");
		
		expectedMFIS.add(t3);
		expectedMFIS.add(t5);
		expectedXSS.add(t1t2t4);
		
		for (Query mfis : q.getAllMFIS()) {
			System.out.println("MFIS : " + ((AbstractQuery)mfis).toSimpleString(q));
		}
		for (Query xss : q.getAllXSS()) {
			System.out.println("XSS : " + ((AbstractQuery)xss).toSimpleString(q));
		}
		
		assertEquals(6, instance.getExecutedQueryCount()); //8 if not executing initial query, 9 if executing initial query
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
		
	}

}
