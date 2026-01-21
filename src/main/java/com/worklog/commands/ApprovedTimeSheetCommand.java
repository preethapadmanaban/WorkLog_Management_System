package com.worklog.commands;

import java.util.List;
import java.util.Optional;

import com.worklog.entities.TimeSheet;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ApprovedTimeSheetCommand  implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session=request.getSession();
		int id=Integer.parseInt(session.getAttribute("id")==null?"0":(String)session.getAttribute("id"));
		if(id>0) {
			TimeSheetDAO timeSheetDAO=new TimeSheetDAO();
			Optional<List<TimeSheet>> optional=timeSheetDAO.getAllApprovedTimeSheet(id);
			if(optional.isPresent()) {
				List<TimeSheet> timeSheets=optional.get();
				session.setAttribute("timeshetapproved", timeSheets);
				return true;
			}else {
				session.setAttribute("message","you have no approved timesheet");
				return true;
			}
		}else {
			session.setAttribute("message","invalid crediintials id ");
			return false;
		}
	}
	

}
