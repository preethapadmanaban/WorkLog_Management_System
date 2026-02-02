<%@page import="com.worklog.commands.constants.TaskStatus"%>
<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Employee dashboard</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>

	<div class="container">
		<h1>Hello, <%=request.getSession().getAttribute("name")%></h1>
		<div>
		 <div id="task_section">
			<% if(request.getAttribute("pending_tasks_array") != null)
			{

				List<Task> tasks = (List<Task>)request.getAttribute("pending_tasks_array");
				
				String employeeUpdateTaskUrl = "/worklog/controller?action=employeeUpdateTask&task_id=";
				
				List<Task> pendingTasks =  tasks.stream().filter(t->t.getStatus().equals(TaskStatus.ASSIGNED)).toList();
				
				List<Task> progressTasks = tasks.stream().filter(t->t.getStatus().equals(TaskStatus.IN_PROGRESS)).toList();
				
				List<Task> completedTasks = tasks.stream().filter(t->t.getStatus().equals(TaskStatus.COMPLETED)).toList();
			
			%>
			<div class="pending_task_array task_card_array">
				<h3>Pending Tasks</h3>
				<% if(pendingTasks.size() > 0)
				{
				for(Task task:pendingTasks)
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
								<span class="task_card_value"><%=task.getStatus().getDisplayValue()%></span>	
							</div>
							<div class="task_card_row">
								<span class="task_card_label">Deadline: </span>
								<span class="task_card_value deadline"><%=task.getDeadline()%></span>	
							</div>	
						</div>
						
						<div>
							<a class="edit_button" href="<%= employeeUpdateTaskUrl + task.getId()%>" >
								<i class="fa-solid fa-pen-to-square fa-lg"></i>
							</a>
						</div>
									
					</div>
				<%	 
				 }
				}else{
				%>
					<p>No Tasks</p>
				<%
				}
				%>
			
			</div>
			
			<div class="progress_task_array task_card_array">
				<h3>In Progress Tasks</h3>
				<% if(progressTasks.size() > 0)
				{
					for(Task task:progressTasks)
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
								<span class="task_card_value"><%=task.getStatus().getDisplayValue()%></span>	
							</div>
							<div class="task_card_row">
								<span class="task_card_label">Deadline: </span>
								<span class="task_card_value deadline"><%=task.getDeadline()%></span>	
							</div>	
						</div>
						
						<div>
							<a class="edit_button" href="<%= employeeUpdateTaskUrl + task.getId()%>" >
								<i class="fa-solid fa-pen-to-square fa-lg"></i>
							</a>
						</div>
									
					</div>
				<%	 
				 }
				}else{
				%>
					<p>No Tasks</p>
				<%
				}
				%>
			</div>
			
			<div class="completed_task_array task_card_array">
				<h3>Completed Tasks</h3>
				<% if(completedTasks.size() > 0)
				{
					for(Task task:completedTasks)
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
								<span class="task_card_value"><%=task.getStatus().getDisplayValue()%></span>	
							</div>
							<div class="task_card_row">
								<span class="task_card_label">Deadline: </span>
								<span class="task_card_value deadline"><%=task.getDeadline()%></span>	
							</div>	
						</div>
					</div>
				<%	 
				 }
				}else{
				%>
					<p>No Tasks</p>
				<%
				}
				%>
			</div>
			
			<%	 
			 }
			else{
			%>
				<div>Hey there, Welcome to your dashboard!</div>
			<%
			}
			%>
			</div>
		</div>
	</div>
</body>
</html>