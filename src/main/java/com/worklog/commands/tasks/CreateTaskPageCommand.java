package com.worklog.commands.tasks;

import java.util.List;

import com.worklog.entities.Employee;
import com.worklog.interfaces.Command;
import com.worklog.repositories.EmployeeDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreateTaskPageCommand implements Command{

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession(false);
		
        if (session == null) {
        	return false;
        }

        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equalsIgnoreCase("Manager")) {
        	 return false;
        }

        List<Employee> members = EmployeeDAO.getAllMembers();
        request.setAttribute("Members", members);
        
		return true;
	}

}
