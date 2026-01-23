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
 	<div class="container">
 	<h3>My Tasks</h3>
 		<table class="table table-hover text-white" id="task_table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Task</th>
					<th>Description</th>
					<th>Report To</th>
					<th>
					<select class="status_select" id="status_select" onchange="selectStatus()">
					
						<option selected>Status</option>
						<option>All</option>
						<option>Completed</option>
						<option>In Progress</option>
						<option>Assigned</option>
			
					</select></th>
					<th>Deadline</th>
					<th>Assigned Date</th>
				</tr>
			</thead>
			<tbody>
				<% 
				if(request.getAttribute("tasks") != null)
				{
					List<ListTaskDTO> tasks = (List<ListTaskDTO>)request.getAttribute("tasks"); 
					
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
							<td><%=task.getCreated_at()%></td>
						</tr>
					<%	
					}	
				}
				%>
			</tbody>
		</table>
 	</div>
 	
 	<script>
 	
 		function selectStatus(){
 			let selectTag = document.getElementById("status_select");
 			let latestValue = selectTag.value;
 			
 			selectTag.value = "Status"; 
 			console.log("selected value: ", latestValue);
 			
 			window.location.href = "controller?action=listEmployeeTasks&status=" + latestValue;
 		}
 		
 	</script>
	
	
</body>
</html>