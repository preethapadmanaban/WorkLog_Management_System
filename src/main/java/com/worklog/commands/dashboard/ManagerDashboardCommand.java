package com.worklog.commands.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.worklog.entities.Employee;
import com.worklog.exceptions.UnAuthorizedException;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.repositories.PendingTimesheetCount;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ManagerDashboardCommand - this class is command class.This holds the logic for the Manager dashboard.
 * @author Preetha
 * @since 19-01-2026
 */

public class ManagerDashboardCommand implements Command{
	
	private static final Logger logger = LogManager.getLogger(ManagerDashboardCommand.class);

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
		    logger.warn("Unauthorized manager dashboard access: no session");
			throw new UnAuthorizedException("access_denied");
		} 
		
		String role = (String) session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			List<Employee> emp_list = EmployeeDAO.getAllMembers().orElse(new ArrayList<Employee>());
			request.setAttribute("Members", emp_list);
			
			int timesheet_count = PendingTimesheetCount.pendingTimeSheet((int) session.getAttribute("id"));
			request.setAttribute("PendingTimesheetCount", timesheet_count);
			
			TaskDAO dao = new TaskDAO();
			Map<String, Integer> status = dao.getTaskCountByStatus((int) session.getAttribute("id")).orElse(new HashMap<>());
			request.setAttribute("Assigned", status.getOrDefault("Assigned", 0));
			request.setAttribute("InProgress", status.getOrDefault("In Progress", 0));
			request.setAttribute("Completed", status.getOrDefault("Completed", 0));
			
			
			return true;
		}
		
		return false;
	}

}
