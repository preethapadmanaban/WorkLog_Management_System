<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Task</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
	
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
	<div class="container_70">
	<table class="table table-striped table-hover">
<%-- 	    <tr><th>ID</th><td><%= t.getId() %></td></tr>
 --%>	   
 		 <tr><th>Title</th><td><%= t.getTitle() %></td></tr>
	    <tr><th>Description</th><td><%= t.getDescription() %></td></tr>
<%-- 	    <tr><th>Assigned To</th><td><%= t.getAssigned_to() %></td></tr>--%>
	    <tr>
		    <th>Status</th>
		    <td>
		        <%
		            String cssClass = "";
		            String status = t.getStatus().name();   
		            if ("COMPLETED".equals(status)) {
		                cssClass = "status-completed";
		            } else if ("ASSIGNED".equals(status)) {
		                cssClass = "status-assigned";
		            } else if ("INPROGRESS".equals(status)) {
		                cssClass = "status-progress";
		            }
		        %>
		        <span class="status-badge <%= cssClass %>">
		            <%= status %>
		        </span>
		    </td>
		</tr>

	    <tr><th>Deadline</th><td><%= t.getDeadline() %></td></tr>
	   <%--  <tr><th>Created By</th><td><%= t.getCreated_by() %></td></tr> --%>

	    <tr>
		<th>Created Date</th>
		  <td><%= t.getCreated_at() == null ? "" : t.getCreated_at().split(" ")[0] %></td>
		</tr>
		<tr>
		  <th>Last Updated Date</th>
		  <td><%= t.getUpdated_at() == null ? "" : t.getUpdated_at().split(" ")[0] %></td>
		</tr>

	    

	</table>
	
	<br>
	
	<a class="info_anchor" href="controller?action=listTasks">Back to Task List</a>
	
	<%
	    }
	%>
	</div>
</body>
</html>