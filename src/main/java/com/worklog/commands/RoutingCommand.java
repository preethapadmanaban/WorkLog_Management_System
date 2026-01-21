package com.worklog.commands;

import java.io.IOException;

import com.worklog.interfaces.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RoutingCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String) session.getAttribute("role");
		
		if(role == null) {
			return false;
		}
		
		try {
			
			if(role.equalsIgnoreCase("manager")) {
				response.sendRedirect("manager_dashboard.jsp");
			}
			else if(role.equalsIgnoreCase("employee")) {
				response.sendRedirect("employee_dashboard.jsp");
			}
			else {
				response.sendRedirect("login.jsp");
			}
			
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
