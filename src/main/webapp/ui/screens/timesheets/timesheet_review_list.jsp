<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Review List</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>

	<%
		List<TimeSheet> ts = (List<TimeSheet>) request.getAttribute("pending");
	%>
	
	<h2>Pending Timesheet</h2>
	
	<table border="1" cellpadding="6">
		<tr>
		
			<th>ID</th>
			<th>Employee ID</th>
			<th>Work Date</th>
			<th>Total hours</th>
			<th>Status</th>
			<th>Created</th>
			<th>Action</th>
			
		</tr>
	
	<% 
		if(ts!=null && !ts.isEmpty()){
			for(TimeSheet t : ts){
		
	%>
	
	<tr>
		<td><%= t.getId() %></td>
		<td><%= t.getEmployee_id() %></td>
		<td><%= t.getWork_date() %></td>
        <td><%= t.getTotal_hours() %></td>
        <td><%= t.getStatus() %></td>
        <td><%= t.getCreated_at() %></td>
        <td>
            <a href="controller?action=timesheetReview&timesheetId=<%= t.getId() %>">Review</a>
        </td>
	</tr>
	
	<%
        }
    } else {
	%>
	
	<tr>
        <td colspan="7">No pending timesheets</td>
    </tr>
    
	<%
    	}
	%>
	
	</table>
	
	<br>
	<a href=""controller?action=managerDashboard"">Back to Dashboard</a>

</body>
</html>