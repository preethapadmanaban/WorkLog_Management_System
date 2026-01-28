package com.worklog.commands.tasks;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

        if (role != null) {

            boolean filter = Boolean.valueOf(request.getParameter("filter"));
            int managerId = (int) session.getAttribute("id");

            TaskDAO dao = new TaskDAO();

            if (filter) {

                String empIdStr = request.getParameter("employee_id");
                String status = request.getParameter("status");
                String fromDateStr = request.getParameter("fromDate");
                String toDateStr = request.getParameter("toDate");

                Integer empId = null;
                LocalDate fromDate = null;
                LocalDate toDate = null;
                
                logger.info("Manager {} is filtering tasks | empId={} status={} from={} to={}",
                		        managerId, empId, status, fromDate, toDate);

                if (empIdStr != null && !empIdStr.trim().isEmpty()) {
                    empId = Integer.parseInt(empIdStr);
                }

                if (status != null && status.trim().isEmpty()) {
                    status = null;
                }

                if (fromDateStr != null && !fromDateStr.trim().isEmpty()) {
                    fromDate = LocalDate.parse(fromDateStr);
                }

                if (toDateStr != null && !toDateStr.trim().isEmpty()) {
                    toDate = LocalDate.parse(toDateStr);
                }

                List<Task> taskList = dao
                        .getTasksCreatedByManager(managerId, empId, status, fromDate, toDate)
                        .orElse(new ArrayList<>());

                request.setAttribute("members", EmployeeDAO.getAllMembers().orElse(new ArrayList<Employee>()));
                request.setAttribute("tasks", taskList);

                return true;

            } else {
            	
            	logger.info("Manager {} is viewing all created tasks", managerId);

                List<Task> taskList = dao.getTasksCreatedByManager(managerId).orElse(new ArrayList<>());

                request.setAttribute("members", EmployeeDAO.getAllMembers().orElse(new ArrayList<Employee>()));
                request.setAttribute("tasks", taskList);

                return true;
            }
        }

        return false;
    }
}
