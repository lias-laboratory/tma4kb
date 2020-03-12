package fr.ensma.lias.tma4kb.exception;

/**
 * Encapsulate all the exception coming from the triple store.
 * 
 * @author Louise PARKIN (louise.parkin@ensma.fr)
 */
public class TripleStoreException extends RuntimeException {

	private static final long serialVersionUID = -5005623190195013021L;

	public TripleStoreException() {
		super();
	}

	public TripleStoreException(String msg) {
		super(msg);
	}

}
