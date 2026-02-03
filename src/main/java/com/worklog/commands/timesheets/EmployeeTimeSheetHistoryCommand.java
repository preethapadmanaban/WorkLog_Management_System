package com.worklog.commands.timesheets;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class EmployeeTimeSheetHistoryCommand implements Command {

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
		List<TimeSheet> timeSheets = timeSheetDAO.getAllTimeSheetsForEmployee(id, status, pageNumber).orElse(new ArrayList<TimeSheet>());
		request.setAttribute("timesheets", timeSheets);

		int totalPages = 1;
		int rowCount = timeSheetDAO.getTimesheetCount(id, false);
		if (rowCount > TaskDAO.rowsPerPage) {
			totalPages = rowCount / TaskDAO.rowsPerPage;
			if (rowCount % TaskDAO.rowsPerPage > 0) {
				++totalPages;
			}
		}

		request.setAttribute("totalPages", totalPages);

		return true;
	}
	

}
