package com.worklog.commands.timesheets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.worklog.dto.TimeSheetEntryDTO;
import com.worklog.dto.TimeSheetRequestDTO;
import com.worklog.entities.TimeSheet;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TimeSheetDAO;
import com.worklog.repositories.TimeSheetEntryDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CreateTimeSheetCommand - This class is used for creating timesheets
 * @author Vasudevan
 * @since 20-01-2026
 */

public class CreateTimeSheetCommand implements Command {

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            throw new UnAuthorizedException("access_denied");
        }

        int employeeId = (int) session.getAttribute("id");

        String managerIdStr = request.getParameter("manager_id");
        String workDateString = request.getParameter("work_date");
        String[] taskIdArray = request.getParameterValues("task_id");
        String[] hoursSpendArray = request.getParameterValues("hours_spend");
        String[] notesArray = request.getParameterValues("notes");

        if (managerIdStr == null || managerIdStr.trim().isEmpty() ||
            workDateString == null || workDateString.trim().isEmpty() ||
            taskIdArray == null || hoursSpendArray == null || notesArray == null ||
            taskIdArray.length != hoursSpendArray.length ||
            taskIdArray.length != notesArray.length) {

            request.getSession().setAttribute("message", "Invalid information.");
            return false;
        }

        double totalHours = 0.0;
        List<TimeSheetEntryDTO> entries = new ArrayList<>();

        for (int i = 0; i < taskIdArray.length; i++) {

            String taskIdStr = taskIdArray[i];
            String hoursStr = hoursSpendArray[i];
            String notes = notesArray[i]; // can be empty

            if (taskIdStr == null || taskIdStr.trim().isEmpty() ||
                hoursStr == null || hoursStr.trim().isEmpty()) {

                request.getSession().setAttribute("message", "Please fill all required fields.");
                return false;
            }

            try {
                int taskId = Integer.parseInt(taskIdStr);
                double hours = Double.parseDouble(hoursStr);

                if (hours <= 0 || hours > 10) {
                    request.getSession().setAttribute("message", "Hours must be between 0 and 10.");
                    return false;
                }

                totalHours += hours;

                // ✅ IMPORTANT: store hours of THIS task, not totalHours
                entries.add(new TimeSheetEntryDTO(taskId, notes, hours));

            } catch (NumberFormatException e) {
                request.getSession().setAttribute("message", "Invalid task or hours value.");
                return false;
            }
        }

        // Create request DTO (optional, but fine)
        TimeSheetRequestDTO timeSheetRequest = new TimeSheetRequestDTO(
                Integer.parseInt(managerIdStr),
                LocalDate.parse(workDateString),
                totalHours,
                entries
        );

        // Create timesheet entity
        TimeSheet timesheet = new TimeSheet();
        timesheet.setManager_id(Integer.parseInt(managerIdStr));
        timesheet.setWork_date(LocalDate.parse(workDateString));
        timesheet.setStatus("PENDING"); // ✅ makes it visible for manager
        timesheet.setEmployee_id(employeeId);
        timesheet.setTotal_hours(totalHours);

        TimeSheetDAO repo = new TimeSheetDAO();

        boolean created = repo.createTimeSheet(timesheet);
        if (!created) {
            request.getSession().setAttribute("message", "Timesheet creation failed, please try again!");
            return false;
        }

        int timeSheetId = repo.getTimeSheetId(timesheet);
        if (timeSheetId == -1) {
            request.getSession().setAttribute("message", "Timesheet creation failed, please try again!");
            return false;
        }

        TimeSheetEntryDAO entryRepo = new TimeSheetEntryDAO();
        boolean entriesCreated = entryRepo.createTimeSheetEntries(timeSheetId, timeSheetRequest.getEntries());

        if (!entriesCreated) {
            request.getSession().setAttribute("message", "Timesheet entries creation failed, please try again!");
            return false;
        }

        request.getSession().setAttribute("message", "Timesheet submitted for approval.");
        return true;
    }
}
