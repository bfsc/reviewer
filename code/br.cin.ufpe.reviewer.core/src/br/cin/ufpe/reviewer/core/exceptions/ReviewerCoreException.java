package br.cin.ufpe.reviewer.core.exceptions;

public class ReviewerCoreException extends RuntimeException {

	private static final long serialVersionUID = 7609558609618068778L;

	public ReviewerCoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReviewerCoreException(String message) {
		super(message);
	}
	
}
