package com.worklog.commands.dashboard;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 
 * EmployeeDashboardCommand - this class is command class(service class). Holds the buisness logic for the employee dashboard.
 * 
 * @author Vasudevan
 * 
 */
public class EmployeeDashboardCommand implements Command {
	
	private static final Logger logger = LogManager.getLogger(EmployeeDashboardCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("id") == null) {
			logger.warn("Unauthorized dashboard access attempt: no session");
			throw new UnAuthorizedException("access_denied");
		}

		Integer employeeId = (Integer) session.getAttribute("id");

		TaskDAO taskDao = new TaskDAO();
		request.setAttribute("pending_tasks_array", taskDao.getAllTasksForEmployee(employeeId, true).orElse(new ArrayList<Task>()));
		return true;
	}


}
