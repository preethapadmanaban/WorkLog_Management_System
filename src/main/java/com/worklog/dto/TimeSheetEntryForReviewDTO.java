package com.worklog.dto;

public class TimeSheetEntryForReviewDTO {

	private int id;
	private String title;
	private String notes;
	private double hours_spent;

	public static class Builder {
		private int id;
		private String title;
		private String notes;
		private double hours_spent;

		public Builder() {

		}

		public Builder withId(int id) {
			this.id = id;
			return this;
		}

		public Builder withTaskTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withTaskNotes(String notes) {
			this.notes = notes;
			return this;
		}

		public Builder hoursSpent(double hoursSpent) {
			this.hours_spent = hoursSpent;
			return this;
		}

		public TimeSheetEntryForReviewDTO build() {
			return new TimeSheetEntryForReviewDTO(this);
		}

	}

	public TimeSheetEntryForReviewDTO(Builder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.hours_spent = builder.hours_spent;
		this.notes = builder.notes;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public double getHours_spent() {
		return hours_spent;
	}

	public void setHours_spent(double hours_spent) {
		this.hours_spent = hours_spent;
	}

}
