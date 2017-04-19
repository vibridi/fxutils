package com.vibridi.fxu.exception;

public class FXException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FXException(String msg) {
		super(msg);
	}
	
	public FXException(String msg, Exception e) {
		super(msg, e);
	}
}
