package com.worklog.exceptions;

public class RequiredResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7750313428200186825L;

	public RequiredResourceNotFoundException(String msg) {
		super(msg);
	}

	public RequiredResourceNotFoundException(String msg, Throwable e) {
		super(msg, e);
	}

}
