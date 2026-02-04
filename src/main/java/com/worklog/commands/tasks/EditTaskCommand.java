package com.worklog.commands.tasks;

import java.util.ArrayList;

import com.worklog.entities.Task;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * EditTaskCommand - This class is used to edit the timesheet
 * 
 * @author Preetha
 * @since 20-01-2026
 */
public class EditTaskCommand implements Command {

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {

		HttpSession session = request.getSession(false);

		if (session == null) {
			throw new UnAuthorizedException("access_denied");
		}

		String role = (String) session.getAttribute("role");

		if (role == null) {
			return false;
		}

		String idStr = request.getParameter("task_id");

		if (idStr == null || idStr.isEmpty()) {
			return false;
		}

		int id = Integer.parseInt(idStr);

		TaskDAO dao = new TaskDAO();
		Task task = dao.getTaskById(id).orElse(null);

		if (task == null) {
			return false;
		}

		// fetch employee name
		EmployeeDAO empDao = new EmployeeDAO();
		String employeeName = empDao.getEmployeeNameById(task.getAssigned_to()).orElse("Unknown");

		// manager edit permission based on status
		boolean managerCanEdit = true;
		if ("manager".equalsIgnoreCase(role)) {
			managerCanEdit = task.getStatus() != null && "ASSIGNED".equals(task.getStatus().name());
		}

		request.setAttribute("task", task);
		request.setAttribute("assignedToName", employeeName);
		request.setAttribute("managerCanEdit", managerCanEdit);
		request.setAttribute("employeeList", EmployeeDAO.getAllMembers("",0).orElse(new ArrayList<>()));

		return true;
	}
}
