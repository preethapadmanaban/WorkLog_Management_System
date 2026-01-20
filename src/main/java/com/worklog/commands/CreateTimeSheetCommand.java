package com.worklog.commands;

import com.google.gson.Gson;
import com.worklog.dto.TimeSheetRequestDTO;
import com.worklog.entities.TimeSheet;
import com.worklog.interfaces.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * CreateTimeSheetCommand - This class is used for creating time sheets.
 * 
 * @author Vasudevan Tamizharasan
 * @since 20-02-2026
 * 
 */
public class CreateTimeSheetCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		String requestData = request.getParameter("timesheet");
		Gson gson = new Gson();
		TimeSheetRequestDTO timeSheetRequest = gson.fromJson(requestData, TimeSheetRequestDTO.class);
		TimeSheet timesheet = new TimeSheet();
		timesheet.setManager_id(timeSheetRequest.getManager_id());
		timesheet.setWork_date(timeSheetRequest.getWork_date());
		timesheet.setStatus("PENDING");
		timesheet.setEmployee_id(Integer.parseInt((String) request.getSession().getAttribute("id")));
		// timesheet.setTotal_hours();
		return false;
	}

}
