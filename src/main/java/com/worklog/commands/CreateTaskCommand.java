package com.worklog.commands;

import java.sql.Date;

import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

	public class CreateTaskCommand implements Command{

		@Override
		public boolean execute(HttpServletRequest request, HttpServletResponse response) {
			
			HttpSession session = request.getSession(false);
			
			if(session == null) {
				return false;
			}
			
			String role = (String) session.getAttribute("role");
			
			if(role != null && role.equalsIgnoreCase("Manager")) {
				
				String title = request.getParameter("title");
				String description = request.getParameter("description");
				String assignedStr = request.getParameter("assigned_to");
				String deadlineStr = request.getParameter("deadline");
				
				if(title == null || title.trim().isEmpty()) {
					return false;
				}
				
				if(assignedStr == null || assignedStr.trim().isEmpty()) {
					return false;
				}
				int assigned_to = Integer.parseInt(assignedStr);
				
				if(deadlineStr == null || deadlineStr.trim().isEmpty()) {
					return false;
				}
				Date deadline = Date.valueOf(deadlineStr);
				
				String status = request.getParameter("status");
				
				if (status == null || status.trim().isEmpty()) {
					status = "Assigned";
				}
				
				TaskDAO dao = new TaskDAO();
				
				int createdBy = (int) session.getAttribute("id");

				boolean inserted = dao.createTask(title, description, assigned_to, status, deadline, createdBy);
				
				if(inserted) {
					return true;
				}
				return false;
		
			}
			
			return false;
		}

}
