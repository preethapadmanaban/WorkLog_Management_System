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
			
			
			
			<div class="full_div_action_button">
			<% if(!task.getStatus().equals(TaskStatus.COMPLETED)){
			%>
				<button class="button button-primary" onclick="updateStatus()">Change Status to <%=nextStatus.getDisplayValue()%></button>
				<%
			}			
			%>
				<button class="button button-secondary" onclick="backToHome()">Back To Dashboard</button>		
			</div>
			<script>
			
				function backToHome(){
					window.location.href='controller?action=routing';
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
						
						if(data.success === true){
							openPopup(data.message, "Success ✅", "success", {onOk: ()=>{window.location.reload()}});
						}
						else{
							openPopup(data.message, "Failed ❌", "error");
						}
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