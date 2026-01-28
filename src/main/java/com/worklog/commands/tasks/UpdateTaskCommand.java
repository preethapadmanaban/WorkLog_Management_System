package com.worklog.commands.tasks;

import java.sql.Date;

import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * UpdateTaskCommand - This class is used to update the timesheet
 * @author Preetha
 * @since 21-01-2026
 */

public class UpdateTaskCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			System.out.println("leaving update task 1");
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if (role != null) {
			
			String idStr = request.getParameter("id");
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String assignedStr = request.getParameter("assigned_to");
			String deadlineStr = request.getParameter("deadline");
			
			// System.out.println("Update task data: " + idStr + " " + title + " " + description + " " + assignedStr + " " + deadlineStr);

			if(idStr == null) {
				// System.out.println("leaving update task 2");
				return false;
			}
			int id = Integer.parseInt(idStr);
			
			if(title == null) {
				request.setAttribute("message", "Invalid title!");
				// System.out.println("leaving update task 3");
				return false;
			}
			
			if(description == null) {
				request.setAttribute("message", "Invalid description!");
				// System.out.println("leaving update task 4");
				return false;
			} 
			
			if(assignedStr == null) {
				request.setAttribute("message", "Invalid assignment employee!");
				// System.out.println("leaving update task 5");
				return false;
			}
			int assigned_to = Integer.parseInt(assignedStr);
			
			if(deadlineStr == null) {
				request.setAttribute("message", "Invalid deadline!");
				// System.out.println("leaving update task 6");
				return false;
			}
			Date deadline = Date.valueOf(deadlineStr);
			
			String status = request.getParameter("status");
			if(status == null) {
				request.setAttribute("message", "Invalid task status!");
				// System.out.println("leaving update task 7");
				return false;
			}
			
			TaskDAO dao = new TaskDAO();
			boolean updated = dao.updateTask(id, title, description, assigned_to, status, deadline);
			if (updated == true) {
				request.setAttribute("status", "success");
				request.setAttribute("message", "Task updated successfully!");
				return true;
			} else {
				request.setAttribute("status", "error");
				request.setAttribute("message", "Task Updation failed!");
				// System.out.println("leaving update task 8");
				return false;
			}
		}
		// System.out.println("leaving update task 9");
		return false;
	}

}
