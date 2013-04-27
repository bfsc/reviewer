package br.cin.ufpe.reviewer.importer.format.spi.exceptions;

public class ImporterFormatException extends Exception {
	
	private static final long serialVersionUID = 1345512736611675759L;

	public ImporterFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImporterFormatException(String message) {
		super(message);
	}

}
