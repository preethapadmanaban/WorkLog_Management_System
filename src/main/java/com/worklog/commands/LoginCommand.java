package com.worklog.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.worklog.entities.Employee;
import com.worklog.interfaces.Command;
import com.worklog.repositories.LoginDAO;
import com.worklog.utils.PasswordProtector;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCommand implements Command{
	 
	private static final int MAX_ATTEMPTS=3;
	private void setInfoInSession(HttpSession session, Employee employee) {
		session.setAttribute("id",employee.getId());
		session.setAttribute("name", employee.getName());
		session.setAttribute("role", employee.getRole());
		HttpServletRequest request=(HttpServletRequest) session.getServletContext();
		@SuppressWarnings("unchecked")
		Map<String,String> logged_in_users=(Map<String, String>)request.getServletContext().getAttribute("logged_in_users");
		if(logged_in_users==null) {
			logged_in_users=new HashMap<String, String>();
			request.getServletContext().setAttribute("logged_in_users", logged_in_users);
			
		}
		
	}

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		HttpSession session=request.getSession();
		Integer num_attempts=(Integer)session.getAttribute("num_attempts");
		if(num_attempts==null) {
			num_attempts=0;
		}
		if(email!=null && password!=null) {
			LoginDAO loginDAO=new LoginDAO();
			Optional<Employee> emp=loginDAO.getDetails(email);
			if(emp.isPresent() && (PasswordProtector.checkPassword(password, emp.get().getPassword()))) {
				Employee employee=emp.get();
				setInfoInSession(session,employee);
				return true;
			}
			else {
//				num_attempts++;
//				
//				if(num_attempts<MAX_ATTEMPTS) {
//					return false;
//					
//				}else {
//					
//				}
				request.setAttribute("message","Invalid credentials!");
				return false;
			}
		}
		else {
			request.setAttribute("message","invalid credentials");
			return false;
		}
	}
	 

}
