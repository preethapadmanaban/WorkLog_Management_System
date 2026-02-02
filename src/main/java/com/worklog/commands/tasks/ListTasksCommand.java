package com.worklog.commands.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.dto.PagedResult;
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

	private static final Logger logger = LogManager.getLogger(ListTasksCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UnAuthorizedException("access_denied");
		}

		String role = (String) session.getAttribute("role");

		if (role != null && role.equalsIgnoreCase("manager")) {

			boolean filter = Boolean.valueOf(request.getParameter("filter"));
			int managerId = (int) session.getAttribute("id");

			int page = 1;
			int size = 5;

			String pageStr = request.getParameter("page");
			String sizeStr = request.getParameter("size");

			if (pageStr != null && !pageStr.isBlank())
				page = Integer.parseInt(pageStr);
			if (sizeStr != null && !sizeStr.isBlank())
				size = Integer.parseInt(sizeStr);

			if (page < 1)
				page = 1;

			if (size < 1)
				size = 5;

			TaskDAO dao = new TaskDAO();

			Integer empId = null;
			String status = null;
			LocalDate fromDate = null;
			LocalDate toDate = null;

			if (filter) {

				String empIdStr = request.getParameter("employee_id");
				status = request.getParameter("status");
				String fromDateStr = request.getParameter("fromDate");
				String toDateStr = request.getParameter("toDate");

				if (empIdStr != null && !empIdStr.trim().isEmpty() && !empIdStr.equalsIgnoreCase("all")) {
					empId = Integer.parseInt(empIdStr);
				}

				if (status == null || status.trim().isEmpty() || status.equalsIgnoreCase("all")) {
					status = null;
				}

				if (fromDateStr != null && !fromDateStr.trim().isEmpty()) {
					fromDate = LocalDate.parse(fromDateStr);
				}

				if (toDateStr != null && !toDateStr.trim().isEmpty()) {
					toDate = LocalDate.parse(toDateStr);
				}

				logger.info("Manager {} is filtering tasks | empId={} status={} from={} to={}", managerId, empId, status, fromDate, toDate);

				PagedResult<Task> result = dao.getTasksCreatedByManager(managerId, empId, status, fromDate, toDate, page, size)
								.orElse(new PagedResult<>(new ArrayList<>(), 0));

				List<Employee> members = EmployeeDAO.getAllMembers().orElse(new ArrayList<>());

				Map<Integer, String> empNameMap = new HashMap<>();
				for (Employee e : members) {
					empNameMap.put(e.getId(), e.getName());
				}

				request.setAttribute("members", members);
				request.setAttribute("empNameMap", empNameMap);

				request.setAttribute("tasks", result.getData());

				request.setAttribute("totalCount", result.getTotalCount());
				request.setAttribute("page", page);
				request.setAttribute("size", size);
				request.setAttribute("totalPages", result.getTotalPages(size));

				return true;

			} else {

				logger.info("Manager {} is viewing all created tasks", managerId);

				PagedResult<Task> result = dao.getTasksCreatedByManager(managerId, null, null, null, null, page, size)
								.orElse(new PagedResult<>(new ArrayList<>(), 0));

				List<Employee> members = EmployeeDAO.getAllMembers().orElse(new ArrayList<>());

				Map<Integer, String> empNameMap = new HashMap<>();
				for (Employee e : members) {
					empNameMap.put(e.getId(), e.getName());
				}

				request.setAttribute("members", members);
				request.setAttribute("empNameMap", empNameMap);

				request.setAttribute("tasks", result.getData());

				request.setAttribute("totalCount", result.getTotalCount());
				request.setAttribute("page", page);
				request.setAttribute("size", size);
				request.setAttribute("totalPages", result.getTotalPages(size));

				return true;
			}
		}

		return false;
	}
}
