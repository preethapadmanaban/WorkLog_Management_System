package com.worklog.commands.timesheets;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
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
		String status = request.getParameter("status") == null ? "all" : request.getParameter("status");
		TimeSheetDAO timeSheetDAO = new TimeSheetDAO();
		List<TimeSheet> timeSheets = timeSheetDAO.getAllTimeSheetsForEmployee(id, status).orElse(new ArrayList<TimeSheet>());
		request.setAttribute("timesheets", timeSheets);
		return true;
	}
	

}
