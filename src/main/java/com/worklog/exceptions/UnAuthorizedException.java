package com.worklog.exceptions;

public class UnAuthorizedException extends Exception {

	private static final long serialVersionUID = -2953529230963527854L;

	public UnAuthorizedException(String msg) {
		super(msg);
	}
}
