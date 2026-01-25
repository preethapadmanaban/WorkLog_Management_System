package com.worklog.commands.timesheets;

import java.util.ArrayList;
import java.util.List;

import com.worklog.dto.TimeSheetEntryForReviewDTO;
import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;
import com.worklog.repositories.TimeSheetEntryDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * TimesheetReviewCommand - This class is used to review the timesheet
 * @author Preetha
 * @since 21-01-2026
 */

public class TimesheetReviewCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		
		HttpSession session = request.getSession(false);
		
		if (session == null || session.getAttribute("role") == null) {
			throw new UnAuthorizedException("access_denied");
		}
			
		String idStr = request.getParameter("timesheetId");
		if (idStr == null) {
			return false;
		}

		int timesheetId = Integer.parseInt(idStr);

		TimeSheetDAO dao1 = new TimeSheetDAO();
		TimeSheet ts = dao1.getTimesheetByid(timesheetId).orElse(null);

		if (ts == null) {
			request.setAttribute("status", "error");
			request.setAttribute("message", "Invalid timesheet id.");
			return false;
		}

		TimeSheetEntryDAO dao2 = new TimeSheetEntryDAO();
		List<TimeSheetEntryForReviewDTO> entries = dao2.getEntriesByTimesheetId(timesheetId)
						.orElse(new ArrayList<TimeSheetEntryForReviewDTO>());

		request.setAttribute("timesheet", ts);
		request.setAttribute("entries", entries);

		return true;

	}

}
