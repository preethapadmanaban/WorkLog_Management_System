<%@page import="com.worklog.dto.ListTaskDTO"%>
<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Tasks</title>
</head>
<body>

	<table border="2">
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
			if(request.getAttribute("tasks") != null)
			{
				List<ListTaskDTO> tasks = (List<ListTaskDTO>)request.getAttribute("tasks"); 
				
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
						<td><%=task.getCreated_at()%></td>
					</tr>
				<%	
				}	
			}
			%>
		</tbody>
	</table>
	
</body>
</html>