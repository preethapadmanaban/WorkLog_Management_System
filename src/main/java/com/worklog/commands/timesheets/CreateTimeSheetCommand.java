package com.worklog.commands.timesheets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.worklog.constants.TimeSheetStatus;
import com.worklog.dto.TimeSheetEntryDTO;
import com.worklog.dto.TimeSheetJsonRequestData;
import com.worklog.dto.TimeSheetRequestDTO;
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

/**
 * CreateTimeSheetCommand - This class is used for creating timesheets
 * @author Vasudevan
 * @since 20-01-2026
 */

public class CreateTimeSheetCommand implements Command {
	
	private static final Logger logger = LoggerFactory.getLogger(CreateTimeSheetCommand.class);

	private boolean createTimesheet(HttpServletRequest request, HttpServletResponse response, TimeSheet timesheet, int employeeId,
					String workDateString, TimeSheetJsonRequestData timeSheetRequest) {
		TimeSheetDAO repo = new TimeSheetDAO();
		boolean flag;
		try {
			flag = repo.createTimeSheet(timesheet);
		} catch (DuplicateTimesheetCreationException e) {
			request.setAttribute("status", "error");
			request.setAttribute("message", e.getMessage());
			return false;
		}
		if (flag == false) {
			logger.error("Timesheet creation failed for employee {} on date {}", employeeId, workDateString);
			request.setAttribute("status", "success");
			request.setAttribute("message", "Time sheet creation failed, please try again!");
			return false;
		}

		int timeSheetId = repo.getTimeSheetId(timesheet);

		if (timeSheetId == -1) {
			logger.error("Failed to retrieve timesheet ID for employee {} on date {}", employeeId, workDateString);
			request.setAttribute("status", "success");
			request.setAttribute("message", "Time sheet creation failed, please try again!");
			return false;
		}

		TimeSheetEntryDAO entryRepo = new TimeSheetEntryDAO();

		flag = entryRepo.createTimeSheetEntries(timeSheetId, timeSheetRequest.getEntries());

		if (flag == false) {
			logger.error("Timesheet entries insertion failed for timesheet ID {}", timeSheetId);
			request.setAttribute("status", "error");
			request.setAttribute("message", "Time sheet entry creation failed, please try again!");
			return false;
		}

		logger.info("Timesheet {} created successfully by employee {} for date {}", timeSheetId, employeeId, workDateString);
		request.setAttribute("status", "success");
		request.setAttribute("message", "Timesheet created and send for approval.");
		return true;
	}

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
				TimeSheetJsonRequestData requestObj = gson.fromJson(request.getReader(), TimeSheetJsonRequestData.class);

				// validate the input
				double total_hours_spent = 0.0;
				for (TimeSheetEntryDTO entry : requestObj.getEntries()) {
					total_hours_spent += entry.getHours_spent();
				}

				// create a new timesheet entity object from timesheet request dto
				TimeSheet timesheet = new TimeSheet();
				timesheet.setManager_id(requestObj.getManager_id());
				timesheet.setWork_date(requestObj.getWork_date());
				timesheet.setStatus(TimeSheetStatus.PENDING.toString());
				timesheet.setEmployee_id(employeeId);
				timesheet.setTotal_hours(total_hours_spent);

				System.out.println(requestObj);

				return createTimesheet(request, response, timesheet, employeeId, requestObj.getWork_date(), requestObj);

			} catch (JsonSyntaxException e) {
				out.write("{\"error\" : \" " + e.getMessage() + "\"}");
				return false;
			} catch (JsonIOException e) {
				out.write("{\"error\" : \" " + e.getMessage() + "\"}");
				return false;
			} catch (IOException e) {
				out.write("{\"error\" : \" " + e.getMessage() + "\"}");
				return false;
			}

		} else {
			// maangerId, work_date, list(task_id, hours_spend, notes)
			String managerIdStr = request.getParameter("manager_id");
			String workDateString = request.getParameter("work_date");
			String[] taskIdArray = request.getParameterValues("task_id");
			String[] hoursSpendArray = request.getParameterValues("hours_spend");
			String[] notesArray = request.getParameterValues("notes");

			if (managerIdStr == null || workDateString == null || taskIdArray == null || hoursSpendArray == null || notesArray == null
							|| taskIdArray.length != hoursSpendArray.length || taskIdArray.length != notesArray.length) {
				request.setAttribute("message", "Invalid information.");
				return false;
			}
			double total_hours_spend = 0;

			// create list of entries.
			List<TimeSheetEntryDTO> entries = new ArrayList<TimeSheetEntryDTO>();
			for (int array_pointer = 0; array_pointer < taskIdArray.length; array_pointer++) {
				if (taskIdArray[array_pointer] == null || hoursSpendArray[array_pointer] == null) {
					throw new InvalidParameterException("Invalid timesheet entry value.");
				} else {
					try {
						double hours_spend_for_one_task = Double.parseDouble(hoursSpendArray[array_pointer]);
						total_hours_spend += hours_spend_for_one_task;
						entries.add(new TimeSheetEntryDTO(Integer.parseInt(taskIdArray[array_pointer]), notesArray[array_pointer],
										hours_spend_for_one_task));
					} catch (NumberFormatException e) {
						request.setAttribute("message", "Invalid information.");
						return false;
					}
				}
			} // now we have have entries list.

			TimeSheetRequestDTO timeSheetRequest = new TimeSheetRequestDTO(Integer.parseInt(managerIdStr), LocalDate.parse(workDateString),
							total_hours_spend, entries);


			// create a new timesheet entity object from timesheet request dto
			TimeSheet timesheet = new TimeSheet();
			timesheet.setManager_id(Integer.parseInt(managerIdStr));
			timesheet.setWork_date(workDateString);
			timesheet.setStatus("pending");
			timesheet.setEmployee_id(employeeId);
			timesheet.setTotal_hours(total_hours_spend);

			// System.out.println("timesheet => " + timesheet);
			//
			// System.out.println("timeSheetRequest => " + timeSheetRequest);

			TimeSheetDAO repo = new TimeSheetDAO();
			boolean flag;
			try {
				flag = repo.createTimeSheet(timesheet);
			} catch (DuplicateTimesheetCreationException e) {
				request.setAttribute("status", "error");
				request.setAttribute("message", e.getMessage());
				return false;
			}
			if (flag == false) {
				logger.error("Timesheet creation failed for employee {} on date {}", employeeId, workDateString);
				request.setAttribute("status", "success");
				request.setAttribute("message", "Time sheet creation failed, please try again!");
				return false;
			}

			int timeSheetId = repo.getTimeSheetId(timesheet);

			if (timeSheetId == -1) {
				logger.error("Failed to retrieve timesheet ID for employee {} on date {}", employeeId, workDateString);
				request.setAttribute("status", "success");
				request.setAttribute("message", "Time sheet creation failed, please try again!");
				return false;
			}

			TimeSheetEntryDAO entryRepo = new TimeSheetEntryDAO();

			flag = entryRepo.createTimeSheetEntries(timeSheetId, timeSheetRequest.getEntries());

			if (flag == false) {
				logger.error("Timesheet entries insertion failed for timesheet ID {}", timeSheetId);
				request.setAttribute("status", "error");
				request.setAttribute("message", "Time sheet entry creation failed, please try again!");
				return false;
			}

			logger.info("Timesheet {} created successfully by employee {} for date {}", timeSheetId, employeeId, workDateString);
			request.setAttribute("status", "success");
			request.setAttribute("message", "Timesheet created and send for approval.");
			return true;
		}


        
    }
}
