package com.worklog.commands;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
