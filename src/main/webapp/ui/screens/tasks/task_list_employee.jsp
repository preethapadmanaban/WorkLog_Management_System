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
 	<%-- <jsp:include page="/ui/screens/common/modal.jsp"></jsp:include> --%>
 		<div class = "filter_section">
 			<h3>My Tasks</h3>
 			
 				<form action="controller" method="post">
	 					<div class="filter_section">
				 		<h3>Filter by Status - </h3>
		 				<input type="hidden" name="action" value="listEmployeeTasks">
		 				<select class="nice-form-input" name="status" id="status_select">
			 					<option value="all">All tasks</option>
								<option value="<%=TaskStatus.ASSIGNED.toString()%>">Assigned</option>
								<option value="<%=TaskStatus.IN_PROGRESS.toString()%>">In Progress</option>
								<option value="<%=TaskStatus.COMPLETED.toString()%>">Completed</option>
							</select>
							
						<input type="submit" value="Filter" class="submit_button">
					</div>
				</form>
 			
 		</div>
 	<div class="container_70">
 		<table class="table table-striped table-hover" id="task_table">
			<thead>
				<tr>
					<th>S.No</th>
					<th>Task</th>
					<th>Description</th>
					<th>Report To</th>
					<th>Status</th>
					<th>Deadline</th>
					<th>Assigned Date</th>
				</tr>
			</thead>
			<tbody>
				<% 
				List<ListTaskDTO> tasks = (List<ListTaskDTO>)request.getAttribute("tasks");
				if(tasks != null && tasks.size() != 0) 
				{
					 
					
					//for(ListTaskDTO task:tasks)
					for(int i=0; i<tasks.size(); i++)
					{
						ListTaskDTO task = tasks.get(i);
					%>
						<tr>
							<td><%=i+1%></td>
							<td><%=task.getTitle()%></td>
							<td><%=task.getDescription()%></td>
							<td><%=task.getManagerName()%></td>
							<td><%=task.getStatus()%></td>
							<td><%=task.getDeadline()%></td>
							<td><%=task.getCreated_at().toString().split("T")[0]%></td>
						</tr>
					<%	
					}	
				}
				else{
				%>
				<tr><td colspan="7" style="text-align: center;">No Tasks found!</td></tr>
				<%
				}
				%>
			</tbody>
		</table>
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

		/* function getTasksByStatus(){
			let selectedStatus = document.getElementById("status_select").value;
			if(selectedStatus == null || status_select == ""){
				alert("Select Status");
				return;
			}

			//  fetch the task from server
			fetch("controller?action=listTasks", {
				method: "POST",
				headers: { 
					"Content-Type" : "application/x-www-form-urlencoded",
					"Accept" : "application/json"
				},
				body: "status="+encodeURIComponent(selectStatus) // encoded data
			})
			.then( (response) => response.json())
			.then( (data) => console.log("data => ", data))
			.catch( (error)=> {
				// handling api call failures here
				const modalObj = new Modal();
				modalObj.error(error.message);
			} );
		} */
 		
 		
 		
 	</script>
	
	
</body>
</html>