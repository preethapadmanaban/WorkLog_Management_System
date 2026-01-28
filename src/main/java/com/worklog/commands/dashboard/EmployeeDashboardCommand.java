package com.worklog.commands.dashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
 * @author Vasudevan Tamizharasan
 * @since 20-01-2026
 * 
 */
public class EmployeeDashboardCommand implements Command {
	
	private static final Logger logger = LogManager.getLogger(EmployeeDashboardCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("Unauthorized dashboard access attempt: no session");
			throw new UnAuthorizedException("access_denied");
		}

		Integer employeeId = session.getAttribute("id") != null ? (Integer) session.getAttribute("id") : -1;
		if (employeeId == -1) {
			request.setAttribute("message", "Access denied!");
			logger.warn("Employee dashboard access without valid session id");
			return false;
		}
		TaskDAO taskDao = new TaskDAO();
		Optional<List<Task>> listOfPendingTasks = taskDao.getAllPendingTasks(employeeId);

		List<Task> tasks;
		if (listOfPendingTasks.isPresent())
		{
			tasks = listOfPendingTasks.get();
		}
		else
		{
			request.setAttribute("message", "You have no pending tasks!");
			tasks = new ArrayList<Task>();
		}

		request.setAttribute("pending_tasks_array", tasks);

		return true;
	}


}
