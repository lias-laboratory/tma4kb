package fr.ensma.lias.tma4kb.query;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import fr.ensma.lias.tma4kb.triplestore.hsqldb.HSQLDBQueryFactory;

/**
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class CPTest {

	@Test
	public void decomposeCP1Test() throws Exception {
		// Given
		TriplePattern t1 = new TriplePattern("<?a> <p> <?b>", 1);
		TriplePattern t2 = new TriplePattern("<?a> <p> <?c>", 2);
		TriplePattern t3 = new TriplePattern("<?d> <p> <?e>", 3);
		TriplePattern t4 = new TriplePattern("<?i> <p> <?f>", 4);
		TriplePattern t5 = new TriplePattern("<?b> <p> <?g>", 5);
		TriplePattern t6 = new TriplePattern("<?a> <p> <?h>", 6);
		TriplePattern t7 = new TriplePattern("<?i> <p> <?j>", 7);
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();

		Query q = currentQueryFactory.createQuery("");
		q.addTriplePattern(t1);
		q.addTriplePattern(t2);
		q.addTriplePattern(t3);
		q.addTriplePattern(t4);
		q.addTriplePattern(t5);
		q.addTriplePattern(t6);
		q.addTriplePattern(t7);

		List<Set<TriplePattern>> rep = new ArrayList<Set<TriplePattern>>();
		Set<TriplePattern> s1 = new HashSet<TriplePattern>();
		s1.add(t1);
		s1.add(t2);
		s1.add(t5);
		s1.add(t6);
		Set<TriplePattern> s2 = new HashSet<TriplePattern>();
		s2.add(t3);
		Set<TriplePattern> s3 = new HashSet<TriplePattern>();
		s3.add(t4);
		s3.add(t7);

		// When
		((AbstractQuery) q).decomposeCP();
		rep = ((AbstractQuery) q).decomp;

		// Then
		assertTrue(rep.size() == 3);
		assertTrue(rep.get(0).containsAll(s1));
		assertTrue(rep.get(1).containsAll(s2));
		assertTrue(rep.get(2).containsAll(s3));
		assertTrue(s1.containsAll(rep.get(0)));
		assertTrue(s2.containsAll(rep.get(1)));
		assertTrue(s3.containsAll(rep.get(2)));
	}

	@Test
	public void decomposeCPnoCartesianProductTest() throws Exception {
		// Given
		TriplePattern t1 = new TriplePattern("<?a> <p> <?b>", 1);
		TriplePattern t2 = new TriplePattern("<?a> <p> <?c>", 2);
		TriplePattern t5 = new TriplePattern("<?b> <p> <?g>", 5);
		TriplePattern t6 = new TriplePattern("<?a> <p> <?h>", 6);
		QueryFactory currentQueryFactory = new HSQLDBQueryFactory();

		Query q = currentQueryFactory.createQuery("");
		q.addTriplePattern(t1);
		q.addTriplePattern(t2);
		q.addTriplePattern(t5);
		q.addTriplePattern(t6);

		List<Set<TriplePattern>> rep = new ArrayList<Set<TriplePattern>>();
		Set<TriplePattern> s1 = new HashSet<TriplePattern>();
		s1.add(t1);
		s1.add(t2);
		s1.add(t5);
		s1.add(t6);

		// When
		((AbstractQuery) q).decomposeCP();
		rep = ((AbstractQuery) q).decomp;

		// Then
		assertTrue(rep.size() == 1);
		assertTrue(rep.get(0).containsAll(s1));
		assertTrue(s1.containsAll(rep.get(0)));
	}
}
