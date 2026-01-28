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

/**
 * TimesheetPendingCommand - This class gives the pending timesheet
 * @author Preetha
 * @since 21-01-2026
 */
public class TimesheetPendingCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			throw new UnAuthorizedException("access_denied");
		}
		
		String role = (String)session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			TimeSheetDAO dao = new TimeSheetDAO();
			List<TimeSheet> list = dao.getPendingTimesheet().orElse(new ArrayList<>());
			request.setAttribute("pending", list);
			return true;

		}
		return false;
	}

}
