<%@page import="com.worklog.entities.TimeSheet"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Timesheet history.</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/ui/css/styles.css">
</head>
<body>
	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
    <jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
    
    <%
    	List<TimeSheet> timesheets = (List<TimeSheet>)request.getAttribute("timesheets");
    	if(timesheets != null && timesheets.size() != 0){
    %>
    
    	<table>
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
			</tbody>
		</table>
		
    <%		
    	}
    	else{
    %>
    
    	<div class="container_70">
    		<h3>Currently you have no timesheets.</h3>
    	</div>
    
    <%
    	}
    %>
</body>
</html>