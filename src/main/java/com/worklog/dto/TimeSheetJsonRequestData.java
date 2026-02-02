package com.worklog.dto;

import java.util.List;

public class TimeSheetJsonRequestData {
	private int manager_id;
	private String work_date;
	private List<TimeSheetEntryDTO> entries;

	public TimeSheetJsonRequestData() {

	}

	public TimeSheetJsonRequestData(int manager_id, String work_date, List<TimeSheetEntryDTO> entries) {
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

	public String getWork_date() {
		return work_date;
	}

	public void setWork_date(String work_date) {
		this.work_date = work_date;
	}

	public List<TimeSheetEntryDTO> getEntries() {
		return entries;
	}

	public void setEntries(List<TimeSheetEntryDTO> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "TimeSheetJsonRequestData [manager_id=" + manager_id + ", work_date=" + work_date + ", entries=" + entries + "]";
	}

}
