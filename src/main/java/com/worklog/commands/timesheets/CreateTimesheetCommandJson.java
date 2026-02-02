package com.worklog.commands.timesheets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.worklog.commands.constants.TimeSheetStatus;
import com.worklog.dto.TimeSheetEntryDTO;
import com.worklog.dto.TimeSheetJsonRequestData;
import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.DuplicateTimesheetCreationException;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;
import com.worklog.repositories.TimeSheetEntryDAO;
import com.worklog.utils.LocalDateAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreateTimesheetCommandJson implements Command {

	private static final Logger logger = LoggerFactory.getLogger(CreateTimesheetCommandJson.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("id") == null) {
			throw new UnAuthorizedException("Access denied.");
		}

		int employeeId = (int) session.getAttribute("id");

		if (request.getRequestURI().contains("/api")) {

			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();

			PrintWriter out = null;
			try {
				out = response.getWriter();
				TimeSheetJsonRequestData timeSheetRequest = gson.fromJson(request.getReader(), TimeSheetJsonRequestData.class);

				// validate the input
				double total_hours_spent = 0.0;
				for (TimeSheetEntryDTO entry : timeSheetRequest.getEntries()) {
					total_hours_spent += entry.getHours_spent();
				}

				// create a new timesheet entity object from timesheet request dto
				TimeSheet timesheet = new TimeSheet();
				timesheet.setManager_id(timeSheetRequest.getManager_id());
				timesheet.setWork_date(timeSheetRequest.getWork_date());
				timesheet.setStatus(TimeSheetStatus.PENDING.toString());
				timesheet.setEmployee_id(employeeId);
				timesheet.setTotal_hours(total_hours_spent);

				System.out.println(timeSheetRequest);

				//return createTimesheet(request, response, timesheet, employeeId, requestObj.getWork_date(), requestObj);
				
				TimeSheetDAO repo = new TimeSheetDAO();
				boolean flag;
				try {
					flag = repo.createTimeSheet(timesheet);
				} catch (DuplicateTimesheetCreationException e) {
					request.setAttribute("message", e.getMessage());
					return false;
				}
				if (flag == false) {
					logger.error("Timesheet creation failed for employee {} on date {}", employeeId, timeSheetRequest.getWork_date());
					request.setAttribute("message", "Timesheet creation failed!");
					return false;
				}

				int timeSheetId = repo.getTimeSheetId(timesheet);

				if (timeSheetId == -1) {
					logger.error("Failed to retrieve timesheet ID for employee {} on date {}", employeeId, timeSheetRequest.getWork_date());
					request.setAttribute("message", "Timesheet creation failed!");
					return false;
				}

				TimeSheetEntryDAO entryRepo = new TimeSheetEntryDAO();

				flag = entryRepo.createTimeSheetEntries(timeSheetId, timeSheetRequest.getEntries());

				if (flag == false) {
					logger.error("Timesheet entries insertion failed for timesheet ID {}", timeSheetId);
					request.setAttribute("message", "Timesheet creation failed!");
					return false;
				}

				logger.info("Timesheet {} created successfully by employee {} for date {}", timeSheetId, employeeId, timeSheetRequest.getWork_date());
				request.setAttribute("message", "Timesheet created successfully!");
				return true;
				

			} catch (JsonSyntaxException e) {
				logger.error("Timesheet creation failed ", e);
				request.setAttribute("message", "Exception Occured!");
				return false;
			} catch (JsonIOException e) {
				logger.error("Timesheet creation failed ", e);
				request.setAttribute("message", "Exception Occured!");
				return false;
			} catch (IOException e) {
				logger.error("Timesheet creation failed ", e);
				request.setAttribute("message", "Exception Occured!");
				return false;
			}
		}
		else {
			request.setAttribute("message", "Invalid Request!");
			return false;
		}

	}

}
