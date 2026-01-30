<%@page import="com.worklog.entities.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Task List</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>



	<%
    List<Task> list = (List<Task>) request.getAttribute("tasks");
    %>
	
	<div class="filter_section">
		<h3>List of Tasks</h3>
		<form action="controller" method="post">
	    	<div class="filter_section">
		    <input type="hidden" name="action" value="listTasks">
	
		    <input type="hidden" name="filter" value="true">
		
		    Employee:
		   	<select name="employee_id" class="nice-form-input" id="employee_select" required>
  			   		<option value="all">Select Employee</option>
 	
		   		<% List<Employee> employees = (List<Employee>) request.getAttribute("members");
		   			if(employees != null || employees.size() != 0)
		   			{
		   				for(Employee emp : employees)
		   				{
		   		%>
		   			<option value="<%=emp.getId()%>"><%=emp.getName() + " (" + emp.getId() + ") "%></option>
		   		<%
		   				}
		   			}
		   			else{
		   		%>
		   				<option selected>No Employee</option>
		   		<%		
		   			}
		   		%>
		   	</select>
		
		    Status:
		    <select name="status" class="nice-form-input" id="status_select" required>
		        <option value="all">-- All --</option>
		        <option value="Assigned">Assigned</option>
		        <option value="In Progress">In Progress</option>
		        <option value="Completed">Completed</option>
		    </select>
		
		    From:
		    <input type="date" name="fromDate" id="fromDate" class="nice-form-input" required>
		
		    To:
		    <input type="date" name="toDate" id="toDate" class="nice-form-input" required>
		
		    <input type="submit" value="Filter" class="submit_button">
		    </div>
		</form>
	</div>
	<div class="container_70">
	<table class="table table-striped table-hover">
    <tr>
<!--         <th>ID</th> -->
		<th>S.No</th>
        <th>Title</th>
        <th>Assigned To</th>
        <th>Status</th>
        <th>Deadline</th>
        <th>Actions</th>
    </tr>

    <%
        if(list != null && !list.isEmpty()){
            // for(Task t : list){
            for(int i = 0; i<list.size(); i++){
            	Task t = list.get(i);
    %>

    <tr>
<%--    <td><%= t.getId() %></td> --%>
		<td><%=i+1%></td>
        <td><%= t.getTitle() %></td>
        <td><%= t.getAssigned_to() %></td>
        <td><%= t.getStatus() %></td>
        <td><%= t.getDeadline() %></td>

        <td class="action_button_array">
            <a class="info_anchor" href="controller?action=viewTask&id=<%= t.getId() %>">View</a>
            <a class="edit_anchor" href="controller?action=editTask&task_id=<%= t.getId() %>">Edit</a>
        </td>
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
	</table>
	</div>
	
	<script type="text/javascript">
		document.addEventListener('DOMContentLoaded', ()=>{
			<%
				String statusSelect = request.getParameter("status") == null ? "all" : request.getParameter("status");
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				String employeeId = request.getParameter("employee_id")== null ? "all" : request.getParameter("employee_id");
			%>
			document.getElementById("status_select").value = "<%=statusSelect%>";
			document.getElementById("employee_select").value = "<%=employeeId%>";
			document.getElementById("fromDate").value = "<%=fromDate%>";
			document.getElementById("toDate").value = "<%=toDate%>";
		});
	</script>
</body>
</html>