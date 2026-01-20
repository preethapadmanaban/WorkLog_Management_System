package com.worklog.entities;

public class TimeSheetEntry {

	private int id;
	private int timesheet_id;
	private int task_id;
	private String notes;
	private double hours_spent;

	public TimeSheetEntry() {

	}

	public TimeSheetEntry(int timesheet_id, int task_id, String notes, double hours_spent) {
		this.timesheet_id = timesheet_id;
		this.task_id = task_id;
		this.notes = notes;
		this.hours_spent = hours_spent;
	}

	public TimeSheetEntry(int id, int timesheet_id, int task_id, String notes, double hours_spent) {
		this.id = id;
		this.timesheet_id = timesheet_id;
		this.task_id = task_id;
		this.notes = notes;
		this.hours_spent = hours_spent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimesheet_id() {
		return timesheet_id;
	}

	public void setTimesheet_id(int timesheet_id) {
		this.timesheet_id = timesheet_id;
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
