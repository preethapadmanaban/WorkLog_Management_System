<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create task</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>


	<form action="/worklog/controller" method="post">

    <input type="hidden" name="action" value="createTask">

    Title:
    <input type="text" name="title">

    Description:
    <input type="text" name="description">

    Assigned to:
    <select name="assigned_to">
        <option value="">-- Select Employee --</option>
        
        <%
        	List<Employee> list = (List<Employee>) request.getAttribute("Members");
        
        	if(list != null){
        		for(Employee e : list){
        %>
        
        				<option value="<%= e.getId() %>"><%= e.getName() %></option>
        				
        <%
          		}
        	}
        %>
    </select>

    Deadline:
    <input type="date" name="deadline">

    <input type="hidden" name="status" value="Assigned">

    <input type="submit" value="Submit">
</form>


</body>
</html>