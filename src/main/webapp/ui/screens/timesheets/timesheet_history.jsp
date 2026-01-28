<%@page import="com.worklog.entities.TimeSheet"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Timesheet history</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%=request.getContextPath()%>/ui/css/styles.css">
</head>
<body>
	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
    <jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
    
    
    
 		<div class = "filter_section">
 			<h3>My Timesheets</h3>
 			<form action="controller">
	 			<div class="filter_section">
	 				<input type="hidden" name="action" value="timesheetHistory">
	 				<h3>Filter by Status - </h3>
	 				
	 				<select class="nice-form-input" name="status" id="status_select">
		 					<option selected>Select</option>
		 					<option value="all">All tasks</option>
							<option value="pending">Pending</option>
							<option value="approved">Approved</option>
							<option  value="rejected">Rejected</option>
				
					</select>
					<input type="submit" value="Filter" class="submit_button">
	 			</div>
 			</form>
 		</div>
 	<div class="container_70">
 		<table class="table table-hover text-white" id="task_table">
 		   <thead>
				<tr>
					<th>Timesheet Date</th>
					<th>Total hours</th>
					<th>Status</th>
					<th>Mannager Comment</th>
					<th>Action</th>
				</tr>
			</thead>
			<tbody>
				<% 
					List<TimeSheet> timesheets = (List<TimeSheet>)request.getAttribute("timesheets");
			    	if(timesheets != null && timesheets.size() != 0)
				{
				
					for(TimeSheet timesheet:timesheets){						
				%>
					<tr>
						<td><%=timesheet.getWork_date() %></td>
						<td><%=timesheet.getTotal_hours() %> hrs</td>
						<td><%=timesheet.getStatus() %></td>
				<%
					String managerComment = timesheet.getManager_comment() == null ? "No Comment" : timesheet.getManager_comment();
				%>
						<td><%=managerComment%></td>
						<td><a href="controller?action=timesheetReview&timesheetId=<%=timesheet.getId()%>">View details</a></td>
					</tr>
				<%
					}
				%>
					
				
		    <%		
		    	}
		    	else{
		    %>
		    
		    	<tr><td colspan="7" style="text-align: center;">No Timesheets found!</td></tr>
		    
		    <%
		    	}
		    %>
		    
		    </tbody>
		</table>
					
 	</div>
 	
 	<script>
 	
 		function selectStatus(){
 			let selectTag = document.getElementById("status_select");
 			let latestValue = selectTag.value;
 			
 			if(latestValue != null){
 	 			window.location.href = "controller?action=timesheetHistory&status=" + latestValue;
 			}
 			
 		}
 		
 	</script>
  
</body>
</html>