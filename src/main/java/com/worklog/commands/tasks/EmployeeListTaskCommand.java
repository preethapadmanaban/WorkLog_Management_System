package com.worklog.commands.tasks;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeListTaskCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("id") == null) {
			throw new UnAuthorizedException("access_denied");
		}

		int employeeId = (int) session.getAttribute("id");

		String status = request.getParameter("status") == null ? "all" : request.getParameter("status");
		int pageNumber = request.getParameter("pageNumber") == null ? 1 : Integer.parseInt(request.getParameter("pageNumber"));

		TaskDAO repo = new TaskDAO();
		List<Task> tasks;

		if (status == null || status.equalsIgnoreCase("all")) {
			tasks = repo.getAllTasksForEmployee(employeeId, false, pageNumber).orElse(new ArrayList<Task>());
		} else {
			tasks = repo.filterTasksByStatus(employeeId, status, pageNumber).orElse(new ArrayList<Task>());
		}

		request.setAttribute("tasks", tasks);

		return true;
	}

}
