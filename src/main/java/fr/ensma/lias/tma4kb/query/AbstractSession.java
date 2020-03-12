package fr.ensma.lias.tma4kb.query;

/**
 * @author Mickael BARON (baron@ensma.fr)
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public abstract class AbstractSession implements Session {

	protected int executedQueryNumber = 0;

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
}
