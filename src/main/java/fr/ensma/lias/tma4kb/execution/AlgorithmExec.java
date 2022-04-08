package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.ensma.lias.tma4kb.cardinalities.ComputeCardinalitiesConfig;
import fr.ensma.lias.tma4kb.exception.NotYetImplementedException;
import fr.ensma.lias.tma4kb.query.AbstractQueryFactory.ChoiceOfTpst;
import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.SPARQLQueryHelper.QueryMethod;
import fr.ensma.lias.tma4kb.query.Session;
import fr.ensma.lias.tma4kb.query.algorithms.AnyCard;
import fr.ensma.lias.tma4kb.query.algorithms.BFS;
import fr.ensma.lias.tma4kb.query.algorithms.CS;
import fr.ensma.lias.tma4kb.query.algorithms.Full;
import fr.ensma.lias.tma4kb.query.algorithms.Local;
import fr.ensma.lias.tma4kb.query.algorithms.Var;
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

	private String initialQuery;
	
	private String file_card;

	private String file_local;

	private String file_cs;

	private int nb_exec;

	private int kValue;

	private ChoiceOfTpst tripsto;

	private QueryMethod qMethod;

	private AlgoChoice[] algoName;
	
	private String url;
	private String output;
	
	private ComputeCardinalitiesConfig card_global;
	private ComputeCardinalitiesConfig card_local;
	private ComputeCardinalitiesConfig card_cs;

	public enum AlgoChoice {
		Base, BFS, Var, Full, Local, CS, Any;
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
		case url:
			factory = new ClientQueryFactory(qMethod);
			break;
		}
	}

	public AlgorithmExec(int nb_ex, String queries, String card, String local, String cs, int k, QueryMethod method, AlgoChoice[] algo,
			ChoiceOfTpst triplestore) {
		nb_exec = nb_ex;
		file_queries = queries;
		file_card = card;
		file_local = local;
		file_cs=cs;
		kValue = k;
		qMethod = method;
		algoName = algo;
		tripsto = triplestore;
		setUp();
	}

	public AlgorithmExec(int numberExecution, String query, String cardinalitiesFile, int k, AlgoChoice[] algorithm,
			String outputFile, String endpoint) {
		nb_exec = numberExecution;
		initialQuery = query;
		file_card = cardinalitiesFile;
		kValue = k;
		qMethod = QueryMethod.countlimit;
		algoName = algorithm;
		tripsto = ChoiceOfTpst.url;
		url=endpoint;
		output=outputFile;
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
		case url:
			session = ((ClientQueryFactory) factory).createSession(url);
			break;
		}
	}
	
	public void shinyExec() throws Exception {
		Query q = factory.createQuery(initialQuery);
		Map <AlgoChoice, ShinyResult> results = new HashMap<AlgoChoice, ShinyResult>();
		long time=0;
		long end=0;
		for (int j = 0; j < algoName.length; j++) {
			switch (algoName[j]) {
			case Full:
				card_global=new ComputeCardinalitiesConfig(file_card);
				card_global.importSource();
				break;
			default:
				break;
			}
			for (int k=0;k<=nb_exec;k++) {
				createSessionHere(factory);
				switch (algoName[j]) {
				case BFS:
					q = factory.createQuery(q.toString(), new BFS());
					break;
				case Var:
					q = factory.createQuery(q.toString(), new Var());
					break;
				case Full:
					q = factory.createQuery(q.toString(), new Full(card_global));
					break;
				default:
					break;
				}
				time = System.currentTimeMillis();
				q.runAlgo(session, kValue);
				end = System.currentTimeMillis();
				Integer nbExecutedQuery = session.getExecutedQueryCount();
				if (k==0) {
					results.put(algoName[j], new ShinyResult(algoName[j],q.getAllMFIS(),q.getAllXSS(),nbExecutedQuery,kValue));
				}
				else {
					if (!q.getAllMFIS().containsAll(results.get(algoName[j]).getMFIS()) | !results.get(algoName[j]).getMFIS().containsAll(q.getAllMFIS())|
						!q.getAllMFIS().containsAll(results.get(algoName[j]).getMFIS()) | !results.get(algoName[j]).getMFIS().containsAll(q.getAllMFIS())|
						!nbExecutedQuery.equals(results.get(algoName[j]).getNb())) {
						throw new NotYetImplementedException();
					}
				}
				
				float[] timer = session.getTimes();
				Integer[] times = new Integer[5];
				times[0]=(int) (timer[1]/1000);
				times[1]=(int) ((timer[3]+timer[4])/1000);
				times[2]=(int) (session.getCountQueryTime()/1000);
				times[3]=(int) timer[8]/1000;
				times[4]=(int) (end-time)/1000;
				results.get(algoName[j]).addTimes(times);
			}
			results.get(algoName[j]).save(output);
		}
	}

	public void testGenAlgorithms() throws Exception {
		List<QueryExplain> newTestResultPairList = this.newTestResultPairList(file_queries);
		ExpRelaxResult resultsBase = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsBFS = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsVar = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsFull = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsLoc = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsCS = new ExpRelaxResult(nb_exec);
		ExpRelaxResult resultsAny = new ExpRelaxResult(nb_exec);
		long time=0;
		long end=0;
		for (int j = 0; j < algoName.length; j++) {
			switch (algoName[j]) {
			case Full:
			case Any:
				card_global=new ComputeCardinalitiesConfig(file_card);
				card_global.importSource();
				break;
			case Local:
				card_local=new ComputeCardinalitiesConfig(file_local);
				card_local.importSource();
				break;
			case CS:
				card_cs=new ComputeCardinalitiesConfig(file_cs);
				card_cs.makeCS();
				break;
			default:
				break;
			}
		for (int i = 0; i < newTestResultPairList.size(); i++) {
			QueryExplain qExplain = newTestResultPairList.get(i);
			String description = qExplain.getDescription();
				switch (algoName[j]) {
				case Base:
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
						time = System.currentTimeMillis();
						q0.runAlgo(session, kValue);
						end = System.currentTimeMillis();
						float tps = ((float) (end - time));// /1000f;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						float[] times = session.getTimes();

						if (k > 0) {
							resultsBase.addQueryResult(k - 1, q0, tps, nbExecutedQuery, queryCountTime,times);
							System.out.println("Base - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: " + nbExecutedQuery );
									//+ " queryCountTime: " + queryCountTime);
						}
						System.out.println("MFIS");
						for (Query q : q0.getAllMFIS())
							System.out.println(q);
						System.out.println("XSS");
						for (Query q : q0.getAllXSS())
							System.out.println(q);
					}
					break;

				case BFS:
					/**/

					// *********************** BFS******************************
					Query q1 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q1);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q1 = qExplain.getQuery();
						q1 = factory.createQuery(q1.toString(), new BFS());
						createSessionHere(factory);
						time = System.currentTimeMillis();
						q1.runAlgo(session, kValue);
						end = System.currentTimeMillis();
						float tps = ((float) (end - time));// / 1000f;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						float[] times = session.getTimes();

						if (k > 0) {
							resultsBFS.addQueryResult(k - 1, q1, tps, nbExecutedQuery, queryCountTime,times);
							System.out.println("bfs - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: " + nbExecutedQuery);
//									+ " queryCountTime: " + queryCountTime);
						}

					}
					break;

				case Var:
					/**/

					// ******************* var ********************

					Query q = qExplain.getQuery();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q = factory.createQuery(q.toString(), new Var());
						createSessionHere(factory);
						time = System.currentTimeMillis();
						q.runAlgo(session, kValue);
						end = System.currentTimeMillis();
						float tps = ((float) (end - time)); // /1000f) ;
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						float[] times = session.getTimes();

						if (k > 0) {
							resultsVar.addQueryResult(k - 1, q, tps, nbExecutedQuery, queryCountTime, times);
							System.out.println("var - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: " + nbExecutedQuery);
//									+ " queryCountTime: " + queryCountTime);
						}
					}
					break;

				case Full:
					/**/

					// ******************* full ********************

					Query q2 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q2);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q2 = qExplain.getQuery();
						q2 = factory.createQuery(q2.toString(), new Full(card_global));
						createSessionHere(factory);
						time = System.currentTimeMillis();
						q2.runAlgo(session, kValue);
						end = System.currentTimeMillis();
						float tps = ((float) (end - time));
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						float[] times = session.getTimes();

						if (k > 0) {
							resultsFull.addQueryResult(k - 1, q2, tps, nbExecutedQuery, queryCountTime, times);
							System.out.println("cardinality based - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: "
									+ nbExecutedQuery);//+ " queryCountTime: " + queryCountTime);

						}
					}
					break;
				case Any:
					/**/

					// ******************* any ********************

					Query q3 = qExplain.getQuery();
					description = qExplain.getDescription();
					System.out.println("-----------------------------------------------------------");
					System.out.println("Query (" + description + "): " + q3);
					System.out.println("-----------------------------------------------------------");

					for (int k = 0; k <= nb_exec; k++) {
						q3 = qExplain.getQuery();
						q3 = factory.createQuery(q3.toString(), new AnyCard(card_global));
						createSessionHere(factory);
						time = System.currentTimeMillis();
						q3.runAlgo(session, kValue);
						end = System.currentTimeMillis();
						float tps = ((float) (end - time));
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						float[] times = session.getTimes();

						if (k > 0) {
							resultsAny.addQueryResult(k - 1, q3, tps, nbExecutedQuery, queryCountTime, times);
							System.out.println("any card - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: "
									+ nbExecutedQuery );//+ " queryCountTime: " + queryCountTime);
						}
					}
					break;
				case Local:
						/**/

						// ******************* local ********************

						Query q4 = qExplain.getQuery();
						description = qExplain.getDescription();
						System.out.println("-----------------------------------------------------------");
						System.out.println("Query (" + description + "): " + q4);
						System.out.println("-----------------------------------------------------------");

						for (int k = 0; k <= nb_exec; k++) {
							q4 = qExplain.getQuery();
							q4 = factory.createQuery(q4.toString(), new Local(card_local));
							createSessionHere(factory);
							time = System.currentTimeMillis();
							q4.runAlgo(session, kValue);
							end = System.currentTimeMillis();
							float tps = ((float) (end - time));
							int nbExecutedQuery = session.getExecutedQueryCount();
							float queryCountTime = session.getCountQueryTime();
							float[] times = session.getTimes();

							if (k > 0) {
								resultsLoc.addQueryResult(k - 1, q4, tps, nbExecutedQuery, queryCountTime, times);
								System.out.println("cardloc - Time = " + tps + "("+queryCountTime+")"+ "ms, NbQueriesExecuted: "
										+ nbExecutedQuery );//+ " queryCountTime: " + queryCountTime);
							}
						}
						break;
					case CS:
						/**/

						// ******************* full ********************

						Query q5 = qExplain.getQuery();
						description = qExplain.getDescription();
						System.out.println("-----------------------------------------------------------");
						System.out.println("Query (" + description + "): " + q5);
						System.out.println("-----------------------------------------------------------");

						for (int k = 0; k <= nb_exec; k++) {
							q5 = qExplain.getQuery();
							q5 = factory.createQuery(q5.toString(), new CS(card_cs));
							createSessionHere(factory);
							time = System.currentTimeMillis();
							q5.runAlgo(session, kValue);
							end = System.currentTimeMillis();
							float tps = ((float) (end - time));
							int nbExecutedQuery = session.getExecutedQueryCount();
							float queryCountTime = session.getCountQueryTime();
							float[] times = session.getTimes();

							if (k > 0) {
								resultsCS.addQueryResult(k - 1, q5, tps, nbExecutedQuery, queryCountTime, times);
								System.out.println("cardinality based - Time = " + tps + "("+queryCountTime+")"+"ms, NbQueriesExecuted: "
										+ nbExecutedQuery );//+ " queryCountTime: " + queryCountTime);
							}
						}
						break;
				}
			}

			System.out.println("------------------------------------");
			for (int l = 0; l < algoName.length; l++) {
				switch (algoName[l]) {
				case Base:
					System.out.print(resultsBase.toString());
					break;
				case BFS:
					System.out.print(resultsBFS.toString());
					break;
				case Var:
					System.out.print(resultsVar.toString());
					break;
				case Full:
					System.out.print(resultsFull.toString());
					break;
				case Any:
					System.out.print(resultsAny.toString());
					break;
				case Local:
					System.out.print(resultsLoc.toString());
					break;
				case CS:
					System.out.print(resultsCS.toString());
					break;
				}
			}
		}
		System.out.println("------------------------------------");
		System.out.println(
				"Query \t Nb Executed Query \t Total time (ms) \t Query Execution time (ms)");
		System.out.println("------------------------------------");

		for (int k = 0; k < algoName.length; k++) {
			switch (algoName[k]) {
			case Base:
				System.out.println("---------- BILAN BASE------------------");
				System.out.println(resultsBase.toString());
				System.out.println("------------------------------------");
				resultsBase.toFile("exp-" + tripsto.toString() + "-base-K" + kValue + ".csv");
				break;
			case BFS:
				System.out.println("---------- BILAN BFS------------------");
				System.out.println(resultsBFS.toString());
				System.out.println("------------------------------------");
				resultsBFS.toFile("exp-" + tripsto.toString() + "-bfs-K" + kValue + ".csv");
				break;
			case Var:
				System.out.println("---------- BILAN VAR ------------------");
				System.out.println(resultsVar.toString());
				System.out.println("------------------------------------");
				resultsVar.toFile("exp-" + tripsto.toString() + "-var-K" + kValue + ".csv");
				break;
			case Full:
				System.out.println("---------- BILAN FULL ------------------");
				System.out.println(resultsFull.toString());
				System.out.println("------------------------------------");
				resultsFull.toFile("exp-" + tripsto.toString() + "-full-K" + kValue + ".csv");
				break;
			case Any:
				System.out.println("---------- BILAN ANY------------------");
				System.out.println(resultsAny.toString());
				System.out.println("------------------------------------");
				resultsAny.toFile("exp-" + tripsto.toString() + "-any-K" + kValue + ".csv");
				break;
			case Local:
				System.out.println("---------- BILAN LOCAL ------------------");
				System.out.println(resultsLoc.toString());
				System.out.println("------------------------------------");
				resultsLoc.toFile("exp-" + tripsto.toString() + "-loc-K" + kValue + ".csv");
				break;
			case CS:
				System.out.println("---------- BILAN CS ------------------");
				System.out.println(resultsCS.toString());
				System.out.println("------------------------------------");
				resultsCS.toFile("exp-" + tripsto.toString() + "-cs-K" + kValue + ".csv");
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