package com.worklog.commands.tasks;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ViewTaskCommand - This class is used to view the task
 * @author Preetha
 * @since 20-01-2026
 */
public class ViewTaskCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			throw new UnAuthorizedException("access_denied");
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
			
			return true;
		}
		
		return false;
	}

}
