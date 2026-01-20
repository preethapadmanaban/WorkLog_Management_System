package com.worklog.entities;

import java.sql.Timestamp;

public class Employee {
	
	private int id;
	private String name;
	private String role;
	private String email;
	private String password;
	private boolean active;
	private Timestamp created_at;
	
	public Employee() {
		
	}
	
	public Employee(Builder builder) {
		
		this.active=builder.active;
		this.created_at=builder.created_at;
		this.email=builder.email;
		this.id=builder.id;
		this.name=builder.name;
		this.role=builder.role;
		this.password=builder.password;
	}
	
	public static class Builder{
		private int id;
		private String name;
		private String role;
		private String password;
		private String email;
		private boolean active;
		private Timestamp created_at;
		public Builder() {
			super();
		}
		public Builder withId(int id) {
			this.id=id;
			return this;
		}
		public Builder withName(String name) {
			this.name=name;
			return this;
		}
		public Builder withEmail(String email) {
			this.email=email;
			return this;
		}
		public Builder withrole(String role) {
			this.role=role;
			return this;
		}
		public Builder withActive(boolean active) {
			this.active=active;
			return this;
		}
		public Builder withPassword(String password) {
			this.password=password;
			return this;
		}
		public Builder withCreatedAt(Timestamp created_at) {
			this.created_at=created_at;
			return this;
		}
		public Employee build() {
			return new Employee(this);
		}
		
		
		
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Timestamp getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Timestamp created_at) {
		this.created_at = created_at;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
