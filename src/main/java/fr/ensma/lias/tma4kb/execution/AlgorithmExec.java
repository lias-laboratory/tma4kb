package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.JenaQueryFactory;

/**
 * @author Ibrahim DELLAL (ibrahim.dellal@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class AlgorithmExec {

	private QueryFactory factory;
	
	private Session session;
	
	private String FILE_QUERIES;
	
	private String FILE_CARD;
	
	private int NB_EXEC;

	public void setUp() {
		factory = new JenaQueryFactory();
	}

	public AlgorithmExec(int nb_exec, String queries, String card) {
		setUp();
		NB_EXEC = nb_exec;
		FILE_QUERIES = queries;
		FILE_CARD = card;
	}

	public void testGenAlgorithms() throws Exception {
		List<QueryExplain> newTestResultPairList = this.newTestResultPairList("/" + FILE_QUERIES);
		ExpRelaxResult resultsBaseline = new ExpRelaxResult(NB_EXEC);
		ExpRelaxResult resultsBFS = new ExpRelaxResult(NB_EXEC);
		ExpRelaxResult resultsVar = new ExpRelaxResult(NB_EXEC);
		ExpRelaxResult resultsCard = new ExpRelaxResult(NB_EXEC);
		int K = 100;
		// int K[] = {2000,300,200000,5000,100,200000,5000,5000,150} ;
		for (int i = 0; i < newTestResultPairList.size(); i++) {
			// *************** bfs Q ******************
			QueryExplain qExplain = newTestResultPairList.get(i);
			String description = qExplain.getDescription();
			/**/
			// *********************** Baseline******************************
			Query q0 = qExplain.getQuery();
			description = qExplain.getDescription();
			System.out.println("-----------------------------------------------------------");
			System.out.println("Query (" + description + "): " + q0);
			System.out.println("-----------------------------------------------------------");

			for (int k = 0; k <= NB_EXEC; k++) {
				q0 = qExplain.getQuery();
				q0 = factory.createQuery(q0.toString());
				session = ((JenaQueryFactory) factory).createSession();
				long time = System.currentTimeMillis();
				q0.runBaseline(session, K);
				long end = System.currentTimeMillis();
				float tps = ((float) (end - time));// /1000f;
				int nbExecutedQuery = session.getExecutedQueryCount();

				if (k > 0) {
					resultsBaseline.addQueryResult(k - 1, q0, tps, nbExecutedQuery);
					System.out.println("baseline - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery);
				}

			} /**/

			// *********************** BFS******************************
			Query q1 = qExplain.getQuery();
			description = qExplain.getDescription();
			System.out.println("-----------------------------------------------------------");
			System.out.println("Query (" + description + "): " + q1);
			System.out.println("-----------------------------------------------------------");

			for (int k = 0; k <= NB_EXEC; k++) {
				q1 = qExplain.getQuery();
				q1 = factory.createQuery(q1.toString());
				session = ((JenaQueryFactory) factory).createSession();
				long time = System.currentTimeMillis();
				q1.runBFS(session, K);
				long end = System.currentTimeMillis();
				float tps = ((float) (end - time));// / 1000f;
				int nbExecutedQuery = session.getExecutedQueryCount();

				if (k > 0) {
					resultsBFS.addQueryResult(k - 1, q1, tps, nbExecutedQuery);
					System.out.println("bfs - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery);
				}

			} /**/

			// ******************* variable-based ********************

			Query q = qExplain.getQuery();
			System.out.println("-----------------------------------------------------------");
			System.out.println("Query (" + description + "): " + q);
			System.out.println("-----------------------------------------------------------");

			for (int k = 0; k <= NB_EXEC; k++) {
				q = factory.createQuery(q.toString());
				session = ((JenaQueryFactory) factory).createSession();
				long time = System.currentTimeMillis();
				q.runVarBased(session, K);
				long end = System.currentTimeMillis();
				float tps = ((float) (end - time)); // /1000f) ;
				int nbExecutedQuery = session.getExecutedQueryCount();

				if (k > 0) {
					resultsVar.addQueryResult(k - 1, q, tps, nbExecutedQuery);
					System.out.println("variable based - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery);
				}
			}
			/**/
			// ******************* card localClass ********************

			Query q2 = qExplain.getQuery();
			description = qExplain.getDescription();
			System.out.println("-----------------------------------------------------------");
			System.out.println("Query (" + description + "): " + q2);
			System.out.println("-----------------------------------------------------------");

			for (int k = 0; k <= NB_EXEC; k++) {
				q2 = qExplain.getQuery();
				q2 = factory.createQuery(q2.toString());
				session = ((JenaQueryFactory) factory).createSession();
				long time = System.currentTimeMillis();
				q2.runCardBased(session, K, FILE_CARD);
				long end = System.currentTimeMillis();
				float tps = ((float) (end - time));
				int nbExecutedQuery = session.getExecutedQueryCount();

				if (k > 0) {
					resultsCard.addQueryResult(k - 1, q2, tps, nbExecutedQuery);
					System.out
							.println("cardinality based - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery);
				}
			}
			System.out.println("------------------------------------");
			System.out.print(resultsBaseline.toString());
			System.out.print(resultsBFS.toString());
			System.out.print(resultsVar.toString());
			System.out.print(resultsCard.toString());
			System.out.println("------------------------------------");
		}

		System.out.println("---------- BILAN Baseline------------------");
		System.out.println(resultsBaseline.toString());
		System.out.println("------------------------------------");
		resultsBaseline.toFile("exp-jena-Baseline.csv");

		System.out.println("---------- BILAN BFS------------------");
		System.out.println(resultsBFS.toString());
		System.out.println("------------------------------------");
		resultsBFS.toFile("exp-jena-BFS.csv");

		System.out.println("---------- BILAN VAR ------------------");
		System.out.println(resultsVar.toString());
		System.out.println("------------------------------------");
		resultsVar.toFile("exp-jena-var.csv");

		System.out.println("---------- BILAN CARD ------------------");
		System.out.println(resultsCard.toString());
		System.out.println("------------------------------------");
		resultsCard.toFile("exp-jena-card.csv");

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
		final InputStream fileUrl = AlgorithmExec.class.getResourceAsStream(filename);
		final InputStreamReader file = new InputStreamReader(fileUrl);
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