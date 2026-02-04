package com.worklog.commands.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.config.AppConfig;
import com.worklog.dto.ListResultWithRowCount;
import com.worklog.entities.Employee;
import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ListTasksCommand implements Command {

	public static int rowsPerPage = AppConfig.getPropertyInt("app.pagination.rows-per-page");
	private static final Logger logger = LogManager.getLogger(ListTasksCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UnAuthorizedException("access_denied");
		}

		String role = (String) session.getAttribute("role");

		if (role != null && role.equalsIgnoreCase("manager")) {

			int managerId = (int) session.getAttribute("id");

			TaskDAO dao = new TaskDAO();

			int empId = -1;
			LocalDate fromDate = null;
			LocalDate toDate = null;

			String empIdStr = request.getParameter("employee_id");
			String status = request.getParameter("status");
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			int pageNumber = request.getParameter("pageNumber") != null ? Integer.parseInt(request.getParameter("pageNumber")) : 1;

			if (empIdStr != null && !empIdStr.trim().isEmpty() && !empIdStr.equalsIgnoreCase("all")) {
				empId = Integer.parseInt(empIdStr);
			}

			if (status == null || status.trim().isEmpty() || status.equalsIgnoreCase("all")) {
				status = null;
			}

			if (fromDateStr != null && !fromDateStr.trim().isEmpty() && toDateStr != null && !toDateStr.trim().isEmpty()) {
				try {
					fromDate = LocalDate.parse(fromDateStr);
					toDate = LocalDate.parse(toDateStr);
					if (fromDate.isAfter(toDate)) {
						request.setAttribute("message", "To Date Should Be Come After From Date!");
						return false;
					}
				} catch (DateTimeParseException e) {
					request.setAttribute("message", "Invalid From Date And To Date!");
					return false;
				}
			}

			logger.info("Manager {} is filtering tasks | empId={} status={} from={} to={}", managerId, empId, status, fromDate, toDate);


			ListResultWithRowCount<Task> taskWithRowCount = dao
							.getTasksCreatedByManager(managerId, empId, status, fromDateStr, toDateStr, pageNumber)
							.orElse(null);

			if (taskWithRowCount == null) {
				request.setAttribute("message", "No data Found!");
				return false;
			}

			// taskList = dao.getTasksCreatedByManager(managerId, empId, status, fromDateStr, toDateStr, pageNumber).orElse(new
			// ArrayList<>());

			List<Employee> members = EmployeeDAO.getAllMembers("",0).orElse(new ArrayList<>());
			Map<Integer, String> empNameMap = new HashMap<>();
			for (Employee e : members) {
				empNameMap.put(e.getId(), e.getName());
			}

			request.setAttribute("members", members);
			request.setAttribute("empNameMap", empNameMap);
			request.setAttribute("tasks", taskWithRowCount.getTasks());

			// store selected values
			request.setAttribute("selectedEmpId", empIdStr);
			request.setAttribute("selectedStatus", status);
			request.setAttribute("selectedFromDate", fromDateStr);
			request.setAttribute("selectedToDate", toDateStr);

			int totalPages = 1;
			int rowCount = taskWithRowCount.getRowsCount();
			if (rowCount > rowsPerPage) {
				totalPages = rowCount / rowsPerPage;
				if (rowCount % rowsPerPage > 0) {
					++totalPages;
				}
			}

			request.setAttribute("totalPages", totalPages);

			return true;

		}

		return false;
	}
}
