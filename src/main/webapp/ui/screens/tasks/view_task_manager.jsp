<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<%
		Task t = (Task) request.getAttribute("task"); 
	%>

	
	<h2>Task Details</h2>
	
	<%
	    if(t == null){
	%>
	        <p>No task found.</p>
	<%
	    } else {
	%>
	
	<table border="1" cellpadding="6">
	    <tr><th>ID</th><td><%= t.getId() %></td></tr>
	    <tr><th>Title</th><td><%= t.getTitle() %></td></tr>
	    <tr><th>Description</th><td><%= t.getDescription() %></td></tr>
	    <tr><th>Assigned To</th><td><%= t.getAssigned_to() %></td></tr>
	    <tr><th>Status</th><td><%= t.getStatus() %></td></tr>
	    <tr><th>Deadline</th><td><%= t.getDeadline() %></td></tr>
	    <tr><th>Created By</th><td><%= t.getCreated_by() %></td></tr>
	    <tr><th>Created At</th><td><%= t.getCreated_at() %></td></tr>
	    <tr><th>Updated At</th><td><%= t.getUpdated_at() %></td></tr>
	</table>
	
	<br>
	
	<a href="controller?action=listTasks">Back to Task List</a>
	
	<%
	    }
	%>

</body>
</html>