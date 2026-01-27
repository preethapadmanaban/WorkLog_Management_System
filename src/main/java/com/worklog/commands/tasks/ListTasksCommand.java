package com.worklog.commands.tasks;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.worklog.entities.Employee;
import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ListTasksCommand - This class is used to list all the tasks
 * @author Preetha
 * @since 20-01-2026
 */

public class ListTasksCommand implements Command{

	@Override 
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String)session.getAttribute("role");
		
		if (role != null) {
			
			int managerId = (int) session.getAttribute("id");
			
			String empIdStr = request.getParameter("empId");
			String status = request.getParameter("status");
			String fromDateStr = request.getParameter("fromDate");
			String toDateStr = request.getParameter("toDate");
			
			Integer empId = null;          
			LocalDate fromDate = null;    
			LocalDate toDate = null;
			
			if(empIdStr != null && !empIdStr.trim().isEmpty()) {
				empId = Integer.parseInt(empIdStr);
			}
			
			if(status != null && status.trim().isEmpty()) {
			    status = null;
			}
			
			if(fromDateStr != null) {
				fromDate = LocalDate.parse(fromDateStr);
			}
			
			if(toDateStr != null) {
				toDate = LocalDate.parse(toDateStr);
			}
			
			TaskDAO dao = new TaskDAO();
			
			List<Task> taskList = dao.getTasksCreatedByManager(managerId, empId, status, fromDate, toDate).orElse(new ArrayList<>());
			
			request.setAttribute("members", EmployeeDAO.getAllMembers().orElse(new ArrayList<Employee>()));
			request.setAttribute("tasks", taskList);
			
			return true;
			
		}
		
		return false;
	}

}
