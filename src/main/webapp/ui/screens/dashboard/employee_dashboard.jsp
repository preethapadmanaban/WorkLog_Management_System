<%@page import="java.util.Map"%>
<%@page import="com.worklog.constants.*"%>
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
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>

	<div class="container">
		<h1>Hello, <%=request.getSession().getAttribute("name")%></h1>
		<div>
		 <div class="task-section" id="task_section">
			<% if(request.getAttribute("tasksMap") != null)
			{

				Map<String, List<Task>> tasks = (Map<String, List<Task>>)request.getAttribute("tasksMap");
				
				String employeeUpdateTaskUrl = "/worklog/controller?action=employeeUpdateTask&task_id=";
				
		/* 		List<Task> pendingTasks =  tasks.stream().filter(t->t.getStatus().equals(TaskStatus.ASSIGNED)).toList();
				
				List<Task> progressTasks = tasks.stream().filter(t->t.getStatus().equals(TaskStatus.IN_PROGRESS)).toList();
				
				List<Task> completedTasks = tasks.stream().filter(t->t.getStatus().equals(TaskStatus.COMPLETED)).toList(); */
			
			%>
			<div class="pending_task_array task_card_array">
				<h3>Assigned Tasks</h3>
				<% if(tasks.get(TaskStatus.ASSIGNED.name()) != null && tasks.get(TaskStatus.ASSIGNED.name()).size() > 0)
				{
				for(Task task:tasks.get(TaskStatus.ASSIGNED.name()))
				 {
				 %>
				    <div class="task-card priority-<%=task.getPriority().toString().toLowerCase()%>">
					    <div class="task-header">
					        <h3 class="task-title"><%=task.getTitle() %></h3>
					        <div class="status-badge-div">
						    	<span class="priority-badge <%=task.getPriority().toString().toLowerCase()%>"><%=task.getPriority().toString()%> PRIORITY</span>
						    	<span class="task-status status-<%= task.getStatus().toString().toLowerCase() %>"><%=task.getStatus().getDisplayValue() %></span>
					    	</div>
					    </div>
					    
					    <div class="task-footer">
					        <div class="deadline">
					            <span class="icon">📅</span>
					            <span class="date-text">Deadline: <strong><%=task.getDeadline() %></strong></span>
					        </div>
					        
					        <div class="task-actions">
					            <a href="<%= employeeUpdateTaskUrl + task.getId()%>" class="edit-btn">
					                View & Update
					            </a>
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
			
			<div class="progress_task_array task_card_array">
				<h3>In Progress Tasks</h3>
				<% if(tasks.get(TaskStatus.IN_PROGRESS.name()) != null && tasks.get(TaskStatus.IN_PROGRESS.name()).size() > 0)
				{
					for(Task task:tasks.get(TaskStatus.IN_PROGRESS.name()))
				 {
				 %>
					<div class="task-card priority-<%=task.getPriority().toString().toLowerCase()%>">
					    <div class="task-header">
					        <h3 class="task-title"><%=task.getTitle() %></h3>
					        <div class="status-badge-div">
						    	<span class="priority-badge <%=task.getPriority().toString().toLowerCase()%>"><%=task.getPriority().toString() %> PRIORITY</span>
						    	<span class="task-status status-<%= task.getStatus().toString().toLowerCase().replace("_", "-")%>"><%=task.getStatus().getDisplayValue() %></span>
					    	</div>
					    </div>
					    
					    <div class="task-footer">
					        <div class="deadline">
					            <span class="icon">📅</span>
					            <span class="date-text">Deadline: <strong><%=task.getDeadline() %></strong></span>
					        </div>
					        
					        <div class="task-actions">
					            <a href="<%= employeeUpdateTaskUrl + task.getId()%>" class="edit-btn">
					                View & Update
					            </a>
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
			
			<div class="completed_task_array task_card_array">
				<h3>Completed Tasks</h3>
				<% if(tasks.get(TaskStatus.COMPLETED.name()) != null && tasks.get(TaskStatus.COMPLETED.name()).size() > 0)
				{
					for(Task task:tasks.get(TaskStatus.COMPLETED.name()))
				 {
				 %>
					<div class="task-card priority-<%=task.getPriority().toString().toLowerCase()%>">
					    <div class="task-header">
					     	<h3 class="task-title"><%=task.getTitle() %></h3>
					    	<div class="status-badge-div">
						    	<span class="task-status status-<%= task.getStatus().toString().toLowerCase()%>"><%=task.getStatus().getDisplayValue() %></span>
					    	</div>
					    </div>
					    
					    <div class="task-footer">
					        <div class="deadline">
					            <span class="icon">📅</span>
					            <span class="date-text">Deadline: <strong><%=task.getDeadline() %></strong></span>
					        </div>
					        
					        <div class="task-actions">
					            <a href="<%= employeeUpdateTaskUrl + task.getId()%>" class="edit-btn">
					                View
					            </a>
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