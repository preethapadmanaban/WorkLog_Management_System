package com.worklog.exceptions;

public class DuplicateTimesheetCreationException extends Exception {

	private static final long serialVersionUID = 4378743357945387794L;

	public DuplicateTimesheetCreationException(String msg) {
		super(msg);
	}

	public DuplicateTimesheetCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
