package fr.ensma.lias.tma4kb.query;

/**
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractSession implements Session {

	protected int executedQueryNumber = 0;
	protected float countQueryTime =0;
	protected float times[] = new float[11] ;
	
	public enum Counters {
		initialisation(0), 
		configCard(1), 
		parentsFIS(2), 
		computeCard(3),
		getSuperQueries(4), 
		isFailing(5), 
		decomposeCP(6), 
		updateFIS(7), 
		nextQueries(8), 
		varProp(9), 
		cardProp(10);

	     private int code;

	     private Counters(int code) {
	          this.code = code;
	     }

	     public int getCode() { return code; }
	};

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
	
	@Override
	public void addTimes(float time, Counters section) {
		times[section.getCode()]+=time;
	}
	@Override
	public float[] getTimes() {
		return times;
	}
	@Override
	public void clearTimes() {
		for (int i=0; i<times.length;i++)
			times[i]=0;
	}
	@Override
	public float getTime(Counters section) {
		return times[section.getCode()];
	}
}
