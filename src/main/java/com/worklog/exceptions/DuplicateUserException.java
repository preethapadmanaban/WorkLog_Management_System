package com.worklog.exceptions;

public class DuplicateUserException extends Exception {

	private static final long serialVersionUID = -6667688039666833653L;

	public DuplicateUserException(String msg) {
		super(msg);
	}

}
