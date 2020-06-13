package fr.ensma.lias.tma4kb.query;

/**
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractSession implements Session {

	protected int executedQueryNumber = 0;
	protected float countQueryTime =0;

	@Override
	public int getExecutedQueryCount() {
		return executedQueryNumber;
	}

	@Override
	public void setExecutedQueryCount(int value) {
		this.executedQueryNumber = value;
	}

	@Override
	public void clearExecutedQueryCount() {
		executedQueryNumber = 0;
	}
	
	@Override
	public float getCountQueryTime() {
		return countQueryTime;
	}

	@Override
	public void setCountQueryTime(float value) {
		this.countQueryTime = value;
	}

	@Override
	public void clearCountQueryTime() {
		countQueryTime = 0;
	}
}
