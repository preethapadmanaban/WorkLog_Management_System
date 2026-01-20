package com.worklog.dto;

public class TimeSheetEntryDTO {

	private int task_id;
	private String notes;
	private double hours_spent;

	public TimeSheetEntryDTO() {

	}


	public TimeSheetEntryDTO(int task_id, String notes, double hours_spent) {
		super();
		this.task_id = task_id;
		this.notes = notes;
		this.hours_spent = hours_spent;
	}

	public int getTask_id() {
		return task_id;
	}

	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public double getHours_spent() {
		return hours_spent;
	}

	public void setHours_spent(double hours_spent) {
		this.hours_spent = hours_spent;
	}

}