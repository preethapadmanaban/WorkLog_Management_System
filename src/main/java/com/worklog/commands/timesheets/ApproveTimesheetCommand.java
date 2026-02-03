package com.worklog.commands.timesheets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.worklog.constants.TimeSheetStatus;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ApproveTimesheetCommand - Approves a timesheet by manager
 * 
 * @author Preetha
 * @since 21-01-2026
 */
public class ApproveTimesheetCommand implements Command {

	private static final Logger logger = LoggerFactory.getLogger(ApproveTimesheetCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute("role") == null
						|| !session.getAttribute("role").toString().equalsIgnoreCase("manager")) {
			throw new UnAuthorizedException("access_denied");
		}

		String timesheetIdStr = request.getParameter("timesheetId");
		String commentStr = request.getParameter("manager_comment");

		if (timesheetIdStr == null || timesheetIdStr.isEmpty()) {
			return false;
		}

		if (commentStr == null || commentStr.trim().isEmpty()) {
			request.setAttribute("status", "error");
			request.setAttribute("message", "Please add a brief comment before approving the timesheet.");
			return false;
		}

		int timesheetId = Integer.parseInt(timesheetIdStr);
		int managerId = (int) session.getAttribute("id");

		TimeSheetDAO dao = new TimeSheetDAO();
		boolean updated = dao.updateTimesheetStatus(timesheetId, TimeSheetStatus.APPROVED.toString(), managerId, commentStr, true);

		if (updated == true) {
			logger.info("Manager {} approved timesheet {}", managerId, timesheetId);
			request.setAttribute("status", "success");
			request.setAttribute("message", "Timesheet approved successfully!");
		} else {
			logger.error("Failed to approve timesheet {} by manager {}", timesheetId, managerId);
		}

		return updated;
	}
}
