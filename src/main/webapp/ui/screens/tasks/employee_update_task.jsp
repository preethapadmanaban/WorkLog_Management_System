<%@page import="com.worklog.constants.TaskStatus"%>
<%@page import="com.worklog.entities.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Update task</title>
<link rel="stylesheet" href="/worklog/ui/css/styles.css">
<script src="${pageContext.request.contextPath}/ui/js/Modal.js"></script>
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/modal.jsp"/>

	<div class="timesheet_container">
		<div class="filter_section">
			<div>
				<div class="back_button" onclick="goBack()"><img alt="Back" src="ui/images/left.png"></div>
				<h3>Update Task Screen</h3>
			</div>
		</div>
		<% Task task = (Task)request.getAttribute("task"); 
		   if(task == null)
		   {
		%>
			<h3>Sorry, looks like there is no available data!</h3>
		<%
		   }
		   else
		   {
		%>
			<table class="table">
				<tr> <th>Task Title </th>  <td><%=task.getTitle() %></td> </tr>
				<tr> <th>Description </th>  <td><%=task.getDescription() %></td> </tr>
				<tr> <th>Task Assigned Date </th>  <td><%=task.getCreated_at() %></td> </tr>
				<tr> <th>Last Updated Date </th>  <td><%=task.getUpdated_at() %></td> </tr>
				<tr> <th>Deadline</th>  <td><%=task.getDeadline()%></td> </tr>
				<tr> <th>Status</th>  <td><%=task.getStatus().getDisplayValue()%></td> </tr>
			</table>
			
			<% 
				TaskStatus nextStatus;
			  	if(task.getStatus().equals(TaskStatus.ASSIGNED)){
			  		nextStatus = TaskStatus.IN_PROGRESS;
			  	}
			  	else{
			  		nextStatus = TaskStatus.COMPLETED;
			  	}
			  	
			%>
			
			<% if(!task.getStatus().equals(TaskStatus.COMPLETED)){
			%>
			
			<div>
				<button class="submit_button" onclick="updateStatus()">Change Status to <%=nextStatus.getDisplayValue()%></button>		
			</div>
			
			<%
			}			
			%>
	
			
			
			<script>
			
				function goBack(){
					window.history.back();
				}
				
				function updateStatus(){
					let status = "<%=nextStatus.toString()%>";
					let task_id = <%=request.getParameter("task_id")%>;
					let updateTaskUri = "/worklog/controller/api?action=employeeUpdateTask&task_id="+  task_id +"&status=" + status;
					/* alert("URL change - " + );
					return; */
					fetch(updateTaskUri, { 
						method: "POST",
						headers : {"Content-Type" : "application/json"}
					})
					.then((res)=>res.json())
					.then((data)=>{		
						openPopup(data.message, "Message", data.status);
					})
					.catch(err => openPopup("Unable to Connect to the Server!", "Alert", "error"));
				}
			</script>
		<%
		   }
		%>		
	</div>	
</body>
</html>