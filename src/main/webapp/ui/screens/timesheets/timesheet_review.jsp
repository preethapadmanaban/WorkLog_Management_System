<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>
<%@ page import="com.worklog.entities.TimeSheetEntry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Review</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>

	<%
	
    TimeSheet ts = (TimeSheet) request.getAttribute("timesheet");
    List<TimeSheetEntry> entries = (List<TimeSheetEntry>) request.getAttribute("entries");
    
	%>
	
	<h2>TimeSheet review</h2>
	
	<%
		if(ts == null){
	%>
	
		<p>Page not found</p>
	
	<%
    	} else {
	%>
	
	<h3>Timesheet Details</h3>
	
	<table border="1" cellpadding="6">
			<tr><th>Timesheet ID</th><td><%= ts.getId() %></td></tr>
		    <tr><th>Employee ID</th><td><%= ts.getEmployee_id() %></td></tr>
		    <tr><th>Work Date</th><td><%= ts.getWork_date() %></td></tr>
		    <tr><th>Total Hours</th><td><%= ts.getTotal_hours() %></td></tr>
		    <tr><th>Status</th><td><%= ts.getStatus() %></td></tr>
	</table>
	
	<br>

	<!-- Entries Table -->
	<h3>Entries</h3>
	
	<table border="1" cellpadding="6">
	    <tr>
	        <th>Entry ID</th>
	        <th>Task ID</th>
	        <th>Notes</th>
	        <th>Hours Spent</th>
	    </tr>
	    
	    <%
    if(entries != null && !entries.isEmpty()){
        for(TimeSheetEntry e : entries){
	%>
	    <tr>
	        <td><%= e.getId() %></td>
	        <td><%= e.getTask_id() %></td>
	        <td><%= e.getNotes() %></td>
	        <td><%= e.getHours_spent() %></td>
	    </tr>
	<%
	        }
	    } else {
	%>
	    <tr>
	        <td colspan="4">No entries found</td>
	    </tr>
	<%
	    }
	%>
	
	</table>
	
	<h3>Manager Action</h3>

	<form action="controller" method="post">
	
	    <input type="hidden" name="timesheetId" value="<%= ts.getId() %>">
	
	    Comment:
	    <br>
	    <textarea name="manager_comment" rows="4" cols="50"></textarea>
	    <br><br>
	
	    <button type="submit" name="action" value="approveTimesheet">Approve</button>
	    <button type="submit" name="action" value="rejectTimesheet">Reject</button>
	
	</form>
	
	<br>
	
	<br>
	<a href="<%=request.getContextPath() %>/controller?action=pending">Back to Pending List</a>
	
	<%
	    }
	%>

</body>
</html>