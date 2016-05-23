package org.qcmix.exceptions;

public class BadFormatException extends Exception {
	public BadFormatException() {
		super("Le format spécifié est invalide");
	}
	
	public BadFormatException(String message) {
		super("Le format spécifié est invalide: " + message);
	}

}
