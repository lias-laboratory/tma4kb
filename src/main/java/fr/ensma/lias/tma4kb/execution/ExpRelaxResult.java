package fr.ensma.lias.tma4kb.execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import fr.ensma.lias.tma4kb.query.Query;

/**
 * A class to get the results of the experiments on the algorithms
 * 
 * @author Stephane JEAN (jean@ensma.fr)
 * @author Ibrahim DELLAL (ibrahim.dellal@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 * @author Celia BORIES-GARCIA (celia.bories-garcia@etu.isae-ensma.fr)
 */
public class ExpRelaxResult {

	private Logger logger = Logger.getLogger(ExpRelaxResult.class);

	/**
	 * Constants for the metrics
	 */
	private static int ID_TIME = 1;

	private static int ID_NB_EXECUTED_QUERY = 2;

	private static int ID_COUNT_QUERY_TIME = 3;
	
	/**
	 * Number of execution of each algorithm
	 */
	protected int nbExecutions;

	protected List<Query> listOfQueries;

	protected Map<Query, QueryResult[]> resultsByQuery;

	public ExpRelaxResult(int nbExecutionQuery) {
		super();
		logger.setLevel(Level.DEBUG);
		this.nbExecutions = nbExecutionQuery;
		listOfQueries = new ArrayList<Query>();
		this.resultsByQuery = new HashMap<Query, QueryResult[]>();
	}

	public void addQueryResult(int i, Query q, float time, int nbExecutedQuery, float queryCountTime, float[] times) {

		QueryResult[] queryResults = resultsByQuery.get(q);
		if (queryResults == null) {
			queryResults = new QueryResult[nbExecutions];
			listOfQueries.add(q);
		}
		queryResults[i] = new QueryResult(time, nbExecutedQuery, queryCountTime, times);
		resultsByQuery.put(q, queryResults);
	}

	/***********************************************
	 * Methods to compute the average of the metrics
	 ***********************************************/

	/**
	 * Get the average of the computing time
	 * 
	 * @param q the query
	 * @return the average of the computing time
	 */
	public float getAvgTime(Query q) {
		return getAvgMetric(q, ID_TIME);
	}

	/**
	 * Get the average of the number of executed query
	 * 
	 * @param q the query
	 * @return the average of the number of executed query
	 */
	public float getAvgNbExecutedQuery(Query q) {
		return getAvgMetric(q, ID_NB_EXECUTED_QUERY);
	}

	/**
	 * Get the average of the counting time
	 * 
	 * @param q the query
	 * @return the average of the computing time
	 */
	public float getAvgCountTime(Query q) {
		return getAvgMetric(q, ID_COUNT_QUERY_TIME);
	}

	/**
	 * Computes the average of a given metrics
	 * 
	 * @param q        the query
	 * @param idMetric the given metric
	 * @return the average of the given metrics
	 */
	public float getAvgMetric(Query q, int idMetric) {
		float res = 0;
		QueryResult[] results = resultsByQuery.get(q);
		if (results != null) {
			for (int j = 0; j < results.length; j++) {
				if (idMetric == ID_TIME)
					res += results[j].getTime();
				else if (idMetric == ID_NB_EXECUTED_QUERY)
					res += results[j].getNbExecutedQuery();
				else if (idMetric == ID_COUNT_QUERY_TIME)
					res += results[j].getCountTime();
			}
		}
		return res / nbExecutions;
	}

	/**
	 * Look for the biggest gap between values of time and the average corresponding
	 * 
	 * @param q
	 * @param avgCountTime
	 * @return gap
	 */
	public float getBiggestGap(Query q, Float avgTime, int idMetric) {
		float gap = 0;
		float temp = 0;
		QueryResult[] results = resultsByQuery.get(q);
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				if (idMetric == ID_TIME) {
					temp = results[i].getTime();
				} else if (idMetric == ID_COUNT_QUERY_TIME) {
					temp = results[i].getCountTime();
				}
				float diff = Math.abs(avgTime - temp);
				if (diff > gap) {
					gap = diff;
				}
			}
		}
		return gap;
	}

	/**
	 * Round a float to certain number of decimals
	 */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
		return bd.floatValue();
	}

	/***************************************************
	 * Methods to display the results of the experiments
	 ***************************************************/

	/**
	 * Display the average over all executions of a query
	 */
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer("");

		for (int i = 0; i < listOfQueries.size(); i++) {
			Query q = listOfQueries.get(i);
			res.append("Q" + (i + 1) + "\t");
			Float valTime = round(getAvgTime(q), 2);
			res.append(valTime.toString() + "\t");
			Float bigGapExecTime = round(getBiggestGap(q, valTime, ID_TIME), 2);
			res.append(bigGapExecTime.toString() + "\t");
			int nbExecutedQuery = Math.round(getAvgNbExecutedQuery(q));
			res.append(nbExecutedQuery + "\t");
			Float countTime = round(getAvgCountTime(q), 2);
			res.append(countTime.toString() + "\t");
			Float bigGapCountTime = round(getBiggestGap(q, countTime, ID_COUNT_QUERY_TIME), 2);
			res.append(bigGapCountTime.toString() + "\n");
		}
		return res.toString();
	}
	
	/**
	 * Display every result
	 * @return
	 */
	public String allResults() {
		StringBuffer res = new StringBuffer("");
		for (int i = 0; i < listOfQueries.size(); i++) {
			Query q = listOfQueries.get(i);
			for (QueryResult r: resultsByQuery.get(q)) {
				res.append("Q" + (i + 1) + "\t");
				res.append(r.getTime() + "\t");
				res.append(r.getNbExecutedQuery() + "\t");
				res.append(r.getCountTime() + "\n");
			}
		}
		return res.toString();
	}

	/**
	 * Create a file with the average results of the experiments
	 * 
	 * @param descriExp the name of the file
	 */
	public void toFile(String descriExp) {
		BufferedWriter fichier;
		try {
			fichier = new BufferedWriter(new FileWriter(descriExp));
			fichier.write(toString());
			fichier.close();
		} catch (IOException e) {
			System.err.println("Unable to create the file with the experiment results.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Create a file with the results of all the experiments
	 * 
	 * @param descriExp the name of the file
	 */
	public void allResultsToFile(String descriExp) {
		BufferedWriter fichier;
		try {
			fichier = new BufferedWriter(new FileWriter(descriExp));
			fichier.write(allResults());
			fichier.close();
		} catch (IOException e) {
			System.err.println("Unable to create the file with the experiment results.");
			e.printStackTrace();
		}
	}
}
