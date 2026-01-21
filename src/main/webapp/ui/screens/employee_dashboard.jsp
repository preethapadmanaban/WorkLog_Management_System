<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee dashboard</title>
</head>
<body>
	<div class="container">
		<div class="task_section">
			<%
			 for(Task task:(List<Task>)request.getAttribute("pending_tasks_array"))
			 {
			 %>
				<div class="task_card">
					<div class="task_card_row">
						<span class="task_card_label">Title: </span>
						<span class="task_card_value"><%=task.getTitle()%></span>	
					</div>
					<div class="task_card_row">
						<span class="task_card_label">Status: </span>
						<span class="task_card_value"><%=task.getStatus()%></span>	
					</div>
					<div class="task_card_row">
						<span class="task_card_label">Deadline: </span>
						<span class="task_card_value"><%=task.getDeadline()%></span>	
					</div>				
				</div>
			<%	 
			 }
			%>
		</div>
		<div class="timesheet_section">

		</div>
	</div>
</body>
</html>