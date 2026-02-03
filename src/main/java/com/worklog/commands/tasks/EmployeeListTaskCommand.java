package com.worklog.commands.tasks;

import com.worklog.config.AppConfig;
import com.worklog.dto.TaskResult;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeListTaskCommand implements Command {

	public static int rowsPerPage = AppConfig.getPropertyInt("app.pagination.rows-per-page");

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

		TaskResult tasksWithRowCount = repo.filterTasksByStatus(employeeId, status, pageNumber).orElse(null);

		if (tasksWithRowCount == null) {
			request.setAttribute("message", "No data found!");
			return false;
		}

		int totalPages = 1; // default pages
		int rowCount = tasksWithRowCount.getRowsCount();
		if (rowCount > rowsPerPage) {
			totalPages = rowCount / rowsPerPage;
			if (rowCount % rowsPerPage > 0) {
				++totalPages;
			}
		}
		request.setAttribute("tasks", tasksWithRowCount.getTasks());
		request.setAttribute("totalPages", totalPages);

		return true;
	}

}
