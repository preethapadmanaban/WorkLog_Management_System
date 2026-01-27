<%@page import="com.worklog.dto.ReportEmployeeDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Worklog Report</title>
	<link rel="stylesheet" href="/worklog/ui/css/styles.css" type="text/css">
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>
	
	<div class="container">
		<h1>Work Log</h1>
		<form action="controller" method="get" class="header-form">
		
			<input type="hidden" name="action" value="report">
		  	<input type="hidden" name="filter" value="true">
		  	
		    
		    <select name="type">
			    <option value="">Select</option>
			    <option value="employee">Employee</option>
			    <option value="task">Task</option>
			</select>

		   
		    
		    <input type="date" name="fromDate">
		
		    
		    <input type="date" name="toDate">
		
		    
		    <button type="submit">generate report</button>
		
		</form>
		
		<div class="records">
			<table>
				<tr>
					<th>Employee Name</th>
					<th>Date Of Work</th>
					<th>Task</th>
					<th>Task Duration</th>
					<th>Notes</th>
				
				</tr>
				<%
				    List<ReportEmployeeDTO> reportEmployees = (List<ReportEmployeeDTO>) request.getAttribute("report");
				
				    if (reportEmployees != null) {
				    for (ReportEmployeeDTO r : reportEmployees) {
				%>
				<tr>
	                <td><%=r.getEmp_name()%></td>
	                <td><%=r.getWork_date() %></td>
	                <td><%=r.getTitle()%></td>
	                <td><%=r.getTask_duration()%></td>
	                <td><%=r.getNotes() %></td>
            	</tr>
            	<%
        	}
   		 }
		%>
			</table>
		</div>
	</div>
</body>
</html>
    