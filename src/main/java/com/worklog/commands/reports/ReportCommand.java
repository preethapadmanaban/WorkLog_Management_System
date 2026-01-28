package com.worklog.commands.reports;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.dto.ReportEmployeeDTO;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;
import com.worklog.servlets.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ReportCommand implements Command{
	
	private static final Logger logger = LogManager.getLogger(Controller.class);
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

			List<ReportEmployeeDTO> reports = timeSheetDAO.getReportDailyworkhours(fromDate, toDate, managerId)
							.orElse(new ArrayList<ReportEmployeeDTO>());

			if (request.getParameter("download") == null) {
				logger.info("Manager {} generated report from {} to {}", managerId, fromDate, toDate);
				if (reports != null) {
					request.setAttribute("report", reports);
					return true;
				} else {
					request.setAttribute("status", "error");
					request.setAttribute("message", "Exception occured");
					return true;
				}
			}
			else {
				try {
					logger.info("Manager {} downloaded report from {} to {}", managerId, fromDate, toDate);
					// set the header
					response.setContentType("text/csv");
					response.setCharacterEncoding("UTF-8"); // default - inline
					response.setHeader("Content-Disposition", "attachment; filename=\"employee_report.csv\"");

					PrintWriter out = response.getWriter();
					out.println("Employee Name, Work Date, Task Title, Task Duration, Task Notes");

					for (ReportEmployeeDTO report : reports) {
						out.printf("%s, %s, %s, %s, %s\n", report.getEmp_name(), report.getWork_date(), report.getTitle(),
										(report.getTask_duration() + " hours"), report.getNotes());
					}
					out.flush();
					return true;
				} catch (IOException e) {
					logger.error("Error while generating CSV report for manager {}", managerId, e);
					return false;
				}

			}
			

		}else {
			request.setAttribute("status", "error");
			request.setAttribute("message", "Invalid date");
			return false;
		}
	}
	
	

}
