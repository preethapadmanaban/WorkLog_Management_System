package com.worklog.entities;

import java.time.LocalDate;

public class Report {
	private int emp_id;
	private LocalDate workDate;
    private double totalHours;
    
	public Report() {
		super();
	}
	public Report(int emp_id, LocalDate workDate, double totalHours) {
		super();
		this.emp_id = emp_id;
		this.workDate = workDate;
		this.totalHours = totalHours;
	}
	public int getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}
	public LocalDate getWorkDate() {
		return workDate;
	}
	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}
	public double getTotalHours() {
		return totalHours;
	}
	
	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}
    
    

}
