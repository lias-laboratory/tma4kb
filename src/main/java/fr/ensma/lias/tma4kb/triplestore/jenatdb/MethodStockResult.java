package fr.ensma.lias.tma4kb.triplestore.jenatdb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author Célia Bories-Garcia (celia.bories-garcia@etu.isae-ensma.fr)
 *
 */
public class MethodStockResult {
	// Constants for the metrics
	private static int ID_ANSWERS = 1;

	private static int ID_COUNT_QUERY_TIME = 2;

	private Logger logger = Logger.getLogger(MethodStockResult.class);

	protected int nbExec;

	protected int nbK;

	protected List<String> listOfMethods;

	protected List<Integer> listOfK;

	protected Map<String, MethodResult[][]> resultsForMethods;

	/**
	 * Constructor
	 * 
	 * @param nbExecution
	 */
	public MethodStockResult(int nbExecution, int nbThreshold) {
		super();
		logger.setLevel(Level.DEBUG);
		this.nbExec = nbExecution;
		this.nbK = nbThreshold;
		listOfMethods = new ArrayList<String>();
		listOfK = new ArrayList<Integer>();
		this.resultsForMethods = new HashMap<String, MethodResult[][]>();
	}

	/**
	 * Adding of results, here number of answers, counting time for given query and
	 * threshold
	 * 
	 * @param i
	 * @param q
	 * @param nbAnswers
	 * @param queryCountTime
	 * @param K
	 */
	public void addResult(int i, int j, String m, int nbAnswers, float queryCountTime, int k) {

		MethodResult[][] queryResults = resultsForMethods.get(m);
		if (queryResults == null) {
			queryResults = new MethodResult[nbExec][];
			listOfMethods.add(m);
			for (int r = 0; r < nbExec; r++) {
				if (m == "ALL" || m == "COUNT") {
					queryResults[r] = new MethodResult[1];
				}
				queryResults[r] = new MethodResult[nbK];
			}
		}
		if (!listOfK.contains(k)) {
			listOfK.add(k);
		}

		queryResults[i][j] = new MethodResult(nbAnswers, queryCountTime, k);
		resultsForMethods.put(m, queryResults);
	}

	/**
	 * Get number of answers and the average of the counting time
	 * 
	 * @param m the query
	 * @return the average of time and answers
	 */
	public float getAvgCountTime(String m, int indexK) {
		return getAvg(m, ID_COUNT_QUERY_TIME, indexK);
	}

	public float getAnswers(String m, int indexK) {
		return getAvg(m, ID_ANSWERS, indexK);
	}

	public float getAvg(String m, int idMetric, int j) {
		float res = 0;
		MethodResult[][] methodResults = resultsForMethods.get(m);
		if (methodResults != null) {
			for (int i = 0; i < nbExec; i++) {
				if (idMetric == ID_ANSWERS)
					res += methodResults[i][j].getNbAnswers();
				else if (idMetric == ID_COUNT_QUERY_TIME)
					res += methodResults[i][j].getCountTime();
			}
		}
		return res / nbExec;
	}

	/***************************************************
	 * Methods to display the results of the experiments
	 ***************************************************/

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer("");
		System.out.println(listOfK);
		System.out.println(listOfMethods);
		for (int i = 0; i < listOfMethods.size(); i++) {
			for (int j = 0; j < listOfK.size(); j++) {
				String m = listOfMethods.get(i);
				res.append(m + "\t");
				int nombre = listOfK.get(j);
				res.append(nombre + "\t");
				Float val = round(getAnswers(m, j), 2);
				res.append(val.toString() + "\t");
				Float countTime = round(getAvgCountTime(m, j), 2);
				res.append(countTime.toString() + "\n");
			}
		}
		return res.toString();
	}

	/**
	 * Round a float to certain number of decimals
	 */
	public static float round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	/**
	 * Create a file with the results of the experiments
	 * 
	 */
	public void toFile(String name) {
		BufferedWriter fichier;
		try {
			fichier = new BufferedWriter(new FileWriter(name));
			fichier.write(toString());
			fichier.close();
		} catch (IOException e) {
			System.err.println("Unable to create the file with the experiment results.");
			e.printStackTrace();
		}
	}
}
