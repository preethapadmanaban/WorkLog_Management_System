package com.worklog.dto;

import java.time.LocalDate;

public class TimeSheetRequestDTO {
	
	private int manager_id;
	private LocalDate work_date;
	private TimeSheetEntryDTO[] entries;

	public TimeSheetRequestDTO() {

	}

	public TimeSheetRequestDTO(int manager_id, LocalDate work_date, TimeSheetEntryDTO[] entries) {
		super();
		this.manager_id = manager_id;
		this.work_date = work_date;
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

	public TimeSheetEntryDTO[] getEntries() {
		return entries;
	}

	public void setEntries(TimeSheetEntryDTO[] entries) {
		this.entries = entries;
	}


}
