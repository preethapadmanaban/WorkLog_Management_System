package com.worklog.commands;

import java.util.List;
import java.util.Optional;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ListPendingTasksCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Integer employeeId = session.getAttribute("id") != null ? Integer.parseInt((String) session.getAttribute("id")) : -1;
		if (employeeId == -1) {
			request.setAttribute("message", "Access denied!");
			return false;
		}
		TaskDAO taskRepo = new TaskDAO();
		Optional<List<Task>> listOfPendingTasks = taskRepo.getAllPendingTasks(employeeId);
		if (listOfPendingTasks.isPresent()) {
			return true;
		} else {
			request.setAttribute("message", "You have no pending tasks!");
			return false;
		}
	}

}
