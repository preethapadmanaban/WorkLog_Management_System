<%@page import="com.worklog.commands.constants.TaskStatus"%>
<%@page import="com.worklog.repositories.TaskDAO"%>
<%@page import="com.worklog.entities.Employee"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.worklog.entities.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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
    List<Employee> employees = (List<Employee>) request.getAttribute("members");
 
    String selectedStatus = (String) request.getAttribute("selectedStatus");
    if (selectedStatus == null) selectedStatus = request.getParameter("status");
    if (selectedStatus == null) selectedStatus = "all";

    String selectedEmp = (String) request.getAttribute("selectedEmpId");
    if (selectedEmp == null) selectedEmp = request.getParameter("employee_id");
    if (selectedEmp == null) selectedEmp = "all";

    String selectedFrom = (String) request.getAttribute("selectedFromDate");
    if (selectedFrom == null) selectedFrom = request.getParameter("fromDate");
    if (selectedFrom == null) selectedFrom = "";

    String selectedTo = (String) request.getAttribute("selectedToDate");
    if (selectedTo == null) selectedTo = request.getParameter("toDate");
    if (selectedTo == null) selectedTo = "";

%>

<div class="tasks-filter">
  <div>
    <h3>List of Tasks</h3>
  </div>

  <form action="controller" method="post" class="tasks-filter-form"
        onsubmit="document.getElementById('filter').value='true';">
    <input type="hidden" name="action" value="listTasks">
    <input type="hidden" name="filter" id="filter" value="false">
    <input type="hidden" name="page" id="page" value="1">
    <input type="hidden" name="size" value="5">

    <div class="tasks-filter-row">
      <div class="tasks-filter-item">
        <label>Employee</label>
        <select name="employee_id" id="employee_select" class="tasks-filter-input">
          <option value="all" selected>Select Employee</option>
          <%
              if (employees != null) {
                  for (Employee emp : employees) {
          %>
              <option value="<%= emp.getId() %>">
                  <%= emp.getName() + " (" + emp.getId() + ")" %>
              </option>
          <%
                  }
              }
          %>
        </select>
      </div>

      <div class="tasks-filter-item">
        <label>Status</label>
        <select name="status" id="status_select" class="tasks-filter-input">
          <option value="all">-- All --</option>
          <option value="<%=TaskStatus.ASSIGNED.toString()%>">Assigned</option>
          <option value="<%=TaskStatus.IN_PROGRESS.toString()%>">In Progress</option>
          <option value="<%=TaskStatus.COMPLETED.toString()%>">Completed</option>
        </select>
      </div>

      <div class="tasks-filter-item">
        <label>From</label>
        <input type="date" name="fromDate" id="fromDate" class="tasks-filter-input">
      </div>

      <div class="tasks-filter-item">
        <label>To</label>
        <input type="date" name="toDate" id="toDate" class="tasks-filter-input">
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
	 		int totalPages = request.getAttribute("totalPages") != null ? (int)request.getAttribute("totalPages") : 1;
			int pageNumber = request.getParameter("pageNumber") != null ? Integer.parseInt(request.getParameter("pageNumber")) : 1;
            if (list != null && !list.isEmpty()) {
        		int currentSerialSequence = TaskDAO.rowsPerPage * (pageNumber - 1);
                for (int i = 0; i < list.size(); i++) {
                    Task t = list.get(i);

                    String empName =
                        (empNameMap != null)
                        ? empNameMap.getOrDefault(t.getAssigned_to(), "Emp-" + t.getAssigned_to())
                        : ("Emp-" + t.getAssigned_to());

                    String statusText = t.getStatus().getDisplayValue();
                    String statusCss  = statusText.toLowerCase().replace(" ", "-");
        %>

          <tr>
            <td><%=++currentSerialSequence %></td>
            <td><%= t.getTitle() %></td>

            <td><%= empName %></td>

            <td>
              <span class="task-status status-<%= statusCss %>">
			  	<%= statusText %>
			  </span>
            </td>

            <td><%= t.getDeadline() %></td>

            <td class="actions-cell">
              <a class="task-btn task-btn-view" href="controller?action=viewTask&id=<%= t.getId() %>">View</a>
              <a class="task-btn task-btn-edit" href="controller?action=editTask&task_id=<%= t.getId() %>">Edit</a>
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
        </tbody>
      </table>
    </div>
  </div>
 
   <div class="pagination-button">
 
 			<form action="controller">
   			<input type="hidden" name="action" value="listTasks">
   			<input type="hidden" name="pageNumber" value="<%=pageNumber - 1%>">
   			<button class="btn btn-primary" type="submit" <%if(pageNumber <= 1) { %> disabled <% } %> >Prev</button>
  			</form>
  			
  			<span><%=pageNumber%></span>
  			
  			<form action="controller">
  				<input type="hidden" name="action" value="listTasks">
   			<input type="hidden" name="pageNumber" value="<%=pageNumber + 1%>">
   			<button class="btn btn-primary" type="submit" <%if(pageNumber >= totalPages ) { %> disabled <% } %>>Next</button>
  			</form>
   </div>
 
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById("status_select").value = "<%= selectedStatus %>";
    document.getElementById("employee_select").value = "<%= selectedEmp %>";
    document.getElementById("fromDate").value = "<%= selectedFrom %>";
    document.getElementById("toDate").value = "<%= selectedTo %>";
});
</script>

</body>
</html>
