package com.worklog.commands.auth;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

import com.worklog.entities.Employee;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.utils.PasswordProtector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignupCommand implements Command {
	private static final Logger logger = LogManager.getLogger(SignupCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		// String role = request.getParameter("role");
		// role = "employee";
		String role = "employee";
		String password = request.getParameter("password");

		if (name == null || email == null || role == null || password == null) {
			request.setAttribute("message", "Invalid info, Please enter valid information!");
			logger.error("Exception, signup request failed: inputs - name : " + name + ", email: " + email + ", role: " + role
							+ ", passowrd: " + password);
			return false;
		}

		String hashedPassword = PasswordProtector.getHashedPassword(password);

		Employee employee = new Employee.Builder().withName(name).withEmail(email).withPassword(hashedPassword).withrole(role)
						.withCreatedAt(Timestamp.valueOf(LocalDateTime.now())).withActive(true).build();

		EmployeeDAO employeeRepo = new EmployeeDAO();
		try {
			if (employeeRepo.createEmployee(employee) == true) {
				logger.info("New User created, email:" + email);
				return true;
			} else {
				return false;
			}

		} catch (PSQLException e) {
			ServerErrorMessage errorMessage = e.getServerErrorMessage();
			if (errorMessage != null && errorMessage.getSQLState() != null
							&& (errorMessage.getSQLState().startsWith("23") || errorMessage.getSQLState().equals("23505"))) {
				logger.error("Exception while creating new user, Unique_constraint_violation error.", e);
				request.setAttribute("message", "Email already exists.");
				return false;
			} else {
				logger.error("Exception while creating new user, PSQLException.", e);
				return false;
			}
		} catch (SQLException e) {
			return false;
		}

	}

}
