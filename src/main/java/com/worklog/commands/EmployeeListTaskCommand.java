package com.worklog.commands;

import java.util.ArrayList;
import java.util.List;

import com.worklog.dto.ListTaskDTO;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeListTaskCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();

		String role = (String) session.getAttribute("role");
		int id = session.getAttribute("id") != null ? (int) session.getAttribute("id") : -1;
		if (id == -1 || role == null)
			return false;

		String status = request.getParameter("status") == null ? "all" : request.getParameter("status");
		TaskDAO repo = new TaskDAO();
		List<ListTaskDTO> tasks = repo.getTasksForEmployee(id, status).orElse(new ArrayList<ListTaskDTO>());
		request.setAttribute("tasks", tasks);

		return true;
	}

}
