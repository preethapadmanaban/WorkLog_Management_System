package com.worklog.commands.reports;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.worklog.dto.ReportEmployeeDTO;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ReportCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);
		if (session == null) {
			throw new UnAuthorizedException("access_denied");
		}
		boolean isManager = ((String) session.getAttribute("role")).equalsIgnoreCase("manager");
		if (isManager == false) {
			throw new UnAuthorizedException("access_denied");
		}

		if (request.getParameter("filter") == null) {
			return false;
		}

		if (request.getParameter("fromDate") == null || request.getParameter("toDate") == null) {
			return false;
		}

		Integer managerId = (Integer) session.getAttribute("id");

		if (managerId == null) {
			return false;
		}

		LocalDate fromDate = LocalDate.parse(request.getParameter("fromDate"));
		LocalDate toDate = LocalDate.parse(request.getParameter("toDate"));

		if ((toDate.isAfter(fromDate))) {

			TimeSheetDAO timeSheetDAO=new TimeSheetDAO();
			// if(name.equalsIgnoreCase("employee")) {
			// optional=timeSheetDAO.getReportDailyworkhours(fromDate,toDate, managerId);
			// }else if(name.equalsIgnoreCase("task")){
			// optional=timeSheetDAO.getReportDailyworkhours(fromDate,toDate, managerId);
			// }

			List<ReportEmployeeDTO> reports = timeSheetDAO.getReportDailyworkhours(fromDate, toDate, managerId)
							.orElse(new ArrayList<ReportEmployeeDTO>());

			if (reports != null) {
				request.setAttribute("report", reports);
				return true;
			}else {
				request.setAttribute("message","id and date is no work");
				return true;
			}
			
		}else {
			request.setAttribute("message","invalid date");
			return false;
		}
	}
	
	

}
