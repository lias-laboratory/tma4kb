package fr.ensma.lias.tma4kb.triplestore.jenatdb;

public class MethodResult {

	private int nbAnswers;

	private float queryCountTime;

	private int K;

	public MethodResult(int nbAnswers, float countTime, int k) {
		super();
		this.queryCountTime = countTime;
		this.nbAnswers = nbAnswers;
		this.K = k;
	}

	public int getNbAnswers() {
		return nbAnswers;
	}

	public float getCountTime() {
		return queryCountTime;
	}

	public int getK() {
		return K;
	}
}
