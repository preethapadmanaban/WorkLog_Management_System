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

public class TimesheetReviewCommand implements Command {

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("role") == null) {
            throw new UnAuthorizedException("access_denied");
        }

		// String role = (String) session.getAttribute("role");
		// if (!"Manager".equalsIgnoreCase(role)) {
		// throw new UnAuthorizedException("access_denied");
		// }

        String idStr = request.getParameter("timesheetId");   // MUST match URL param
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("message", "Timesheet id missing!");
            return false;
        }

        int timesheetId = Integer.parseInt(idStr);

        TimeSheetDAO tsDao = new TimeSheetDAO();
        TimeSheet ts = tsDao.getTimesheetByid(timesheetId).orElse(null);

        if (ts == null) {
			request.setAttribute("status", "error");
            request.setAttribute("message", "Invalid timesheet id!");
            return false;
        }

        TimeSheetEntryDAO entryDao = new TimeSheetEntryDAO();
        List<TimeSheetEntryForReviewDTO> entries = entryDao.getEntriesByTimesheetId(timesheetId).orElse(new ArrayList<>());

        request.setAttribute("timesheet", ts);
        request.setAttribute("entries", entries);

        return true;
    }
}
