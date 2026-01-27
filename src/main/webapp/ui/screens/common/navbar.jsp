<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="navbar">

        <!-- LOGO ONLY -->
        <div class="nav_left">
            <img src="<%= request.getContextPath() %>/ui/images/worklog_logo.png" class="nav_logo" alt="worklog">
        </div>
        <% if(session.getAttribute("role") != null){ %>
        <div class="navLinks">
        	<% if(((String)session.getAttribute("role")).equalsIgnoreCase("manager")){ %>
        	
        	<a href="controller?action=managerDashboard">Home</a>
        	
            <a href="controller?action=createTaskPage">Assign Task</a>
            <a href="controller?action=listTasks">View Tasks</a>
            <a href="controller?action=pending">Review Timesheets</a>
            <a href="controller?action=report">Reports</a>
            <% } %>
            
            <% if(((String)session.getAttribute("role")).equalsIgnoreCase("employee")){ %>
            <a href="controller?action=employeeDashboard">Home</a>
            <a href="controller?action=listEmployeeTasks">View all tasks</a>
            <a href="controller?action=createTimesheetPage">Create Timesheets</a>
            <a href="controller?action=timesheetHistory">Timesheet History</a>
            <% } %>
            
             
            
            <a href="controller?action=logout">Logout</a>
        </div>
		<%} %>
</div>