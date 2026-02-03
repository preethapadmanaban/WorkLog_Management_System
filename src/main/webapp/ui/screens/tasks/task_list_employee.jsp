<%@page import="com.worklog.repositories.TaskDAO"%>
<%@page import="com.worklog.commands.constants.TaskStatus"%>
<%@page import="com.worklog.dto.ListTaskDTO"%>
<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>My Tasks</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link href="/worklog/ui/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
 	<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
 	<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
 
 <div class="tasks-filter">
  <div>
    <h3>List of Tasks</h3>
  </div>
	  <form action="controller" method="post" class="tasks-filter-form">
	    <input type="hidden" name="action" value="listEmployeeTasks">
	
	    <div class="tasks-filter-row">
	      <div class="tasks-filter-item">
	        <label>Status</label>
	        <select class="nice-form-input" name="status" id="status_select">
					<option value="all">All tasks</option>
				<option value="<%=TaskStatus.ASSIGNED.toString()%>">Assigned</option>
				<option value="<%=TaskStatus.IN_PROGRESS.toString()%>">In Progress</option>
				<option value="<%=TaskStatus.COMPLETED.toString()%>">Completed</option>
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
			<th>Task</th>
			<th>Description</th>
			<th>Status</th>
			<th>Deadline</th>
			<th>Assigned Date</th>
          </tr>
        </thead>
        <tbody>
        <% 
        int totalPages = request.getAttribute("totalPages") != null ? (int)request.getAttribute("totalPages") : 1;
		int pageNumber = request.getParameter("pageNumber") != null ? Integer.parseInt(request.getParameter("pageNumber")) : 1;
		List<Task> tasks = (List<Task>)request.getAttribute("tasks");
		if(tasks != null && tasks.size() != 0) 
		{
		 	
		int currentSerialSequence = TaskDAO.rowsPerPage * (pageNumber - 1);
		//for(ListTaskDTO task:tasks)
		for(int i=0; i<tasks.size(); i++)
		{
			Task task = tasks.get(i);
		%>
			<tr>
				<td><%=++currentSerialSequence%></td>
				<td><%=task.getTitle()%></td>
				<td><%=task.getDescription()%></td>
				<td>
				 <span class="task-status status-<%= task.getStatus().getDisplayValue() %>">
	                <%= task.getStatus().getDisplayValue() %>
	              </span>
				</td>
				<td><%=task.getDeadline()%></td>
				<td><%=task.getCreated_at()%></td>
			</tr>
		<%	
		}	
       } else {
       %>
         <tr>
           <td colspan="6">No tasks found</td>
         </tr>
       <%
           }
       %>
       </tbody>
     </table>
     <% if(tasks != null || tasks.size() > 5) {  %>
     <div class="pagination-button">
     		<%
     			
     			%>	
   			<form action="controller" method="post">
     			<input type="hidden" name="action" value="listEmployeeTasks">
     			<input type="hidden" name="pageNumber" value="<%=pageNumber - 1%>">
     			<button class="btn btn-primary" type="submit" <%if(pageNumber <= 1) { %> disabled <% } %> >Prev</button>
    			</form>
    			
    			<span><%=pageNumber%></span>
    			
    			<form action="controller" method="post">
    				<input type="hidden" name="action" value="listEmployeeTasks">
     			<input type="hidden" name="pageNumber" value="<%=pageNumber + 1%>">
     			<button class="btn btn-primary" type="submit" <%if(pageNumber >= totalPages ) { %> disabled <% } %>>Next</button>
    			</form>
     </div>
     <% } %>
    </div>
  </div>
</div>
 	
 	<script>


 	
 		document.addEventListener('DOMContentLoaded', ()=>{
			<%
				String statusSelect = request.getParameter("status") == null ? "all" : request.getParameter("status");
			%>
			let status = "<%=statusSelect%>";
			document.getElementById("status_select").value = status;
 		});
 	
 		function selectStatus(){
 			let selectTag = document.getElementById("status_select"); 			
 			window.location.href = "controller?action=listEmployeeTasks&status=" + selectTag.value;
 		}

	
 	</script>
	
	
</body>
</html>