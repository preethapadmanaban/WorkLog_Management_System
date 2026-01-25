<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit task</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>

	 <%
	 boolean isEmployee = false;
	 if(((String)session.getAttribute("role")).equalsIgnoreCase("employee")){
		 isEmployee = true;
	 }
	 
		Task t = (Task)request.getAttribute("task");
	
	    if(t == null){
	%>
	        <p>No task found.</p>
	<%
	    } else {
	    //System.out.println("from jsp: " + t.getTitle());
	%>
  <section class="nice-form">
      <h3>
        Update task
      </h3>
      <p>Update the task according to the status.</p>
      <% if(isEmployee == true){
	    	%>
	    	<p>**You can change only the status.</p>
	    	<%
	    } %> 
      
      <form action="/worklog/controller" method="post">

	    <input type="hidden" name="action" value="updateTask">
	    
		<input type="hidden" name="id" value="<%= t.getId() %>">
	    

      <div class="nice-form-group"> 
        <label>Title</label>
        <input class="nice-form-input" type="text" name="title" placeholder="Task title" value="<%=t.getTitle()%>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    		} 
	    	%>>
      </div>

      <div class="nice-form-group">
        <label>Description</label>
        <input class="nice-form-input" type="text" name="description" placeholder="Your description" value="<%= t.getDescription() %>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
      </div>


      <div class="nice-form-group">
        <label for="deadline">Status</label>
        <select name="status" class="nice-form-input">
	        <option value="Assigned" <%= t.getStatus().equals("Assigned") ? "selected" : "" %>>Assigned</option>
	        <option value="In Progress" <%= t.getStatus().equals("In Progress") ? "selected" : "" %>>In Progress</option>
	        <option value="Completed" <%= t.getStatus().equals("Completed") ? "selected" : "" %>>Completed</option>
	    </select>
      </div>
      
      <div class="nice-form-group">
        <label for="deadline">Assigned to</label>
        <input class="nice-form-input" type="text" name="assigned_to" value="<%= t.getAssigned_to() %>"<% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
      </div>
      
      <div class="nice-form-group">
        <label for="deadline">Deadline</label>
        <input class="nice-form-input" type="date" id="deadline" name="deadline" placeholder="Your password" value="<%= t.getDeadline() %>" <% if(isEmployee == true){
	    	%>
	    	readonly
	    	<%
	    } %> >
      </div>
      
      <button class="submit_button">Submit</button>
      </form>
    </section>
	<%
	    }
	%>
</body>
</html>