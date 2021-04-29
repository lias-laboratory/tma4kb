package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.query.algorithms.AnyCard;
import fr.ensma.lias.tma4kb.query.algorithms.Full;
import fr.ensma.lias.tma4kb.query.algorithms.Local;
import fr.ensma.lias.tma4kb.query.util.ScriptRunner;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBSession;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class CardinalityTest {

	@Test
	public void runCardBasedTest() throws Exception {
		// Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		ComputeCardinalitiesConfig c = new ComputeCardinalitiesConfig("src/test/resources/cardinalities.config");
		c.importSource();
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }", new Full(c));
		Query t1t2t3 = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");

		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);

		// When
		q.runAlgo(instance, 3);

		// Then
		assertEquals(2, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
	
	@Test
	public void runAny_CardTest() throws Exception {
		// Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		ComputeCardinalitiesConfig c = new ComputeCardinalitiesConfig("src/test/resources/cardinalities1.config");
		c.importSource();
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }", new AnyCard(c));
		Query t1t2t3 = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");

		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);

		// When
		((AbstractQuery) q).runAlgo(instance, 3);

		// Then
		assertEquals(2, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
	
	@Test
	public void runLocalTest() throws Exception {
		// Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		ComputeCardinalitiesConfig c = new ComputeCardinalitiesConfig("src/test/resources/cardinalities1.config");
		c.importSource();
		
		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }", new Local(c));
		Query t1t2t3 = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");

		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);

		// When
		((AbstractQuery) q).runAlgo(instance, 3);

		// Then
		assertEquals(2, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
	
	@Test
	public void runCardBasedCompositeQueryTest() throws Exception{
		//Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing_composite.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));
		ComputeCardinalitiesConfig c = new ComputeCardinalitiesConfig("src/test/resources/cardinalities2.config");
		c.importSource();

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?a <advisor> ?e . ?a <age> ?b . ?a <teacherOf> ?c . ?e <type> <Student> . ?e <nationality> ?g }", new Full(c));


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

		assertEquals(4, instance.getExecutedQueryCount());
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
}
