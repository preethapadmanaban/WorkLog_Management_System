package com.worklog.commands.tasks;

import java.sql.Date;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CreateTaskCommand - This class is the logic to insert into the timesheet
 * 
 * @author Preetha
 * @since 19-01-2026
 */

public class CreateTaskCommand implements Command {

	private static final Logger logger = LogManager.getLogger(CreateTaskCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("CreateTask failed: No active session.");
			throw new UnAuthorizedException("access_denied");
		}

		String role = (String) session.getAttribute("role");

		if (role != null && role.equalsIgnoreCase("Manager")) {

			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String assignedStr = request.getParameter("assigned_to");
			String deadlineStr = request.getParameter("deadline");

			if (title == null || title.trim().isEmpty()) {
				logger.warn("Task creation failed: Title is missing.");
				request.setAttribute("message", "Please enter the task title.");
				return false;
			}

			if (description == null || description.trim().isEmpty()) {
				logger.warn("Task creation failed: description is missing.");
				request.setAttribute("message", "Please enter the task description.");
				return false;
			}

			if (assignedStr == null || !assignedStr.matches("\\d+")) {
				logger.warn("Task creation failed: Assigned user ID missing.");
				request.setAttribute("message", "Please select the employee.");
				return false;
			}
			int assigned_to = Integer.parseInt(assignedStr);

			if (deadlineStr == null || deadlineStr.trim().isEmpty()) {
				logger.warn("Task creation failed: deadline is missing.");
				request.setAttribute("message", "Please select a deadline.");
				return false;
			}

			LocalDate localDeadline = LocalDate.parse(deadlineStr);
			LocalDate today = LocalDate.now();

			if (localDeadline.isBefore(today)) {
				logger.warn("Task creation failed: Deadline {} is in the past.", localDeadline);
				request.setAttribute("message", "Deadline cannot be in the past. Choose today or a future date.");
				return false;
			}

			Date deadline = Date.valueOf(localDeadline);

			String status = request.getParameter("status");

			if (status == null || status.trim().isEmpty()) {
				status = "Assigned";
			}

			TaskDAO dao = new TaskDAO();

			int createdBy = (int) session.getAttribute("id");

			boolean inserted = dao.createTask(title, description, assigned_to, status, deadline, createdBy);

			if (inserted) {
				logger.info("Task '{}' successfully created by Manager ID {}", title, createdBy);
				request.setAttribute("status", "success");
				request.setAttribute("message", "Task assigned successfully!");
				return true;
			} else {
				return false;

			}
		}

		return false;
	}

}
