package com.worklog.commands.timesheets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.worklog.commands.constants.TimeSheetStatus;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class RejectTimesheetCommand implements Command {

	private static final Logger logger = LoggerFactory.getLogger(RejectTimesheetCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UnAuthorizedException("access_denied");
		}

		String role = (String) session.getAttribute("role");

		if (role != null && role.equalsIgnoreCase("Manager")) {

			String timesheetIdStr = request.getParameter("timesheetId");
			String commentStr = request.getParameter("manager_comment");

			if (timesheetIdStr == null) {
				return false;
			}

			int timesheetId = Integer.parseInt(timesheetIdStr);

			if (commentStr == null || commentStr.trim().isEmpty()) {
				request.setAttribute("status", "error");
				request.setAttribute("message", "Please add a brief comment before rejecting the timesheet.");
				return false;
			}

			int managerId = (int) session.getAttribute("id");

			TimeSheetDAO dao = new TimeSheetDAO();

			boolean updated = dao.updateTimesheetStatus(timesheetId, TimeSheetStatus.REJECTED.toString(), managerId, commentStr, false);

			if (updated == true) {
				logger.info("Manager {} rejected timesheet {}", managerId, timesheetId);
				request.setAttribute("status", "success");
				request.setAttribute("message", "Timesheet rejected successfully");
			} else {
				logger.error("Failed to reject timesheet {} by manager {}", timesheetId, managerId);
			}

			return updated;
		}
		return false;
	}

}
