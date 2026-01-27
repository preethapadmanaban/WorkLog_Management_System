<%@page import="java.util.List"%>
<%@page import="com.worklog.dto.ReportEmployeeDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Worklog Report</title>

	<link rel="stylesheet" href="/worklog/ui/styles/report.css" type="text/css">

</head>

<body>
	
	<div class="container">
		<h1>Work Log</h1>
		<form action="<%=request.getContextPath()%>/controller?action=report" method="get" class="header-form">
		
		  	
		    
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
					<th>emp_id</th>
					<th>emp_name</th>
					<th>task_id</th>
					<th> title</th>
					<th>task_duration</th>
				</tr>
				<%
				    List<ReportEmployeeDTO> reportEmployees = (List<ReportEmployeeDTO>) request.getAttribute("report");
				
				    if (reportEmployees != null) {
				        for (ReportEmployeeDTO r : reportEmployees) {
				%>
				<tr>
	                <td><%=r.getEmp_id()%></td>
	                <td><%=r.getEmp_name()%></td>
	                <td><%=r.getTask_id()%></td>
	                <td><%=r.getTitle()%></td>
	                <td><%=r.getTask_duration()%></td>
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
    