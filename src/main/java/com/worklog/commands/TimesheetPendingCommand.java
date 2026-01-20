package com.worklog.commands;

import com.worklog.interfaces.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class TimesheetPendingCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			return true;
		}
		return false;
	}

}
