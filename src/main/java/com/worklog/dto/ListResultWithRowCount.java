package com.worklog.dto;

import java.util.List;

public class ListResultWithRowCount<T> {

	List<T> listOfValues;
	int rowsCount;

	public ListResultWithRowCount(List<T> listOfValues, int rowsCount) {
		this.listOfValues = listOfValues;
		this.rowsCount = rowsCount;
	}

	public List<T> getTasks() {
		return listOfValues;
	}

	public void setTasks(List<T> listOfValues) {
		this.listOfValues = listOfValues;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}



}
