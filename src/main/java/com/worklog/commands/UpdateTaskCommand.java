package com.worklog.commands;

import java.sql.Date;

import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateTaskCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			String idStr = request.getParameter("id");
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String assignedStr = request.getParameter("assigned_to");
			String deadlineStr = request.getParameter("deadline");
			
			if(idStr == null) {
				return false;
			}
			int id = Integer.parseInt(idStr);
			
			if(title == null) {
				return false;
			}
			
			if(description == null) {
				return false;
			} 
			
			if(assignedStr == null) {
				return false;
			}
			int assigned_to = Integer.parseInt(assignedStr);
			
			if(deadlineStr == null) {
				return false;
			}
			Date deadline = Date.valueOf(deadlineStr);
			
			String status = request.getParameter("status");
			if(status == null) {
				return false;
			}
			
			TaskDAO dao = new TaskDAO();
			boolean updated = dao.updateTask(id, title, description, assigned_to, status, deadline);
			request.setAttribute("message", "Task Updated");
			
			return true;
		}
		return false;
	}

}
