<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="navbar">

        <!-- LOGO ONLY -->
        <div class="nav_left">
            <img src="<%= request.getContextPath() %>/ui/images/worklog_logo.png" class="nav_logo" alt="worklog">
        </div>
        <% if(session.getAttribute("role") != null){ %>
        <div class="navLinks display-none">
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
		
		<div class="navbar_menu" onclick="showMenu()">
	            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
				  <path stroke-linecap="round" stroke-linejoin="round" d="M3.75 5.25h16.5m-16.5 4.5h16.5m-16.5 4.5h16.5m-16.5 4.5h16.5" />
				</svg>
        </div>
        
        
        <script>
	        function showMenu(){
	            let navbarLinks = document.querySelector(".navLinks");
	            if(navbarLinks != null){
	            	if(navbarLinks.style.display === "flex"){
	            		navbarLinks.style.display = "none";
	            	}
	            	else{
	            		navbarLinks.style.display = "flex";
	            	}
	            }
	        } 
        </script>
</div>