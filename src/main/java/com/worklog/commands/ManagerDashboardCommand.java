package com.worklog.commands;

import java.util.List;

import com.worklog.entities.Employee;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ManagerDashboardCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
		if(session == null) {
			return false;
		}
		
		String role = (String) session.getAttribute("role");
		
		if(role != null && role.equalsIgnoreCase("Manager")) {
			
			EmployeeDAO dao = new EmployeeDAO();
			List<Employee> emp_list =  EmployeeDAO.getAllMembers();
			session.setAttribute("Members", emp_list);
			
			return true;
		}
		
		return false;
	}

}
