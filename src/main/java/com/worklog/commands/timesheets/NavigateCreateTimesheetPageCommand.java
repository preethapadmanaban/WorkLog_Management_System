package com.worklog.commands.timesheets;

import java.util.Collections;
import java.util.List;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class NavigateCreateTimesheetPageCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("id") == null) {
			throw new UnAuthorizedException("access_denied");
		}

		int employeeId = (int) session.getAttribute("id");

		TaskDAO taskDAO = new TaskDAO();

		List<Task> tasks = taskDAO.getWorkingTasksByEmployee(employeeId).orElse(Collections.emptyList());

		request.setAttribute("tasks", tasks);

		return true;
	}

}
