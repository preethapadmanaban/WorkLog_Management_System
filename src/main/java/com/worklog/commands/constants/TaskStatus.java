package com.worklog.commands.constants;

public enum TaskStatus {
	ASSIGNED("ASSIGNED"), IN_PROGRESS("IN PROGRESS"), COMPLETED("COMPLETED");

	private String displayValue;

	private TaskStatus(String value) {
		this.displayValue = value;
	}

	public String getDisplayValue() {
		return this.displayValue;
	}
}

