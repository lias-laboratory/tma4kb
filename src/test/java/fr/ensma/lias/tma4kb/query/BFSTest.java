package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.ensma.lias.tma4kb.query.algorithms.BFS;
import fr.ensma.lias.tma4kb.query.util.ScriptRunner;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBSession;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class BFSTest {

	@Test
	public void runBFSTest() throws Exception {
		// Given
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));

		List<Query> expectedMFIS = new ArrayList<>();
		List<Query> expectedXSS = new ArrayList<>();

		Query t1t2t3 = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		Query t4 = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");

		expectedMFIS.add(t4);
		expectedXSS.add(t1t2t3);

		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }", new BFS());

		// When
		q.runAlgo(instance, 3);

		// Then
		for (Query mfis : q.getAllMFIS()) {
			System.out.println("MFIS : " + ((AbstractQuery) mfis).toSimpleString(q));
		}
		for (Query xss : q.getAllXSS()) {
			System.out.println("XSS : " + ((AbstractQuery) xss).toSimpleString(q));
		}
		assertEquals(9, instance.getExecutedQueryCount()); // 8 if not executing initial query, 9 if executing initial
															// query
		assertTrue(q.getAllMFIS().containsAll(expectedMFIS));
		assertTrue(expectedMFIS.containsAll(q.getAllMFIS()));
		assertTrue(q.getAllXSS().containsAll(expectedXSS));
		assertTrue(expectedXSS.containsAll(q.getAllXSS()));
	}
}