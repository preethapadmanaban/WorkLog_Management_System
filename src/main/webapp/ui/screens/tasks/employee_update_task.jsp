<%@page import="com.worklog.entities.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Update task</title>
</head>
<body>
	<div>
		<% Task task = (Task)request.getAttribute("task"); 
		   if(task == null)
		   {
		%>
			<h3>Sorry, looks like there is no available data!</h3>
		<%
		   }
		   else
		   {
		%>
			<table class="table">
				<tr> <th>Task Title </th>  <td><%=task.getTitle() %></td> </tr>
				<tr> <th>Description </th>  <td><%=task.getDescription() %></td> </tr>
				<tr> <th>Task Assigned Date </th>  <td><%=task.getCreated_at() %></td> </tr>
				<tr> <th>Last Updated Date </th>  <td><%=task.getUpdated_at() %></td> </tr>
				<tr> <th>Deadline</th>  <td><%=task.getDeadline()%></td> </tr>
				<tr> <th>Status</th>  <td><%=task.getStatus()%></td> </tr>
			</table>
			
			
			<div>
				<button>Change Status to <%=task.getStatus()%></button>
			</div>
		<%
		   }
		%>
		
		
	</div>
</body>
</html>