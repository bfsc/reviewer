package br.cin.ufpe.reviewer.core.exceptions;

public class CoreException extends RuntimeException {

	private static final long serialVersionUID = 7609558609618068778L;

	public CoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public CoreException(String message) {
		super(message);
	}
	
}
