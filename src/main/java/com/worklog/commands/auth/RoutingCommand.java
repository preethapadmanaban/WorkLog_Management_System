package com.worklog.commands.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.interfaces.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RoutingCommand implements Command{
	
	private static final Logger logger = LogManager.getLogger(RoutingCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
				
		HttpSession session = request.getSession(false);
//		
//		if(session == null) {
//			request.setAttribute("message", "Access denied!");
//			return false;
//		}
//		
		String role = (String) session.getAttribute("role");
		
		if(role == null) {
			 logger.warn("Routing attempt with missing role. sessionId={}", session.getId());
			request.setAttribute("message", "Access denied!");
			return false;
		}

		// see commands.xml to success and failure commands for login
		if (role.equalsIgnoreCase("manager")) {
			return true;
		} else {
			return false;
		}
	}
}
