package com.vibridi.fxmlutils.exception;

public class FXMLException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FXMLException(String msg) {
		super(msg);
	}
	
	public FXMLException(String msg, Exception e) {
		super(msg, e);
	}
}
