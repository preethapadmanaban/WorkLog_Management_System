package com.worklog.commands.timesheets;

import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ApproveTimesheetCommand - This class is the logic to approve the timesheet
 * @author Preetha
 * @since 21-01-2026
 */
public class ApproveTimesheetCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("role") == null
						|| session.getAttribute("role").toString().equalsIgnoreCase("manager") == false) {
			throw new UnAuthorizedException("access_denied");
		}
		
		String timesheetIdStr = request.getParameter("timesheetId");
		String commentStr = request.getParameter("manager_comment");

		if (timesheetIdStr == null) {
			return false;
		}

		int timesheetId = Integer.parseInt(timesheetIdStr);

		if (commentStr == null) {
			commentStr = "";
		}

		int managerId = (int) session.getAttribute("id");

		TimeSheetDAO dao = new TimeSheetDAO();

		boolean updated = dao.updateTimesheetStatus(timesheetId, "approved", managerId, commentStr, true);

		return updated;

	}

}
