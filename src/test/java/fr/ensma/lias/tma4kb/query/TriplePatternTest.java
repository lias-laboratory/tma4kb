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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TriplePatternTest {

	/**
	 * Constants for RDF namespace
	 */
	public static String PREFIX_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	/**
	 * Constants for RDFS prefixes
	 */
	public static String PREFIX_RDFS = "http://www.w3.org/2000/01/rdf-schema#";

	/**
	 * Constants for OWL prefixes
	 */
	public static String PREFIX_OWL = "http://www.w3.org/2002/07/owl#";

	/**
	 * Constants for the WatDiv benchmark
	 */
	public static String PREFIX_WATDIV = "http://db.uwaterloo.ca/watdiv/";

	private TriplePattern t1, t2, t3, t4, t5;

	@Before
	public void setUp() throws Exception {
		t1 = new TriplePattern("?p <type> <p>", 1);
		t2 = new TriplePattern("?p <advises> ?s", 1);
		t3 = new TriplePattern("?s <age> '25'", 1);
		t4 = new TriplePattern("?s <" + PREFIX_RDF + "type> <" + PREFIX_WATDIV + "Book>", 1);
		t5 = new TriplePattern("<http://db.uwaterloo.ca/watdiv/Product2> <" + PREFIX_WATDIV + "availableAt"
				+ "> <http://db.uwaterloo.ca/watdiv/Retailer23>", 1);
	}

	@Test
	public void testGetSuject() {
		assertEquals("?p", t1.getSubject());
		assertEquals("?p", t2.getSubject());
		assertEquals("?s", t3.getSubject());
		assertEquals("?s", t4.getSubject());
		assertEquals("http://db.uwaterloo.ca/watdiv/Product2", t5.getSubject());
	}

	@Test
	public void testGetPredicate() {
		assertEquals("type", t1.getPredicate());
		assertEquals("advises", t2.getPredicate());
		assertEquals("age", t3.getPredicate());
		assertEquals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type", t4.getPredicate());
		assertEquals("http://db.uwaterloo.ca/watdiv/availableAt", t5.getPredicate());
	}

	@Test
	public void testGetObject() {
		assertEquals("p", t1.getObject());
		assertEquals("?s", t2.getObject());
		assertEquals("25", t3.getObject());
		assertEquals("http://db.uwaterloo.ca/watdiv/Book", t4.getObject());
		assertEquals("http://db.uwaterloo.ca/watdiv/Retailer23", t5.getObject());
	}

	@Test
	public void testToString() {
		assertEquals("?p <type> <p>", t1.toString());
		assertEquals("?p <advises> ?s", t2.toString());
		assertEquals("?s <age> '25'", t3.toString());
		assertEquals("?s <" + PREFIX_RDF + "type> <" + PREFIX_WATDIV + "Book>", t4.toString());
		assertEquals("<http://db.uwaterloo.ca/watdiv/Product2> <" + PREFIX_WATDIV + "availableAt"
				+ "> <http://db.uwaterloo.ca/watdiv/Retailer23>", t5.toString());
	}

	@Test
	public void testToSimpleString() {
		assertEquals("t1", t1.toSimpleString());
		assertEquals("t1", t2.toSimpleString());
		assertEquals("t1", t3.toSimpleString());
		assertEquals("t1", t4.toSimpleString());
		assertEquals("t1", t5.toSimpleString());
	}

	@Test
	public void testGetVariables() {
		assertEquals(1, t1.getVariables().size());
		assertEquals(2, t2.getVariables().size());
		assertEquals(1, t3.getVariables().size());
		assertEquals(1, t4.getVariables().size());
		assertEquals(0, t5.getVariables().size());
	}

	@Test
	public void testIsSubjectVariable() {
		assertTrue(t1.isSubjectVariable());
		assertTrue(t2.isSubjectVariable());
		assertTrue(t3.isSubjectVariable());
		assertTrue(t4.isSubjectVariable());
		assertFalse(t5.isSubjectVariable());
	}

	@Test
	public void testIsPredicateVariable() {
		assertFalse(t1.isPredicateVariable());
		assertFalse(t2.isPredicateVariable());
		assertFalse(t3.isPredicateVariable());
		assertFalse(t4.isPredicateVariable());
		assertFalse(t5.isPredicateVariable());
	}

	@Test
	public void testIsObjectVariable() {
		assertFalse(t1.isObjectVariable());
		assertTrue(t2.isObjectVariable());
		assertFalse(t3.isObjectVariable());
		assertFalse(t4.isObjectVariable());
		assertFalse(t5.isObjectVariable());
	}

	@Test
	public void testEquals() {
		assertTrue(t1.equals(new TriplePattern("?p <type> <p>", 2)));
	}

	@Test
	public void testToSQL() {
		String sqlT1 = "select s as p from t where p='type' and o='p'";
		assertEquals(sqlT1, t1.toSQL());
		String sqlT2 = "select s as p, o as s from t where p='advises'";
		assertEquals(sqlT2, t2.toSQL());
	}
}
