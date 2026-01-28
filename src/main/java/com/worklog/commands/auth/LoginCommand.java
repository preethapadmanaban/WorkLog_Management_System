package com.worklog.commands.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.worklog.entities.Employee;
import com.worklog.interfaces.Command;
import com.worklog.repositories.LoginDAO;
import com.worklog.utils.PasswordProtector;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements Command{
	 
	private static final int MAX_ATTEMPTS=3;

	private void setInfoInSession(HttpSession session, Employee employee) {
		session.setAttribute("id",employee.getId());
		session.setAttribute("name", employee.getName());
		session.setAttribute("role", employee.getRole());
		ServletContext context = session.getServletContext();
		@SuppressWarnings("unchecked")
		Map<String, String> logged_in_users = (Map<String, String>) context.getAttribute("logged_in_users");
		if(logged_in_users==null) {
			logged_in_users=new HashMap<String, String>();
			context.setAttribute("logged_in_users", logged_in_users);
		}		
	}

	private boolean isValidLoginRequest(HttpSession session) {
		Integer num_attempts = (Integer) session.getAttribute("num_attempts");
		if (num_attempts == null) {
			num_attempts = 1;
		}

		if (num_attempts < MAX_ATTEMPTS) {
			return true;
		} else {
			return false;
		}
	}

	public void addAttempt(HttpSession session) {
		Integer num_attempts = (Integer) session.getAttribute("num_attempts");
		if (num_attempts != null) {
			session.setAttribute("num_attempts", ++num_attempts);
		} else {
			session.setAttribute("num_attempts", 1);
		}
	}

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session=request.getSession();
		if (isValidLoginRequest(session)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			if (email != null && password != null) {
				LoginDAO loginDAO = new LoginDAO();
				Optional<Employee> emp = loginDAO.getDetails(email);
				if (emp.isPresent() && (PasswordProtector.checkPassword(password, emp.get().getPassword()))) {
					Employee employee = emp.get();
					setInfoInSession(session, employee);
					// for route this into routing command.
					request.setAttribute("action", "routing");
					// request.set
					return true;
				} else {
					request.setAttribute("message", "Invalid credentials!");
					addAttempt(session);
					return false;
				}
			}
			else {
				request.setAttribute("message","Invalid credentials!");
				addAttempt(session);
				return false;
			}
		}
		else {
			request.setAttribute("message", "Account locked, Max Attempts reached.");
			return false;
		}

	}
	 

}
