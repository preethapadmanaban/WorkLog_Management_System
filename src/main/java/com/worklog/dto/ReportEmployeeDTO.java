package com.worklog.dto;


public class ReportEmployeeDTO {
	private int emp_id;
	private String emp_name;
	private int task_id;
	private String title;
	private double task_duration;
	public ReportEmployeeDTO(int emp_id, String emp_name, int task_id, String title, double task_duration) {
		super();
		this.emp_id = emp_id;
		this.emp_name = emp_name;
		this.task_id = task_id;
		this.title = title;
		this.task_duration = task_duration;
	}
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
	

}
