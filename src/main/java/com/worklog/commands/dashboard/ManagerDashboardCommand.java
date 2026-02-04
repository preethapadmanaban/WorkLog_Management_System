package com.worklog.commands.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	public static final int PAGE_SIZE=5;

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws UnAuthorizedException {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
		    logger.warn("Unauthorized manager dashboard access: no session");
			throw new UnAuthorizedException("access_denied");
		} 
		
		String role = (String) session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			int currentPage = 1; 
//			List<Employee> emp_list = EmployeeDAO.getAllMembers("managedashboard",).orElse(new ArrayList<Employee>());
//			request.setAttribute("Members", emp_list);
			
			int timesheet_count = PendingTimesheetCount.pendingTimeSheet((int) session.getAttribute("id"));
			request.setAttribute("PendingTimesheetCount", timesheet_count);
			
			TaskDAO dao = new TaskDAO();
			Map<String, Integer> status = dao.getTaskCountByStatus((int) session.getAttribute("id")).orElse(new HashMap<>());
			request.setAttribute("Assigned", status.getOrDefault("Assigned", 0));
			request.setAttribute("InProgress", status.getOrDefault("In Progress", 0));
			request.setAttribute("Completed", status.getOrDefault("Completed", 0));
			
			int page=1;
			if (request.getParameter("page") != null) {
			    page = Integer.parseInt(request.getParameter("page"));
			}
			int offset = (page - 1) * PAGE_SIZE;
			
			Optional<List<Employee>> optional=EmployeeDAO.getAllMembers("managerDashboard",offset);
			List<Employee> emp_list=null;
			if(optional.isPresent()) {
				emp_list=optional.get();
			}else {
				emp_list=new ArrayList<>();
			}
			
			int employee_count =EmployeeDAO.getEmployeeCount();
			int totalPages = (int) Math.ceil((double) employee_count / PAGE_SIZE);

			
			
			request.setAttribute("employees",emp_list );
		    request.setAttribute("currentPage", page);
		    request.setAttribute("totalPages", totalPages);
		    System.out.println("emp list: "+emp_list);
		    System.out.println(employee_count);
			
			return true;
		}
		
		return false;
	}

}
