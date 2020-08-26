package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ensma.lias.tma4kb.query.AbstractQueryFactory.ChoiceOfTpst;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.triplestore.jenatdb.JenaQueryFactory;
import fr.ensma.lias.tma4kb.triplestore.sparqlendpoint.ClientQueryFactory;

/**
 * @author Ibrahim DELLAL (ibrahim.dellal@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Célia BORIES-GARCIA (celiabories-garcia@etu.isae-ensma.fr)
 */
public class AlgorithmExec {

	private QueryFactory factory;

	private Session session;

	private String file_queries;

	private String file_card;

	private int nb_exec;

	private int kValue;

	private ChoiceOfTpst tripsto;

	private QueryMethod qMethod;

	private AlgoChoice[] algoName;

	public enum AlgoChoice {
		base, bfs, var, full;
	}

	public void setUp() {
		switch (tripsto) {
		case jena:
			factory = new JenaQueryFactory(qMethod);
			break;
		case fuseki:
		case virtuoso:
			factory = new ClientQueryFactory(qMethod);
			break;

		}
	}

	public AlgorithmExec(int nb_ex, String queries, String card, int k, QueryMethod method, AlgoChoice[] algo,
			ChoiceOfTpst triplestore) {
		nb_exec = nb_ex;
		file_queries = queries;
		file_card = card;
		kValue = k;
		qMethod = method;
		algoName = algo;
		tripsto = triplestore;
		setUp();
	}

	public void createSessionHere(QueryFactory factory) {
		switch (tripsto) {
		case jena:
			session = ((JenaQueryFactory) factory).createSession();
			break;
		case fuseki:
		case virtuoso:
			session = ((ClientQueryFactory) factory).createSession(tripsto);
			break;
		}
	}

	public void testGenAlgorithms() throws Exception {
		List<QueryExplain> newTestResultPairList = this.newTestResultPairList(file_queries);
		ExpRelaxResult resultsBase = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsBFS = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsVar = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsFull = new ExpRelaxResult(nb_exec);
		for (int i = 0; i < newTestResultPairList.size(); i++) {
			QueryExplain qExplain = newTestResultPairList.get(i);
			String description = qExplain.getDescription();
			for (int j = 0; j < algoName.length; j++) {
				switch (algoName[j]) {
				case base:
					/**/

					// *********************** Base******************************
					Query q0 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q0);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q0 = qExplain.getQuery();
						q0 = factory.createQuery(q0.toString());
						createSessionHere(factory);
						long time = System.currentTimeMillis();
						q0.runBase(session, kValue);
						long end = System.currentTimeMillis();
						float tps = ((float) (end - time));// /1000f;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();

						if (k > 0) {
							resultsBase.addQueryResult(k - 1, q0, tps, nbExecutedQuery, queryCountTime);
							System.out.println("Base - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery
									+ " queryCountTime: " + queryCountTime);
						}

					}
					break;

				case bfs:
					/**/

					// *********************** BFS******************************
					Query q1 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q1);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q1 = qExplain.getQuery();
						q1 = factory.createQuery(q1.toString());
						createSessionHere(factory);
						long time = System.currentTimeMillis();
						q1.runBFS(session, kValue);
						long end = System.currentTimeMillis();
						float tps = ((float) (end - time));// / 1000f;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();

						if (k > 0) {
							resultsBFS.addQueryResult(k - 1, q1, tps, nbExecutedQuery, queryCountTime);
							System.out.println("bfs - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery
									+ " queryCountTime: " + queryCountTime);
						}

					}
					break;

				case var:
					/**/

					// ******************* var ********************

					Query q = qExplain.getQuery();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q = factory.createQuery(q.toString());
						createSessionHere(factory);
						long time = System.currentTimeMillis();
						q.runVar(session, kValue);
						long end = System.currentTimeMillis();
						float tps = ((float) (end - time)); // /1000f) ;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();

						if (k > 0) {
							resultsVar.addQueryResult(k - 1, q, tps, nbExecutedQuery, queryCountTime);
							System.out.println("var - Time = " + tps + "ms, NbQueriesExecuted: " + nbExecutedQuery
									+ " queryCountTime: " + queryCountTime);
						}
					}
					break;

				case full:
					/**/

					// ******************* full ********************

					Query q2 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q2);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q2 = qExplain.getQuery();
						q2 = factory.createQuery(q2.toString());
						createSessionHere(factory);
						long time = System.currentTimeMillis();
						q2.runFull(session, kValue, file_card);
						long end = System.currentTimeMillis();
						float tps = ((float) (end - time));
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();

						if (k > 0) {
							resultsFull.addQueryResult(k - 1, q2, tps, nbExecutedQuery, queryCountTime);
							System.out.println("cardinality based - Time = " + tps + "ms, NbQueriesExecuted: "
									+ nbExecutedQuery + " queryCountTime: " + queryCountTime);
						}
					}
					break;
				}
			}

			System.out.println("------------------------------------");
			for (int l = 0; l < algoName.length; l++) {
				switch (algoName[l]) {
				case base:
					System.out.print(resultsBase.toString());
					break;
				case bfs:
					System.out.print(resultsBFS.toString());
					break;
				case var:
					System.out.print(resultsVar.toString());
					break;
				case full:
					System.out.print(resultsFull.toString());
					break;
				}
			}
		}
		System.out.println("------------------------------------");

		for (int k = 0; k < algoName.length; k++) {
			switch (algoName[k]) {
			case base:
				System.out.println("---------- BILAN BASE------------------");
				System.out.println(resultsBase.toString());
				System.out.println("------------------------------------");
				resultsBase.toFile("exp-jena-base-" + tripsto.toString() + ".csv");
				break;
			case bfs:
				System.out.println("---------- BILAN BFS------------------");
				System.out.println(resultsBFS.toString());
				System.out.println("------------------------------------");
				resultsBFS.toFile("exp-jena-BFS-" + tripsto.toString() + ".csv");
				break;
			case var:
				System.out.println("---------- BILAN VAR ------------------");
				System.out.println(resultsVar.toString());
				System.out.println("------------------------------------");
				resultsVar.toFile("exp-jena-var-" + tripsto.toString() + ".csv");
				break;
			case full:
				System.out.println("---------- BILAN FULL ------------------");
				System.out.println(resultsFull.toString());
				System.out.println("------------------------------------");
				resultsFull.toFile("exp-jena-full-" + tripsto.toString() + ".csv");
				break;
			}
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
		final InputStream fileUrl = new FileInputStream(new File(filename));
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