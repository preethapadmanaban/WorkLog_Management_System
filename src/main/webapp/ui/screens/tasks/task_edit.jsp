<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit task</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>

	 <%
	 boolean isEmployee = false;
	 if(((String)session.getAttribute("role")).equalsIgnoreCase("employee")){
		 isEmployee = true;
	 }
	 
		Task t = (Task)request.getAttribute("task");
	%>

	<h2>Edit Task</h2>

	<%
	    if(t == null){
	%>
	        <p>No task found.</p>
	<%
	    } else {
	    //System.out.println("from jsp: " + t.getTitle());
	%>
	
	<form action="/worklog/controller" method="post">
	
	    <input type="hidden" name="action" value="updateTask">
	    <input type="hidden" name="id" value="<%= t.getId() %>">
	
	    Title:
	    <input type="text" name="title" value="<%=t.getTitle()%>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    		} 
	    	%> >
	    <br><br>
	
	    Description:
	    <input type="text" name="description" value="<%= t.getDescription() %>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
	    <br><br>
	
	    Assigned To (Employee ID):
	    <input type="text" name="assigned_to" value="<%= t.getAssigned_to() %>"<% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
	    <br><br>
	
	    Deadline:
	    <input type="date" name="deadline" value="<%= t.getDeadline() %>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
	    <br><br>
	
	    Status:
	    <select name="status">
	        <option value="Assigned" <%= t.getStatus().equals("Assigned") ? "selected" : "" %>>Assigned</option>
	        <option value="In Progress" <%= t.getStatus().equals("In Progress") ? "selected" : "" %>>In Progress</option>
	        <option value="Completed" <%= t.getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
	    </select>
	    <br><br>
	
	    <input type="submit" value="Update Task">
	
	</form>
	
	<%
	    }
	%>

</body>
</html>