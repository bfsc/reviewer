package br.cin.ufpe.reviewer.persistence.exceptions;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = 3582407726873414234L;

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(String cause) {
		super(cause);
	}
	
}
