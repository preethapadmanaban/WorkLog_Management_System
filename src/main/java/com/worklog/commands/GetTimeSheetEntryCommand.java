package com.worklog.commands;

import java.util.List;
import java.util.Optional;

import com.worklog.entities.TimeSheetEntry;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetEntryDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GetTimeSheetEntryCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		int id=Integer.parseInt(request.getParameter("timeSheet_id")==null?"0":request.getParameter("timeSheet_id"));
		
		if(id>0) {
			TimeSheetEntryDAO timeSheetEntryDao=new TimeSheetEntryDAO();
			Optional<List<TimeSheetEntry>>optional=timeSheetEntryDao.getTimeSheetEntries(id);
			if(optional.isPresent()) {
				List<TimeSheetEntry> timeSheetEntries=optional.get();
				
				request.setAttribute("timeSheetEntries",timeSheetEntries);
				return true;
			}else {
				request.setAttribute("message", "not present id");
				return true;
			}
			
		}else {
			request.setAttribute("message","invalid credentials");
			return false;
		}
		
	}
	

}
