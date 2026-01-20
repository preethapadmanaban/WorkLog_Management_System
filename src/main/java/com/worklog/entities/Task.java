package com.worklog.entities;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * This class is a entity class for task.
 * 
 * @author Vasudevan.
 * @since 19-02-2026
 * 
 */
public class Task {

	private int id;
	private String title;
	private String description;
	private int assigned_to;
	private String status;
	private LocalDate deadline;
	private int created_by;
	private Timestamp created_at;
	private Timestamp updated_at;

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

	public String description() {
		return description;
	}

	public int getAssigned_to() {
		return assigned_to;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public int getCreated_by() {
		return created_by;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public Timestamp getUpdated_at() {
		return updated_at;
	}

	// Builder class
	public static class Builder {
		private int id;
		private String title;
		private String description;
		private int assigned_to;
		private String status;
		private LocalDate deadline;
		private int created_by;
		private Timestamp created_at;
		private Timestamp updated_at;

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

		public Builder setStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder withDeadline(LocalDate date) {
			this.deadline = date;
			return this;
		}

		public Builder createdBy(int employeeId) {
			this.created_by = employeeId;
			return this;
		}

		public Builder createdAt(Timestamp createdAt) {
			this.created_at = createdAt;
			return this;
		}

		public Builder updatedAt(Timestamp updateAt) {
			this.updated_at = updateAt;
			return this;
		}

		public Task build() {
			return new Task(this);
		}

	}
}
