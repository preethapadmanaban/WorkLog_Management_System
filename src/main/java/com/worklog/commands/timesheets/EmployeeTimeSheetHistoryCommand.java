package com.worklog.commands.timesheets;

import com.worklog.config.AppConfig;
import com.worklog.dto.ListResultWithRowCount;
import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeTimeSheetHistoryCommand implements Command {

	public static int rowsPerPage = AppConfig.getPropertyInt("app.pagination.rows-per-page");

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("id") == null) {
			throw new UnAuthorizedException("access_denied");
		}

		int id = (int) session.getAttribute("id");
		String status = request.getParameter("status") == null ? "all" : request.getParameter("status");//if filter is not selected default all
		int pageNumber = request.getParameter("pageNumber") == null ? 1 : Integer.parseInt(request.getParameter("pageNumber"));
		TimeSheetDAO timeSheetDAO = new TimeSheetDAO();
		ListResultWithRowCount<TimeSheet> timeSheetWithRowCount = timeSheetDAO.getAllTimeSheetsForEmployee(id, status, pageNumber)
						.orElse(null);

		if (timeSheetWithRowCount == null) {
			request.setAttribute("message", "Unable to find Data!");
			return false;
		}

		request.setAttribute("timesheets", timeSheetWithRowCount.getTasks());

		int totalPages = 1;
		int rowCount = timeSheetWithRowCount.getRowsCount();
		if (rowCount > rowsPerPage) {
			totalPages = rowCount / rowsPerPage;
			if (rowCount % rowsPerPage > 0) {
				++totalPages;
			}
		}

		request.setAttribute("totalPages", totalPages);

		return true;
	}
	

}
