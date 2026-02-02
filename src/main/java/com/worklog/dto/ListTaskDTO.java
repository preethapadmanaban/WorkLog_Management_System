package com.worklog.dto;

public class ListTaskDTO {

	private int id;
	private String title;
	private String description;
	private String managerName;
	private String status;
	private String deadline;
	private String created_at;

	public ListTaskDTO(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.description = builder.description;
		this.managerName = builder.managerName;
		this.status = builder.status;
		this.deadline = builder.deadline;
		this.created_at = builder.created_at;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public static class Builder {
		private int id;
		private String title;
		private String description;
		private String managerName;
		private String status;
		private String deadline;
		private String created_at;

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

		public Builder withStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder withManagerName(String managerName) {
			this.managerName = managerName;
			return this;
		}

		public Builder withDeadline(String deadline) {
			this.deadline = deadline;
			return this;
		}

		public Builder createdAt(String created_at) {
			this.created_at = created_at;
			return this;
		}

		public ListTaskDTO build() {
			return new ListTaskDTO(this);
		}
	}

}
