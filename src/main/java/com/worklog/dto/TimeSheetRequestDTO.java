package com.worklog.dto;

import java.time.LocalDate;
import java.util.List;

public class TimeSheetRequestDTO {
	
	private int manager_id;
	private LocalDate work_date;
	private double total_hours;
	private List<TimeSheetEntryDTO> entries;

	public TimeSheetRequestDTO() {

	}

	public TimeSheetRequestDTO(int manager_id, LocalDate work_date, double total_hours, List<TimeSheetEntryDTO> entries) {
		super();
		this.manager_id = manager_id;
		this.work_date = work_date;
		this.total_hours = total_hours;
		this.entries = entries;
	}

	public int getManager_id() {
		return manager_id;
	}

	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}

	public LocalDate getWork_date() {
		return work_date;
	}

	public void setWork_date(LocalDate work_date) {
		this.work_date = work_date;
	}

	public List<TimeSheetEntryDTO> getEntries() {
		return entries;
	}

	public void setEntries(List<TimeSheetEntryDTO> entries) {
		this.entries = entries;
	}


	public double getTotal_hours() {
		return total_hours;
	}

	public void setTotal_hours(double total_hours) {
		this.total_hours = total_hours;
	}

	@Override
	public String toString() {
		return "TimeSheetRequestDTO [manager_id=" + manager_id + ", work_date=" + work_date + ", total_hours=" + total_hours + ", entries="
						+ entries + "]";
	}

}
