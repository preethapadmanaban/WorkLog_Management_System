<%@page import="com.worklog.entities.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>

	<%
    List<Task> list = (List<Task>) request.getAttribute("tasks");
    %>
	
	<div class="filter_section">
		<h3>List of Tasks</h3>
		<form action="controller" method="get">
	    	<div class="filter_section">
		    <input type="hidden" name="action" value="listTasks">
	
		    <input type="hidden" name="filter" value="true">
		
		    Employee ID:
		   	<select name="employee_id" class="nice-form-input">
		   		<% List<Employee> employees = (List<Employee>) request.getAttribute("members");
		   			if(employees != null || employees.size() != 0)
		   			{
		   				for(Employee emp : employees)
		   				{
		   		%>
		   			<option value="<%=emp.getId()%>"><%=emp.getName()%></option>
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
		    <select name="status" class="nice-form-input">
		        <option value="all">-- All --</option>
		        <option value="Assigned">Assigned</option>
		        <option value="In Progress">In Progress</option>
		        <option value="Completed">Completed</option>
		    </select>
		
		    From:
		    <input type="date" name="fromDate" class="nice-form-input">
		
		    To:
		    <input type="date" name="toDate" class="nice-form-input">
		
		    <input type="submit" value="Filter" class="submit_button">
		    </div>
		</form>
	</div>
	<table border="1" cellpadding="6">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Assigned To</th>
        <th>Status</th>
        <th>Deadline</th>
        <th>Actions</th>
    </tr>

    <%
        if(list != null && !list.isEmpty()){
            for(Task t : list){
    %>

    <tr>
        <td><%= t.getId() %></td>
        <td><%= t.getTitle() %></td>
        <td><%= t.getAssigned_to() %></td>
        <td><%= t.getStatus() %></td>
        <td><%= t.getDeadline() %></td>

        <td>
            <a href="controller?action=viewTask&id=<%= t.getId() %>">View</a>
            <a href="controller?action=editTask&task_id=<%= t.getId() %>">Edit</a>
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
	
	
</body>
</html>