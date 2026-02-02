package com.worklog.entities;

import com.worklog.commands.constants.TaskStatus;

/**
 * This class is a entity class for task.
 * 
 * @author Vasudevan.
 * @since 19-01-2026
 * 
 */
public class Task {

	private int id;
	private String title;
	private String description;
	private int assigned_to;
	private TaskStatus status;
	private String deadline;
	private int created_by;
	private String created_at;
	private String updated_at;


	public Task(Task.Builder builder) {
		super();
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.assigned_to = builder.assigned_to;
		this.status = builder.status;
		this.deadline = builder.deadline;
		this.created_by = builder.created_by;
		this.created_at = builder.created_at;
		this.updated_at = builder.updated_at;

	}


	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getAssigned_to() {
		return assigned_to;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public String getDeadline() {
		return deadline;
	}

	public int getCreated_by() {
		return created_by;
	}

	public String getCreated_at() {
		return created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	// Builder class
	public static class Builder {
		private int id;
		private String title;
		private String description;
		private int assigned_to;
		private TaskStatus status;
		private String deadline;
		private int created_by;
		private String created_at;
		private String updated_at;

		public Builder() {

		}

		public Builder withId(int id) {
			this.id = id;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder assignedTo(int employeeId) {
			this.assigned_to = employeeId;
			return this;
		}

		public Builder setStatus(TaskStatus status) {
			this.status = status;
			return this;
		}

		public Builder withDeadline(String date) {
			this.deadline = date;
			return this;
		}

		public Builder createdBy(int employeeId) {
			this.created_by = employeeId;
			return this;
		}

		public Builder createdAt(String createdAt) {
			this.created_at = createdAt;
			return this;
		}

		public Builder updatedAt(String updateAt) {
			this.updated_at = updateAt;
			return this;
		}

		public Task build() {
			return new Task(this);
		}

	}
}
