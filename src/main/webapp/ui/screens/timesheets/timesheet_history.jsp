<%@page import="com.worklog.repositories.TaskDAO"%>
<%@page import="com.worklog.constants.TimeSheetStatus"%>
<%@page import="com.worklog.entities.TimeSheet"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Timesheet history</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%=request.getContextPath()%>/ui/css/styles.css">
</head>
<body>
	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
    <jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
    
    <div class="tasks-filter">
	  <div>
	    <h3>Timesheet History</h3>
	  </div>
	  <form action="controller" method="post" class="tasks-filter-form">
	    <input type="hidden" name="action" value="timesheetHistory">
	    <div class="tasks-filter-row">
	      <div class="tasks-filter-item">
	        <label>Status</label>
	        <select class="nice-form-input" name="status" id="status_select">
					<option value="all" selected>All status</option>
					<option value="<%=TimeSheetStatus.PENDING.toString()%>">Pending</option>
					<option value="<%=TimeSheetStatus.APPROVED.toString()%>">Approved</option>
					<option value="<%=TimeSheetStatus.REJECTED.toString()%>">Rejected</option>
			</select>
	      </div>
	
	      <div class="tasks-filter-actions">
	        <button type="submit" class="tasks-filter-btn">Filter</button>
	      </div>
	    </div>
	  </form>
	</div>
	<div class="container_70">
	<div class="table-card">
	    <div class="table-responsive">
	      <table class="table-professional">
	        <thead>
	          <tr>
	          		<th>S.No</th>
					<th>Timesheet Date</th>
					<th>Total hours</th>
					<th>Status</th>
					<th>Mannager Comment</th>
					<th>Action</th>
			 </tr>
	        </thead>
		        <tbody>
					<% 
						int totalPages = request.getAttribute("totalPages") != null ? (int)request.getAttribute("totalPages") : 1;
						int pageNumber = request.getParameter("pageNumber") != null ? Integer.parseInt(request.getParameter("pageNumber")) : 1;
						List<TimeSheet> timesheets = (List<TimeSheet>)request.getAttribute("timesheets");
				    	if(timesheets != null && timesheets.size() != 0)
					{
					
			    		int currentSerialSequence = TaskDAO.rowsPerPage * (pageNumber - 1);
						for(TimeSheet timesheet:timesheets){						
					%>
						<tr>
							<td><%=++currentSerialSequence %></td>
							<td><%=timesheet.getWork_date() %></td>
							<td><%=timesheet.getTotal_hours() %> hrs</td>
							<td><%=timesheet.getStatus().toString() %></td>
					<%
						String managerComment = timesheet.getManager_comment() == null ? "No Comment" : timesheet.getManager_comment();
					%>
							<td><%=managerComment%></td>
							<td class="actions-cell"><a class="task-btn task-btn-view" href="controller?action=timesheetReview&timesheetId=<%=timesheet.getId()%>">View details</a></td>
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
	     <% if(totalPages > 1) {  %>
	     <div class="pagination-button">
	   			<form action="controller">
	     			<input type="hidden" name="action" value="timesheetHistory">
	     			<input type="hidden" name="pageNumber" value="<%=pageNumber - 1%>">
	     			<button class="btn btn-primary" type="submit" <%if(pageNumber <= 1) { %> disabled <% } %> >Prev</button>
	    			</form>
	    			
	    			<span><%=pageNumber%></span>
	    			
	    			<form action="controller">
	    				<input type="hidden" name="action" value="timesheetHistory">
	     			<input type="hidden" name="pageNumber" value="<%=pageNumber + 1%>">
	     			<button class="btn btn-primary" type="submit" <%if(pageNumber >= totalPages) { %> disabled <% } %>>Next</button>
	    			</form>
	     </div>
	     <% } %>
	    </div>
 	</div>
</div>
<script>
 	
 		document.addEventListener('DOMContentLoaded', () => {
			<%
				String selectStatus = request.getParameter("status") == null ? "all": request.getParameter("status");
			%>
			let status = "<%=selectStatus%>";
			document.getElementById('status_select').value = status;
			/* console.log("status => ", status); */
 		});
 	
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