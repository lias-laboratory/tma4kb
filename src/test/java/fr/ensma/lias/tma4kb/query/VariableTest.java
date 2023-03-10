package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ensma.lias.tma4kb.query.algorithms.Var;
import fr.ensma.lias.tma4kb.query.util.ScriptRunner;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBSession;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class VariableTest {

	@Test
	public void runVarBasedTest() throws Exception {
		// Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));

		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }", new Var());
		Query t1t2t3 = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");

		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);

		// When
		q.runAlgo(instance, 3);

		// Then
		assertEquals(5, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
	
	@Test
	public void runVarBasedCompositeQueryTest() throws Exception {	
		//Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing_composite.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?a <teacherOf> ?c . ?e <type> <Student> . ?e <nationality> ?g }", new Var());


		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query t1t2t4t5 = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?e <type> <Student> . ?e <nationality> ?g }");
		Query t3 = currentQueryFactory.createQuery("SELECT * WHERE { ?a <teacherOf> ?c }");

		expectedMFIS.add(t3);
		expectedXSS.add(t1t2t4t5);

		//When

		q.runAlgo(instance, 4);
		
		//Then
		
		assertEquals(7, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
}
