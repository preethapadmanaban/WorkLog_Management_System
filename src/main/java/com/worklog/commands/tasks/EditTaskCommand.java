package com.worklog.commands.tasks;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

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
	    
	    if (session == null){
	    	return false;
	    }

	    String role = (String) session.getAttribute("role");
	    
	    if (role == null) {
	    	return false;
	    }

	    String idStr = request.getParameter("task_id");
	    
	    if (idStr == null || idStr.isEmpty()) {
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
}
