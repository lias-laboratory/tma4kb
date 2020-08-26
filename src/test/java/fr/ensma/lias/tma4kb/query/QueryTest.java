package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import fr.ensma.lias.tma4kb.query.util.ScriptRunner;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBSession;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class QueryTest {

	@Test
	public void isFailingTest() throws Exception {
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();
		final Session instance = currentQueryFactory.createSession();
		ScriptRunner newScriptRunner = new ScriptRunner(((HSQLDBSession) instance).getConnection(), false, false);
		InputStream resourceAsStream = getClass().getResourceAsStream("/dump_test_query_failing.sql");
		newScriptRunner.runScript(new InputStreamReader(resourceAsStream));

		// t1 ^ t2 ^ t3 ^ t4
		Query q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 1) > 1);
		assertTrue(q.isFailing(instance, 2) > 2);
		assertTrue(q.isFailing(instance, 3) > 3);
		assertTrue(q.isFailing(instance, 4) > 4);
		assertTrue(q.isFailing(instance, 5) > 5);
		assertTrue(q.isFailing(instance, 6) > 6);
		assertFalse(q.isFailing(instance, 7) > 7);
		assertFalse(q.isFailing(instance, 8) > 8);

		// t1 ^ t2 ^ t3
		q = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t1 ^ t2 ^ t4
		q = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t1 ^ t3 ^ t4
		q = currentQueryFactory.createQuery(
				"SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t2 ^ t3 ^ t4
		q = currentQueryFactory
				.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t1 ^ t2
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <age> ?a }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t1 ^ t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t1 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t2 ^ t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <nationality> ?n }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t2 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t3 ^ t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <nationality> ?n . ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t1
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <type> <FullProfessor> }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t2
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <age> ?a }");
		assertFalse(q.isFailing(instance, 3) > 3);

		// t3
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <nationality> ?n }");
		assertTrue(q.isFailing(instance, 3) > 3);

		// t4
		q = currentQueryFactory.createQuery("SELECT * WHERE { ?fp <teacherOf> ?c }");
		assertTrue(q.isFailing(instance, 3) > 3);
	}
}
