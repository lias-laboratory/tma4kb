package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqlsb.HSQLDBSession;

public class CardinalityTest {

	@Test	
	public void testFindQbaseCS() throws Exception {
		
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		Query t1t2t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }"); 
		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		
		((AbstractQuery)q).findQbaseCS(instance);
		//System.out.println("Qbase : " + ((AbstractQuery)((AbstractQuery)q).baseQuery).toSimpleString(q));
		
		assertEquals(((AbstractQuery)q).baseQuery, t1t2t3); 
	}
	
	@Test	
	public void testFindQbase() throws Exception {
		
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		Query t1t2t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }"); 
		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		
		q.findQbase(instance);
		//System.out.println("Qbase : " + ((AbstractQuery)((AbstractQuery)q).baseQuery).toSimpleString(q));
		
		assertEquals(((AbstractQuery)q).baseQuery, t1t2t3); 
	}
	
	@Test	
	public void testFindQbaseLocal() throws Exception {
		
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		Query t1t2t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }"); 
		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		q.getTriplePatterns().get(0).setDomain("FullProfessor");
		q.getTriplePatterns().get(2).setDomain("Person");
		q.getTriplePatterns().get(1).setDomain("FullProfessor");
		q.getTriplePatterns().get(3).setDomain("FullProfessor");
		Set<String> classes = new HashSet<String>();
		classes.add("thing");
		q.getTriplePatterns().get(2).setSuperclasses(classes);
		classes.add("Person");
		q.getTriplePatterns().get(0).setSuperclasses(classes);
		q.getTriplePatterns().get(1).setSuperclasses(classes);
		q.getTriplePatterns().get(3).setSuperclasses(classes);
		
		q.findQbaseLocal(instance);
		//System.out.println("Qbase : " + ((AbstractQuery)((AbstractQuery)q).baseQuery).toSimpleString(q));
		
		assertEquals(t1t2t3,((AbstractQuery)q).baseQuery); 
	}
	
	
	
	@Test
	public void testCardAlgo() throws Exception {
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession(); 
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession)instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");   
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();
		
		Query q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		Query t1t2t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }"); 
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");
		
		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);
		
		
		q.runCardAlgo(instance, 3);
		/*for (Query mfis : q.getAllMFIS()) {
			System.out.println("MFIS : " + ((AbstractQuery)mfis).toSimpleString(q));
		}
		for (Query xss : q.getAllXSS()) {
			System.out.println("XSS : " + ((AbstractQuery)xss).toSimpleString(q));
		}*/
		
		assertEquals(1, instance.getExecutedQueryCount()); 
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}

}
