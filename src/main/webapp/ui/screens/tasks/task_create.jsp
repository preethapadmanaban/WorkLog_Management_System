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

<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
  <section class="nice-form">
      <h3>
        New Task
      </h3>
      <p>Create a new task and assign to a employee.</p>
      
      <form action="/worklog/controller" method="post">
   	  <input type="hidden" name="action" value="createTask">

      <div class="nice-form-group"> 
        <label>Title</label>
        <input class="nice-form-input" name="title" type="text" placeholder="Task title" required/>
      </div>

      <div class="nice-form-group">
        <label>Description</label>
        <input class="nice-form-input" name="description" type="text" placeholder="Your description" required/>
      </div>

      <div class="nice-form-group">
        <label>Select employee</label>
        <select class="nice-form-input" name="assigned_to" name="task_id" id="task_id" required>
          <option selected>-- Select employee --</option>
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
      </div>

      <div class="nice-form-group">
        <label for="deadline">Deadline</label>
        <input class="nice-form-input" type="date" id="deadline" name="deadline" placeholder="Your password" required/>
      </div>
      
      <button class="submit_button">Submit</button>
      </form>
    </section>

</body>
</html>
