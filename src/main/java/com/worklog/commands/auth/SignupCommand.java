package com.worklog.commands.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.worklog.entities.Employee;
import com.worklog.exceptions.DuplicateUserException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.utils.PasswordProtector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignupCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String role = request.getParameter("role");
		String password = request.getParameter("password");

		if (name == null || email == null || role == null || password == null) {
			request.setAttribute("message", "Invalid info, please enter correct information!");
			return false;
		}

		String hashedPassword = PasswordProtector.getHashedPassword(password);

		Employee employee = new Employee.Builder().withName(name).withEmail(email).withPassword(hashedPassword).withrole(role)
						.withActive(true)
						.withCreatedAt(Timestamp.valueOf(LocalDateTime.now())).withActive(true).build();


		EmployeeDAO employeeRepo = new EmployeeDAO();
		boolean flag;
		try {
			flag = employeeRepo.createEmployee(employee);
		} catch (DuplicateUserException e) {
			flag = false;
			request.setAttribute("message", e.getMessage());
		}

		if (flag == true) {
			return true;
		} else {
			return false;
		}
	}

}
