<%@page import="com.worklog.entities.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Task" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Task List</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/bootstrap.min.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body class="tasks-page">
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>



	<%
    List<Task> list = (List<Task>) request.getAttribute("tasks");
	Map<Integer, String> empNameMap = (Map<Integer, String>) request.getAttribute("empNameMap");
	
	Integer pageObj = (Integer) request.getAttribute("page");
    Integer sizeObj = (Integer) request.getAttribute("size");
    Integer totalPagesObj = (Integer) request.getAttribute("totalPages");

    int currentPage = (pageObj == null) ? 1 : pageObj;
    int size = (sizeObj == null) ? 10 : sizeObj;
    int totalPages = (totalPagesObj == null) ? 1 : totalPagesObj;
    %>
	
	<div class="tasks-filter">
	  <div>
	    <h3>List of Tasks</h3>
	  </div>

  <form action="controller" method="post" class="tasks-filter-form"
      onsubmit="document.getElementById('filter').value='true'; document.getElementById('page').value='1';">
  	
    <input type="hidden" name="action" value="listTasks">
   	<input type="hidden" name="filter" id="filter" value="false"> 
    <input type="hidden" name="page" id="page" value="<%= currentPage %>">
	<input type="hidden" name="size" value="5">

    <div class="tasks-filter-row">
	      <div class="tasks-filter-item">
	        <label>Employee</label>
	        <select name="employee_id" id="employee_select" class="tasks-filter-input" >
	          <option value="all">Select Employee</option>
	          <% 
	            List<Employee> employees = (List<Employee>) request.getAttribute("members");
	            if(employees != null){
	              for(Employee emp : employees){
	          %>
	            <option value="<%=emp.getId()%>"><%=emp.getName() + " (" + emp.getId() + ")"%></option>
	          <% 
	              }
	            }
	          %>
	        </select>
	      </div>
	
	      <div class="tasks-filter-item">
	        <label>Status</label>
	        <select name="status" id="status_select" class="tasks-filter-input" >
	          <option value="all">-- All --</option>
	          <option value="Assigned">Assigned</option>
	          <option value="In Progress">In Progress</option>
	          <option value="Completed">Completed</option>
	        </select>
	      </div>
	
	      <div class="tasks-filter-item">
	        <label>From</label>
	        <input type="date" name="fromDate" id="fromDate" class="tasks-filter-input">
	      </div>
	
	      <div class="tasks-filter-item">
	        <label>To</label>
	        <input type="date" name="toDate" id="toDate" class="tasks-filter-input" >
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
			      <th>Title</th>
			      <th>Employee Name</th>
			      <th>Status</th>
			      <th>Deadline</th>
			      <th class="text-center">Actions</th>
			    </tr>
			  </thead>
			  <tbody>

    <%
        if(list != null && !list.isEmpty()){
            // for(Task t : list){
            for(int i = 0; i<list.size(); i++){
            	Task t = list.get(i);
    %>

    <tr>
<<<<<<< Updated upstream
<%--    <td><%= t.getId() %></td> --%>
		<td><%=i+1%></td>
        <td><%= t.getTitle() %></td>
        <td><%= t.getAssigned_to() %></td>
        <td><%= t.getStatus().toString() %></td>
        <td><%= t.getDeadline() %></td>

        <td class="action_button_array">
            <a class="info_anchor" href="controller?action=viewTask&id=<%= t.getId() %>">View</a>
            <a class="edit_anchor" href="controller?action=editTask&task_id=<%= t.getId() %>">Edit</a>
        </td>
    </tr>
=======
		  <td><%= ((currentPage - 1) * size) + (i + 1) %></td>
		
		  <td><%= t.getTitle() %></td>
		
		  <td>
		    <%= (empNameMap != null)
		          ? empNameMap.getOrDefault(t.getAssigned_to(), "Emp-" + t.getAssigned_to())
		          : ("Emp-" + t.getAssigned_to()) %>
		  </td>
		
		  <td>
		    <%
		      String st = t.getStatus().name().toLowerCase().replace("_", "-");
		    %>
		    <span class="task-status status-<%= st %>">
		      <%= t.getStatus().name().replace("_", " ") %>
		    </span>
		  </td>
		
		  <td><%= t.getDeadline() %></td>
		
		  <td class="actions-cell">
		    <a class="task-btn task-btn-view" href="controller?action=viewTask&id=<%= t.getId() %>">View</a>
		    <a class="task-btn task-btn-edit" href="controller?action=editTask&task_id=<%= t.getId() %>">Edit</a>
		  </td>
	</tr>
>>>>>>> Stashed changes

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
			<div class="tasks-pagination">
			  <button type="button" class="pg-btn" <%= (currentPage<=1) ? "disabled" : "" %>
			          onclick="goPage(<%= currentPage-1 %>)">Prev</button>
			
			  <span class="pg-info">Page <%= currentPage %> of <%= totalPages %></span>
			
			  <button type="button" class="pg-btn" <%= (currentPage>=totalPages) ? "disabled" : "" %>
			          onclick="goPage(<%= currentPage+1 %>)">Next</button>
			</div>  
	    </div>
	  </div>
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
		
		function goPage(p){
			  document.getElementById("filter").value = "true";
			  document.getElementById("page").value = p;
			  document.querySelector(".tasks-filter-form").submit();
			}

	</script>
</body>
</html>