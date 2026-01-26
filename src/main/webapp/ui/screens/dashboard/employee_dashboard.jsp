<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee dashboard</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>

	<div class="container">
		<h1>Hello, <%=request.getSession().getAttribute("name")%></h1>
		<div class="task_section">
			<h2>My Tasks</h2>
			<% if(request.getAttribute("pending_tasks_array") != null)
			{
				List<Task> tasks = (List<Task>)request.getAttribute("pending_tasks_array");
				 for(Task task:tasks)
				 {
				 %>
					<div class="task_card">
					
						<div>
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
								<span class="task_card_value deadline"><%=task.getDeadline()%></span>	
							</div>	
						</div>
						
						<div>
							<a class="edit_button" href="/worklog/controller?action=editTask&task_id=<%=task.getId()%>" >
								<i class="fa-solid fa-pen-to-square fa-lg"></i>
							</a>
						</div>
									
					</div>
				<%	 
				 }
				%>
			<%	 
			 }
			%>
		</div>
		<div class="timesheet_section">

		</div>
	</div>
</body>
</html>