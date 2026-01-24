package com.worklog.commands.tasks;

import java.util.List;
import java.util.Optional;

import com.worklog.entities.Task;
import com.worklog.interfaces.Command;
import com.worklog.repositories.TaskDAO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ListComletedTaskCommand implements Command{
	
	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session =request.getSession(false);
		int empid=Integer.parseInt(session.getAttribute("id")==null?"0":(String)session.getAttribute("id"));
		if(empid>0) {
			TaskDAO taskDAO=new TaskDAO();
			Optional<List<Task>> op=taskDAO.getAllCompletedTakEmployeeId(empid);
			if(op.isPresent()) {
				List<Task> tasks=op.get(); 
				request.setAttribute("taskcompleted", tasks);
				return true;
				
			}else {
				request.setAttribute("message", "you have no completed tasks");
				return true;
			}
		}else {
			request.setAttribute("message","access denied");
			return false;
		}
		
	}
	

}
