package com.worklog.commands;

import java.time.LocalDate;
import java.util.Optional;

import com.worklog.entities.Report;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ReportCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session =request.getSession();
		Report report=null;
		int id=Integer.parseInt(session.getAttribute("id")==null?"0":(String)session.getAttribute("id"));
		LocalDate fromDate=LocalDate.parse(request.getAttribute("fromDate")==null?"0":(String)request.getAttribute("date"));
		LocalDate toDate=LocalDate.parse(request.getAttribute("toDate")==null?"0":(String)request.getAttribute("toDate"));
		if(id>0 && !(fromDate.isAfter(toDate))){
			String name=request.getParameter("employee");
			TimeSheetDAO timeSheetDAO=new TimeSheetDAO();
			Optional<Report> optional=null;
			if(name.equalsIgnoreCase("employee")) {
				
				optional=timeSheetDAO.getReportDailyworkhours(fromDate,toDate, id);
			}else if(name.equalsIgnoreCase("task")){
				optional=timeSheetDAO.getReportDailyworkhours(fromDate,toDate, id);
			}
			report=optional.get();
			if(report!=null) {
				request.setAttribute("report",report);
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
