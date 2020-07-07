package fr.ensma.lias.tma4kb.triplestore.jenatdb;

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

import fr.ensma.lias.tma4kb.query.Query;
import fr.ensma.lias.tma4kb.query.QueryFactory;
import fr.ensma.lias.tma4kb.query.Session;

public class MethodExec {
	private QueryFactory[] factory = new QueryFactory[5];

	private QueryFactory factoryTemp;

	private Session session;

	private String FILE_QUERIES;

	private int NB_EXEC;

	private int[] K;

	private String[] name = { "ALL", "STOP K", "COUNT", "LIMIT", "COUNT+LIMIT" };

	public void setUp() {
		for (int i = 0; i < 5; i++) {
			factory[i] = new JenaQueryFactory(i);
		}
	}

	public MethodExec(String queries, int[] k, int execution) {
		FILE_QUERIES = queries;
		K = k;
		NB_EXEC = execution;
		setUp();
	}

	public void methodRun() throws Exception {

		MethodStockResult resultsForMethods = new MethodStockResult(NB_EXEC);
		for (int j = 0; j < 5; j++) {
			System.out.println("-----------------------------------------------------------");
			System.out.println("                   METHOD " + name[j] + "                  ");
			System.out.println("-----------------------------------------------------------");

			factoryTemp = factory[j];
			List<QueryExplain> newQueryList = this.newQueryList(FILE_QUERIES);

			for (int i = 0; i < newQueryList.size(); i++) {

				if (j == 0 || j == 2) {

					for (int k = 0; k <= NB_EXEC; k++) {
						// Récupération de la requete à exécuter
						QueryExplain qExplain = newQueryList.get(i);
						Query q = qExplain.getQuery();
						q = factoryTemp.createQuery(q.toString());
						// Création du lien avec le repository
						session = ((JenaQueryFactory) factoryTemp).createSession();
						JenaQueryHelper jenaq = new JenaQueryHelper(q, j);
						// Execution de la requete
						int nbAnswers = jenaq.executeQuery(session, K[K.length - 1]);
						int nbExecutedQuery = session.getExecutedQueryCount();
						float queryCountTime = session.getCountQueryTime();
						// Affichage des résultats intermédiaires
						if (k > 0) {
							resultsForMethods.addResult(k - 1, name[j], nbAnswers, queryCountTime, K[K.length - 1]);
							System.out.println("Method " + name[j] + " K = " + K[K.length - 1] + " queryCountTime: "
									+ queryCountTime + " Nbanswers: " + nbAnswers);
						}
					}
				} else {
					for (int l = 0; l < K.length; l++) {

						for (int k = 0; k <= NB_EXEC; k++) {
							// Récupération de la requete à exécuter
							QueryExplain qExplain = newQueryList.get(i);
							Query q = qExplain.getQuery();
							q = factoryTemp.createQuery(q.toString());
							// Création du lien avec le repository
							session = ((JenaQueryFactory) factoryTemp).createSession();
							JenaQueryHelper jenaq = new JenaQueryHelper(q, j);
							// Execution de la requete
							int nbAnswers = jenaq.executeQuery(session, K[l]);
							int nbExecutedQuery = session.getExecutedQueryCount();
							float queryCountTime = session.getCountQueryTime();
							// Affichage des résultats intermédiaires
							if (k > 0) {
								resultsForMethods.addResult(k - 1, name[j], nbAnswers, queryCountTime, K[l]);
								System.out.println("Method " + name[j] + " K = " + K[l] + " queryCountTime: "
										+ queryCountTime + " Nbanswers: " + nbAnswers);
							}
						}
					}
				}
			}
		}
		System.out.println("-----------------------------------------------------------");
		System.out.println("                        BILAN METHOD                       ");
		System.out.println("Name \t K \t Nb Answers \t Query Count Time");
		System.out.println("-----------------------------------------------------------");
		System.out.println(resultsForMethods.toString());
		resultsForMethods.toFile("exp-allMethod-jena.csv");

	}

// Donne la description d'une requete
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

	// Lit le fichier de requete pour créer une liste des requetes
	protected List<QueryExplain> newQueryList(final String filename) throws IOException {
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
					addQuery(queries, test, mfsresult, xssresult, testNumber, testName);

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

			addQuery(queries, test, mfsresult, xssresult, testNumber, testName);

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

//Ajoute une requete a la liste de requete créée dans la fonction précédente
	private void addQuery(List<QueryExplain> queries, StringBuffer query, StringBuffer mfsResult,
			StringBuffer xssResult, String number, String description) throws IOException {
		if (query == null || mfsResult == null || xssResult == null) {
			return;
		}

		QueryExplain currentQuery = new QueryExplain();
		currentQuery.setQuery(this.factoryTemp.createQuery(query.toString().trim()));
		currentQuery.setIndex(Integer.valueOf(number));
		currentQuery.setDescription(description.trim());

		BufferedReader bufReader = new BufferedReader(new StringReader(mfsResult.toString()));
		String line = null;
		while ((line = bufReader.readLine()) != null) {
			currentQuery.addMFS(this.factoryTemp.createQuery(line.trim()));
		}

		bufReader = new BufferedReader(new StringReader(xssResult.toString()));
		line = null;
		while ((line = bufReader.readLine()) != null) {
			currentQuery.addXSS(this.factoryTemp.createQuery(line.trim()));
		}

		queries.add(currentQuery);
	}
}
