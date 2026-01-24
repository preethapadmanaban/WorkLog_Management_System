package com.worklog.commands.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.worklog.entities.Employee;
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

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		} 
		
		String role = (String) session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			List<Employee> emp_list =  EmployeeDAO.getAllMembers();
			request.setAttribute("Members", emp_list);
			
			int timesheet_count = PendingTimesheetCount.pendingTimeSheet();
			request.setAttribute("PendingTimesheetCount", timesheet_count);
			
			TaskDAO dao = new TaskDAO();
			Map<String, Integer> status = dao.getTaskCountByStatus().orElse(new HashMap<>());
			request.setAttribute("Assigned", status.getOrDefault("Assigned", 0));
			request.setAttribute("InProgress", status.getOrDefault("In Progress", 0));
			request.setAttribute("Completed", status.getOrDefault("Completed", 0));
			
			
			return true;
		}
		
		return false;
	}

}
