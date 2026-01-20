package com.worklog.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * EmployeeDashboardCommand - this class is command class(service class). Holds the buisness logic for the employee dashboard.
 * 
 * @author Vasudevan Tamizharasan
 * @since 20-01-2026
 * 
 */
public class EmployeeDashboardCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		Integer employeeId = request.getSession().getAttribute("id") != null ? (int) request.getSession().getAttribute("id") : 0;
		if (employeeId == 0) {
			request.setAttribute("message", "invalid employee id.");
			return false;
		}
		TaskDAO taskDao = new TaskDAO();
		Optional<List<Task>> listOfTasks = taskDao.getAllTasksForEmployee(employeeId);

		List<Task> tasks;
		if (listOfTasks.isPresent())
			tasks = listOfTasks.get();
		else
			tasks = new ArrayList<Task>();

		request.setAttribute("task_array", tasks);

		return true;
	}


}
