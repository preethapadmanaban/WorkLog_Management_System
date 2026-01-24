package com.worklog.commands.timesheets;

import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.TimeSheet;
import com.worklog.entities.TimeSheetEntry;
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
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String) session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			String idStr = request.getParameter("id");
			if(idStr == null) {
				return false;
			}
			
			int id = Integer.parseInt(idStr);
			
			TimeSheetDAO dao1 = new TimeSheetDAO();
			TimeSheet ts = dao1.getTimesheetByid(id).orElse(null);
			
			if(ts == null) {
				return false;
			}
			
			TimeSheetEntryDAO dao2 = new TimeSheetEntryDAO();
			List<TimeSheetEntry> entries = dao2.getEntriesByTimesheetId(id).orElse(new ArrayList<>());
			
			request.setAttribute("timesheet", ts);
			request.setAttribute("entries", entries);
			
			return true;
		}
		
		return false;
	}

}
