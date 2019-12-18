

/*********************************************************************************
* This file is part of QARS4UKB Project.
* Copyright (C) 2017 LIAS - ENSMA
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
* along with QARS4UKB.  If not, see <http://www.gnu.org/licenses/>.
**********************************************************************************/
package fr.ensma.lias.tma4kb.query;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.JenaQueryFactory;




/**
 * @author Ibrahim DELLAL
 */
public class PropreAlgorithmTest {



	private QueryFactory factory;

	private Session session;
	private static final String FILE_QUERIES = "queriesLUBM.test";
	private static final int NB_EXEC = 1;
	ComputeCardinalitiesConfig c =new ComputeCardinalitiesConfig();
	//ComputeCardinalitiesLocalClassImpConfig c2 ;
	@Before
	public void setUp() throws Exception {
		

		factory = new JenaQueryFactory();
		
		//session2= ((JenaTDBGraphQueryFactory) factory).createSession();
	}
	 @Test
	 public void testAlgorithms() throws Exception {
		 List<QueryExplain>  newTestResultPairList = this.newTestResultPairList("/" + FILE_QUERIES);
		 try {	
		 ExpRelaxResult resultsBFS = new ExpRelaxResult(NB_EXEC);
		 ExpRelaxResult resultsCardGlo = new ExpRelaxResult(NB_EXEC);
		 //ExpRelaxResult resultsCardLoc = new ExpRelaxResult(NB_EXEC);
			for (int i = 0; i<newTestResultPairList.size(); i++) {
			// ***************   dfs Q ****************** 
				QueryExplain qExplain = newTestResultPairList.get(i);
				String description = qExplain.getDescription();
				
				
				// *********************** Card Q1******************************
				Query q1 = qExplain.getQuery();
				description = qExplain.getDescription();
				System.out.println("-----------------------------------------------------------");
				System.out.println("Query (" + description + "): " + q1);
				System.out.println("-----------------------------------------------------------");
				
				for (int k = 0; k <= NB_EXEC; k++) {
					q1 = qExplain.getQuery();
					//description = qExplain.getDescription();
					q1 = factory.createQuery(q1.toString());c.computeMaxCardinalities(q1);
					session = ((JenaQueryFactory) factory).createSession();
					long time = System.currentTimeMillis();
					q1.runBFS(session, 8);
					long end = System.currentTimeMillis();
					float tps = ((float) (end - time)) / 1000f;
					int nbExecutedQuery = session.getExecutedQueryCount();
					//System.out.println("Q1 nbr de requête dfs " + session.getExecutedQueryCount());
					//System.out.println("time dfs " + ((float) (end - time)) / 1000f);
					
					
					
					if (k > 0) {
						resultsBFS.addQueryResult(k - 1, q1, tps, nbExecutedQuery, 0);
						System.out.println("bfs - Time = " + tps + ", NbQueriesExecuted: " + nbExecutedQuery);
					}
					
				}
				// *******************  card localClass ********************
				/*
				Query q2 = qExplain.getQuery();
				description = qExplain.getDescription();
				System.out.println("-----------------------------------------------------------");
				System.out.println("Query (" + description + "): " + q2);
				System.out.println("-----------------------------------------------------------");
				
				for (int k = 0; k <= NB_EXEC; k++) {
					q2 = qExplain.getQuery();
					//description = qExplain.getDescription();
					q2 = factory.createQuery(q2.toString());c2=new ComputeCardinalitiesLocalClassImpConfig("jena",q2);
					session = ((JenaTDBGraphQueryFactory) factory).createSession();
					long time = System.currentTimeMillis();
					q2.runCardGenImp(session, 0.0);
					long end = System.currentTimeMillis();
					float tps = ((float) (end - time)) / 1000f;
					int nbExecutedQuery = session.getExecutedQueryCount();
					//System.out.println("Q1 nbr de requête dfs " + session.getExecutedQueryCount());
					//System.out.println("time dfs " + ((float) (end - time)) / 1000f);
					
					
					
					if (k > 0) {
						resultsCardLoc.addQueryResult(k - 1, q2, tps, nbExecutedQuery, 0);
						System.out.println("card local classe - Time = " + tps + ", NbQueriesExecuted: " + nbExecutedQuery);
					}
				}*/
				// *******************  CardAlgo ********************
				
				Query q = qExplain.getQuery();
				System.out.println("-----------------------------------------------------------");
				System.out.println("Query (" + description + "): " + q);
				System.out.println("-----------------------------------------------------------");

				for (int k = 0; k <= NB_EXEC; k++) {
					q = factory.createQuery(q.toString());
					session = ((JenaQueryFactory) factory).createSession();				
					long time = System.currentTimeMillis();
					q.runCardAlgo(session, 8);
					long end = System.currentTimeMillis();
					float tps = ((float) (end - time)) / 1000f;
					int nbExecutedQuery = session.getExecutedQueryCount();
					//System.out.println("Q1 nbr de requête dfs " + session.getExecutedQueryCount());
					//System.out.println("time dfs " + ((float) (end - time)) / 1000f);
					
					
					
					if (k > 0) {
						resultsCardGlo.addQueryResult(k - 1, q, tps, nbExecutedQuery, 0);
						System.out.println("card - Time = " + tps + ", NbQueriesExecuted: " + nbExecutedQuery);
					}
				}
				
				
				for(Query t : q.getAllMFIS()){
					System.out.println("card global MFIS " +t);
				}
				
				for(Query t : q1.getAllMFIS()){
					System.out.println("bfs MFIS " +t);
				}
				Assert.assertTrue(q.getAllMFIS().containsAll((q1.getAllMFIS())));
				Assert.assertTrue(q1.getAllMFIS().containsAll((q.getAllMFIS())));
				Assert.assertTrue(q.getAllXSS().containsAll((q1.getAllXSS())));
				Assert.assertTrue(q1.getAllXSS().containsAll((q.getAllXSS())));
				
				/*Assert.assertTrue(q.getAllMFIS().containsAll((q2.getAllMFIS())));
				Assert.assertTrue(q2.getAllMFIS().containsAll((q.getAllMFIS())));
				Assert.assertTrue(q.getAllXSS().containsAll((q2.getAllXSS())));
				Assert.assertTrue(q2.getAllXSS().containsAll((q.getAllXSS())));*/
			}
		
			
			System.out.println("---------- BILAN CARD Global------------------");
			System.out.println(resultsCardGlo.toString());
			System.out.println("------------------------------------");

			resultsCardGlo.toFile("exp-jena-cardGlo.csv");
			
			/*System.out.println("---------- BILAN CARD------------------");
			System.out.println(resultsCardLoc.toString());
			System.out.println("------------------------------------");

			resultsCardLoc.toFile("exp-jena-cardLoc.csv");*/
			
			System.out.println("---------- BILAN BFS------------------");
			System.out.println(resultsBFS.toString());
			System.out.println("------------------------------------");

			resultsBFS.toFile("exp-jena-bfs.csv");

		} catch (IOException e) {
			System.out.println("Unable to read the queries in the file.");
			e.printStackTrace();
		}
		 
		 
	 }	 
		class QueryExplain {

			protected int index;

			protected String description;

			protected Query query;

			protected List<Query> mfs;

			protected List<Query> xss;

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public List<Query> getMfs() {
				return mfs;
			}

			public List<Query> getXss() {
				return xss;
			}

			public QueryExplain() {
				this.mfs = new ArrayList<Query>();
				this.xss = new ArrayList<Query>();
			}

			public Query getQuery() {
				return query;
			}

			public void setQuery(Query pQuery) {
				this.query = pQuery;
			}

			public void addMFS(Query mfs) {
				this.mfs.add(mfs);
			}

			public void addXSS(Query xss) {
				this.xss.add(xss);
			}

			public void setIndex(int pIndex) {
				this.index = pIndex;
			}

			public int getIndex() {
				return this.index;
			}
		}

		protected List<QueryExplain> newTestResultPairList(final String filename) throws IOException {
			final List<QueryExplain> queries = new ArrayList<QueryExplain>();
			final URL fileUrl = PropreAlgorithmTest.class.getResource(filename);
			final FileReader file = new FileReader(fileUrl.getFile());
			BufferedReader in = null;
			try {
				in = new BufferedReader(file);
				StringBuffer test = null;
				StringBuffer mfsresult = null;
				StringBuffer xssresult = null;

				final Pattern pTest = Pattern.compile("# Test (\\w+) \\((.*)\\)");
				final Pattern pMFS = Pattern.compile("# MFS (\\w+)");
				final Pattern pXSS = Pattern.compile("# XSS (\\w+)");

				String line;
				int lineNumber = 0;

				String testNumber = null;
				String testName = null;
				StringBuffer curbuf = null;

				while ((line = in.readLine()) != null) {
					lineNumber++;
					final Matcher mTest = pTest.matcher(line);
					final Matcher mMFS = pMFS.matcher(line);
					final Matcher mXSS = pXSS.matcher(line);
					if (mTest.matches()) { // # Test
						addTestResultPair(queries, test, mfsresult, xssresult, testNumber, testName);

						testNumber = mTest.group(1);
						testName = mTest.group(2);

						test = new StringBuffer();
						mfsresult = new StringBuffer();
						xssresult = new StringBuffer();

						curbuf = test;
					} else if (mMFS.matches()) { // # Result
						if (testNumber == null) {
							throw new RuntimeException("Test file has result without a test (line " + lineNumber + ")");
						}
						final String resultNumber = mMFS.group(1);
						if (!testNumber.equals(resultNumber)) {
							throw new RuntimeException(
									"Result " + resultNumber + " test " + testNumber + " (line " + lineNumber + ")");
						}

						curbuf = mfsresult;
					} else if (mXSS.matches()) {
						if (testNumber == null) {
							throw new RuntimeException("Test file has result without a test (line " + lineNumber + ")");
						}
						final String resultNumber = mXSS.group(1);
						if (!testNumber.equals(resultNumber)) {
							throw new RuntimeException(
									"Result " + resultNumber + " test " + testNumber + " (line " + lineNumber + ")");
						}

						curbuf = xssresult;
					} else {
						line = line.trim();
						if (!line.isEmpty()) {
							curbuf.append(line);
							curbuf.append("\n");
						}
					}
				}

				addTestResultPair(queries, test, mfsresult, xssresult, testNumber, testName);

			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (final IOException e) {
					}
				}
			}

			return queries;
		}

		private void addTestResultPair(List<QueryExplain> queries, StringBuffer query, StringBuffer mfsResult,
				StringBuffer xssResult, String number, String description) throws IOException {
			if (query == null || mfsResult == null || xssResult == null) {
				return;
			}

			QueryExplain currentQuery = new QueryExplain();
			currentQuery.setQuery(this.factory.createQuery(query.toString().trim()));
			currentQuery.setIndex(Integer.valueOf(number));
			currentQuery.setDescription(description.trim());

			BufferedReader bufReader = new BufferedReader(new StringReader(mfsResult.toString()));
			String line = null;
			while ((line = bufReader.readLine()) != null) {
				currentQuery.addMFS(this.factory.createQuery(line.trim()));
			}

			bufReader = new BufferedReader(new StringReader(xssResult.toString()));
			line = null;
			while ((line = bufReader.readLine()) != null) {
				currentQuery.addXSS(this.factory.createQuery(line.trim()));
			}

			queries.add(currentQuery);
		}
}