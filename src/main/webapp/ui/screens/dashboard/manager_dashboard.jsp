<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manager Dashboard</title>
<link rel="stylesheet" href="ui/styles/styles.css" type="text/css">
</head>
<body>
	
	<header>
		<nav>
			<div class="nav_label">
				<h2>Manager Dashboard</h2>
			</div>
			
			<div class="nav_links">
				<a href="controller?action=createTaskPage">Assign New Task</a>
			    <a href="controller?action=listTasks">View All Tasks</a>
			    <a href="controller?action=pending">Review Timesheets</a>
			    <a href="controller?action=report">Reports</a>
			    <a href="controller?action=logout">Logout</a>
			</div>
		</nav>
	</header>

	<%
    List<Employee> members = (List<Employee>) session.getAttribute("Members");
    Integer pendingCount = (Integer) session.getAttribute("PendingTimesheetCount");

    Integer assigned = (Integer) session.getAttribute("Assigned");
    Integer inProgress = (Integer) session.getAttribute("InProgress");
    Integer completed = (Integer) session.getAttribute("Completed");
	%>
	
	<h2>Manager Dashboard</h2>
	
	<!-- Summary -->
	<h3>Summary</h3>
	
	<p><b>Team Members:</b> <%= (members == null ? 0 : members.size()) %></p>
	<p><b>Pending Timesheets:</b> <%= (pendingCount == null ? 0 : pendingCount) %></p>
	
	<p><b>Tasks by Status:</b></p>
	<ul>
	    <li>Assigned: <%= (assigned == null ? 0 : assigned) %></li>
	    <li>In Progress: <%= (inProgress == null ? 0 : inProgress) %></li>
	    <li>Completed: <%= (completed == null ? 0 : completed) %></li>
	</ul>
	
	<hr>
	
	<!-- Actions -->
	<h3>Actions</h3>
	
	<a href="controller?action=createTaskPage">Assign New Task</a>
	<a href="controller?action=listTasks">View All Tasks</a>
	<a href="controller?action=pending">Review Timesheets</a>
	<a href="controller?action=report">Reports</a>
	<a href="controller?action=logout">Logout</a>
	
	<hr>
	
	<!-- Team Members Table -->
	<h3>Team Members</h3>
	
	<table border="1" cellpadding="6">
	    <tr>
	        <th>ID</th>
	        <th>Name</th>
	        <th>Role</th>
	    </tr>
	
	<%
	    if(members != null && !members.isEmpty()){
	        for(Employee e : members){
	%>
	    <tr>
	        <td><%= e.getId() %></td>
	        <td><%= e.getName() %></td>
	        <td><%= e.getRole() %></td>
	    </tr>
	<%
	        }
	    } else {
	%>
	    <tr>
	        <td colspan="3">No members found</td>
	    </tr>
	<%
	    }
	%>
	</table>
	
</body>
</html>