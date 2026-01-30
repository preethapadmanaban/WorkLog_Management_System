<%@page import="java.time.LocalDate"%>
<%@page import="com.worklog.dto.ReportEmployeeDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Worklog Report</title>
	<link rel="stylesheet" href="/worklog/ui/css/styles.css" type="text/css">
	<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>
	<div class="filter_section">
	
		<h1>Employee Reports</h1>
		
		<form action="controller" method="post" class="header-form">
		
		<div class="filter_section">
				<input type="hidden" name="action" value="report">
			  	<input type="hidden" name="filter" value="true">
			  	
			    
			    <!-- <select name="type" class="nice-form-input">
				    <option value="">Select</option>
				    <option value="employee">Employee</option>
				    <option value="task">Task</option>
				</select> -->

				<input type="hidden" name="type" value="employee"> 
		   
		    	From date:
			    <input type="date" name="fromDate" class="nice-form-input">
			
			    To date:
			    <input type="date" name="toDate" class="nice-form-input" max="<%=LocalDate.now().toString()%>">
		
		   		<button type="submit" class="submit_button">Generate report</button>
		   		
			</div>
		</form>
</div>

<div class="container_70">
		<div class="records">
			<table class="table table-striped table-hover">
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
	                <% String notes = (r.getNotes() == null || r.getNotes().strip().isEmpty() == true) ? "N/A" : r.getNotes(); %>
	                <td><%=notes%></td>
            	</tr>
            	<%
        	}
   		 }else{
   		%>
   		
   		<tr><td style="text-align: center;" colspan="5">No records.</td></tr>
   		
   		<%
   		 }
				    
		%>
			</table>
			<%
			if (reportEmployees != null && reportEmployees.size() != 0) {
			%>
				<div style="width: 100%; text-align: center;"> 
				 	<a href="<%=(request.getContextPath() + "/controller?" + request.getQueryString())%>&download=true"><button class="submit_button">Download Report</button></a>
				</div>
			<% } %>
		</div>
		</div>
</body>
</html>
    