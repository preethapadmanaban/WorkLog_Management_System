package com.worklog.exceptions;

public class CommandNotFoundException extends Exception {

	private static final long serialVersionUID = -4408453310184966215L;

	public CommandNotFoundException(String msg) {
		super(msg);
	}

	public CommandNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
