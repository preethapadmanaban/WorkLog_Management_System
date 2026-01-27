package com.worklog.dto;

import java.time.LocalDate;

public class ReportEmployeeDTO {
	private int emp_id;
	private String emp_name;
	private LocalDate work_date;
	private int task_id;
	private String title;
	private String notes;
	private double task_duration;

	public ReportEmployeeDTO() {
		super();
	}

	public int getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	public String getEmp_name() {
		return emp_name;
	}
	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}
	public int getTask_id() {
		return task_id;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getTask_duration() {
		return task_duration;
	}
	public void setTask_duration(double task_duration) {
		this.task_duration = task_duration;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDate getWork_date() {
		return work_date;
	}

	public void setWork_date(LocalDate work_date) {
		this.work_date = work_date;
	}
	

}
