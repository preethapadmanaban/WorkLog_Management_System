<%@page import="com.worklog.dto.ListTaskDTO"%>
<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Tasks</title>
<link href="/worklog/ui/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
 	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
 	<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
 	<div class="container_70">
 		<div class = "filter_section">
 			<h3>My Tasks</h3>
 			<div class="filter_section">
 				<h3>Filter by Status - </h3>
 				<select class="nice-form-input" id="status_select" onchange="selectStatus()">
 				
	 					<option value="all">All tasks</option>
						<option value="Completed">Completed</option>
						<option value="In Progress">In Progress</option>
						<option value="Assigned">Assigned</option>
					</select>
 			</div>
 		</div>
 	
 		<table class="table table-hover text-white" id="task_table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Task</th>
					<th>Description</th>
					<th>Report To</th>
					<th>Status</th>
					<th>Deadline</th>
					<th>Assigned Date</th>
				</tr>
			</thead>
			<tbody>
				<% 
				List<ListTaskDTO> tasks = (List<ListTaskDTO>)request.getAttribute("tasks");
				if(tasks != null && tasks.size() != 0) 
				{
					 
					
					//for(ListTaskDTO task:tasks)
					for(int i=0; i<tasks.size(); i++)
					{
						ListTaskDTO task = tasks.get(i);
					%>
						<tr>
							<td><%=i+1%></td>
							<td><%=task.getTitle()%></td>
							<td><%=task.getDescription()%></td>
							<td><%=task.getManagerName()%></td>
							<td><%=task.getStatus()%></td>
							<td><%=task.getDeadline()%></td>
							<td><%=task.getCreated_at().toString().split("T")[0]%></td>
						</tr>
					<%	
					}	
				}
				else{
				%>
				<tr><td colspan="7" style="text-align: center;">No Tasks found!</td></tr>
				<%
				}
				%>
			</tbody>
		</table>
 	</div>
 	
 	<script>
 	
 		function selectStatus(){
 			let selectTag = document.getElementById("status_select"); 			
 			window.location.href = "controller?action=listEmployeeTasks&status=" + selectTag.value;
 		}
 		
 	</script>
	
	
</body>
</html>