<%@page import="java.time.LocalDate"%>
<%@page import="com.worklog.dto.ReportEmployeeDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
		
		<div class="tasks-filter">
		  <div>
		    <h3>Employee Reports</h3>
		  </div>
		
		  <form action="controller" method="post" class="tasks-filter-form">
		    <input type="hidden" name="action" value="report">
		    <input type="hidden" name="filter" value="true">
		    <input type="hidden" name="type" value="employee">
		
		    <div class="tasks-filter-row">
		      <div class="tasks-filter-item">
		        <label>From</label>
		        <input type="date" name="fromDate" id="fromDate" class="tasks-filter-input">
		      </div>
		
		      <div class="tasks-filter-item">
		        <label>To</label>
		        <input type="date" name="toDate" id="toDate" class="tasks-filter-input">
		      </div>
		
		      <div class="tasks-filter-actions">
		        <button type="submit" class="tasks-filter-btn">Generate</button>
		      </div>
		    </div>
		  </form>
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
				<form action="controller/download" method="post">
					<input type="hidden" name="filter" value="true">
					<input type="hidden" name="action" value="report">
					<input type="hidden" name="download" value="download">
					<input type="hidden" name="type" value="employee"> 
					<input type="hidden" name="fromDate" value=<%=request.getParameter("fromDate") %>>
					<input type="hidden" name="toDate" value=<%=request.getParameter("toDate") %>>
					<button type="submit">download report</button>
				</form>
			<% }%>
		</div>
		</div>
</body>
</html>
    