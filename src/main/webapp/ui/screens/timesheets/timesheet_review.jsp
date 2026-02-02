<%@page import="com.worklog.dto.TimeSheetEntryForReviewDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>
<%@ page import="com.worklog.entities.TimeSheetEntry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Review</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>

	<%
	
    TimeSheet ts = (TimeSheet) request.getAttribute("timesheet");
    List<TimeSheetEntryForReviewDTO> entries = (List<TimeSheetEntryForReviewDTO>) request.getAttribute("entries");
    
	%>
	
	<h2>TimeSheet review</h2>
	
	<%
		if(ts == null){
	%>
	
		<p>Sorry! Time sheet not found</p>
	
	<%
    	} else {
	%>
	
	<div class="container_70">
	<h3>Timesheet Details</h3>
	<div class="timesheet_review_detail_with_image">
	<table class="table table-striped table-hover border border-secondary">
		    <tr><th>Employee Id</th><td><%= ts.getEmployee_id() %></td></tr>
		    <tr><th>Work Date</th><td><%= ts.getWork_date() %></td></tr>
		    <tr><th>Total Hours</th><td><%= ts.getTotal_hours() %> hours</td></tr>
		    <% String comment = ts.getManager_comment() == null ? "No Comment" : ts.getManager_comment();%>
		    <tr><th>Manager Comment</th><td><%=comment%></td></tr>
		    <tr><th>Status</th><td><%= ts.getStatus().toString()%></td></tr>
		    <tr><th>Submitted Date</th><td><%=ts.getCreated_at()%></td></tr>
	</table>
	
	
	<%
	if(((String)session.getAttribute("role")).equalsIgnoreCase("employee") == true)
	{
	if(ts.getStatus().equalsIgnoreCase("approved") == true)
	{ 
	%>
	<div class="timesheet_status_img_div" id="timesheet_approved_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_approved.png">
	</div>
	<%
	}
	else if(ts.getStatus().equalsIgnoreCase("rejected") == true)
	{
	%>
	<div class="timesheet_status_img_div" id="timesheet_rejected_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_rejected.png">
	</div>
	<%
	}
	else
	{
	%>
	<div class="timesheet_status_img_div" id="timesheet_pending_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_pending.png">
	</div>
	<%} 
	}
	%>
	
	</div>
	<br>
	
	
	
	<!-- Entries Table -->
	<h3>Entries</h3>
	
	<table class="table table-striped table-hover border border-secondary">
	    <tr>
	        <!-- <th>Entry ID</th> -->
	        <th>Task Title</th>
	        <th>Notes</th>
	        <th>Hours Spent</th>
	    </tr>
	    
	    <%
    if(entries != null && !entries.isEmpty()){
        for(TimeSheetEntryForReviewDTO e : entries){
	%>
	    <tr>
	        <%-- <td><%= e.getId() %></td> --%>
	        <td><%= e.getTitle() %></td>
	        <td><%= e.getNotes() %></td>
	        <td class="action_button_array"><%= e.getHours_spent() %> hours</td>
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
	
	
	
	<% 
		if(((String)session.getAttribute("role")).equalsIgnoreCase("manager"))
		{
	%>
	<h3>Manager Action</h3>

	<form action="controller" method="post">
	
	    <input type="hidden" name="timesheetId" value="<%= ts.getId() %>">
	
	    Comment:
	    <br>
	    <textarea name="manager_comment" rows="4" cols="50"></textarea>
	    <br><br>
	
	    <button class="submit_button" type="submit" name="action" value="approveTimesheet">Approve</button>
	    <button class="reject_button" type="submit" name="action" value="rejectTimesheet">Reject</button>
	
	</form>
	
	<br>
	
	<br>
	<a class="info_anchor" href="<%=request.getContextPath() %>/controller?action=pending">Back to Pending List</a>
	
	<%
		}
	  }
	%>
	
	<% if(((String)session.getAttribute("role")).equalsIgnoreCase("employee") == true)
	{
	%>
	<div style="margin-top: 20px;">
		<a class="info_anchor" href="/worklog/controller?action=timesheetHistory">Back to Timesheet History</a>
	</div>
	<% 
	}
	%>
	</div>
</body>
</html>