package com.worklog.commands;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ListTasksCommand - This class is used to list all the tasks
 * @author Preetha
 * @since 20-01-2026
 */

public class ListTasksCommand implements Command{

	@Override 
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			TaskDAO dao = new TaskDAO();
			List<Task> taskList = dao.getAllTasks().orElse(new ArrayList<>());
			
			session.setAttribute("tasks", taskList);
			return true;
			
		}
		
		return false;
	}

}
