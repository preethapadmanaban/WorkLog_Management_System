package com.worklog.entities;

public class TimeSheet {

	private int id;
	private int employee_id;
	private String work_date;
	private double total_hours;
	private String status;
	private int manager_id;
	private String manager_comment;
	private boolean approved;
	private String created_at;

	public TimeSheet() {

	}

	public TimeSheet(int employee_id, String work_date, double total_hours, String status, int manager_id, String manager_comment,
					boolean approved, String created_at) {
		this.employee_id = employee_id;
		this.work_date = work_date;
		this.total_hours = total_hours;
		this.status = status;
		this.manager_id = manager_id;
		this.manager_comment = manager_comment;
		this.approved = approved;
		this.created_at = created_at;
	}

	public TimeSheet(int id, int employee_id, String work_date, double total_hours, String status, int manager_id, String manager_comment,
					boolean approved, String created_at) {
		this.id = id;
		this.employee_id = employee_id;
		this.work_date = work_date;
		this.total_hours = total_hours;
		this.status = status;
		this.manager_id = manager_id;
		this.manager_comment = manager_comment;
		this.approved = approved;
		this.created_at = created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}

	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public double getTotal_hours() {
		return total_hours;
	}

	public void setTotal_hours(double total_hours) {
		this.total_hours = total_hours;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getManager_id() {
		return manager_id;
	}

	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}

	public String getManager_comment() {
		return manager_comment;
	}

	public void setManager_comment(String manager_comment) {
		this.manager_comment = manager_comment;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "TimeSheet [id=" + id + ", employee_id=" + employee_id + ", work_date=" + work_date + ", total_hours=" + total_hours
						+ ", status=" + status + ", manager_id=" + manager_id + ", manager_comment=" + manager_comment + ", approved="
						+ approved + ", created_at=" + created_at + "]";
	}

}
