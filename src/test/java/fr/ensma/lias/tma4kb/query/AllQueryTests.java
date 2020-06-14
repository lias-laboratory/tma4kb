package fr.ensma.lias.tma4kb.query;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Mickael BARON (baron@ensma.fr)
 */
@RunWith(Suite.class)
@SuiteClasses(value = { 
		BaselineTest.class,
		BFSTest.class,
		CardinalityTest.class,
		CPTest.class,
		QueryTest.class, 
		TriplePatternTest.class,
		VariableTest.class,
		})
public class AllQueryTests {
}
