package com.worklog.dto;

import java.util.List;

import com.worklog.entities.Task;

public class TaskResult {

	List<Task> tasks;
	int rowsCount;

	public TaskResult(List<Task> tasks, int rowsCount) {
		super();
		this.tasks = tasks;
		this.rowsCount = rowsCount;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public int getRowsCount() {
		return rowsCount;
	}

	public void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}



}
