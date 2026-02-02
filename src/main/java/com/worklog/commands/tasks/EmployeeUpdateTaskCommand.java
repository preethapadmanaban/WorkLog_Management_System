package com.worklog.commands.tasks;

import java.io.IOException;
import java.io.PrintWriter;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeUpdateTaskCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);
		if (session == null || !((String) session.getAttribute("role")).equalsIgnoreCase("employee")) {
			throw new UnAuthorizedException("access_denied");
		}

		int task_id;

		try {
			task_id = Integer.parseInt(request.getParameter("task_id"));
		} catch (NumberFormatException e) {
			return false;
		}

		TaskDAO dao = new TaskDAO();

		if (request.getRequestURI().contains("/api")) {
			
			response.setContentType("application/json");
			PrintWriter out = null;
			try {
				out = response.getWriter();
			} catch (IOException e) {
				out.append("{\"status\" : \"error\", \"message\" : \"Invalid Id\"}");
				return false;
			}

			String status = request.getParameter("status");

			if (status == null) {
				out.append("{\"status\" : \"error\", \"message\" : \"Invalid Id.\"}");
				return false;
			}

			if (dao.updateTask(task_id, status) == true) {
				out.append("{\"status\" : \"success\", \"message\" : \"Task Updated\"}");
				return true;
			} else {
				out.append("{\"status\" : \"error\", \"message\" : \"Exception while updation!\"}");
				return false;
			}


		} else {

			Task task = dao.getTaskById(task_id).orElse(null);

			if (task == null) {
				return false;
			}

			request.setAttribute("task", task);

			return true;
		}
	}

}
