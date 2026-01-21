package com.worklog.commands;

import java.io.IOException;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * EditTaskCommand - This class is used to edit the timesheet
 * @author Preetha
 * @since 20-01-2026
 */
public class EditTaskCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			String idStr = request.getParameter("id");

			if (idStr == null || idStr.trim().isEmpty()) {
				return false;
			}

			int id = Integer.parseInt(idStr);

			TaskDAO dao = new TaskDAO();
			Task task = dao.getTaskById(id).orElse(null);

			if (task == null) {
				return false;
			}

			request.setAttribute("task", task);
			
			RequestDispatcher ds = request.getRequestDispatcher("task_edit.jsp");
			try {
				ds.forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
			
			return true;
			
		}
		return false;
	}

}
