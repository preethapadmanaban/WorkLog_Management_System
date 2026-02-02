<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manager Dashboard</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>

<body>


    <jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
    <jsp:include page="/ui/screens/common/message.jsp"></jsp:include>


    <%
        List<Employee> members = (List<Employee>) request.getAttribute("Members");
        Integer pendingCount = (Integer) request.getAttribute("PendingTimesheetCount");

        Integer assigned = (Integer) request.getAttribute("Assigned");
        Integer inProgress = (Integer) request.getAttribute("InProgress");
        Integer completed = (Integer) request.getAttribute("Completed");
    %>

    <div class="container">

        <h3>Manager Dashboard Overview</h3>

        <!-- SUMMARY CARDS -->
        <div class="cards">

            <div class="card">
                <h4>Team Members</h4>
                <p><%= (members == null ? 0 : members.size()) %></p>
            </div>

            <div class="card">
                <h4>Pending Timesheets</h4>
                <p><%= (pendingCount == null ? 0 : pendingCount) %></p>
            </div>

            <div class="card">
                <h4>Assigned</h4>
                <p><%= (assigned == null ? 0 : assigned) %></p>
            </div>

            <div class="card">
                <h4>In Progress</h4>
                <p><%= (inProgress == null ? 0 : inProgress) %></p>
            </div>

            <div class="card">
                <h4>Completed</h4>
                <p><%= (completed == null ? 0 : completed) %></p>
            </div>

        </div>

        <div class="tableBox">
            <h3>Team Members List</h3>

            <table class="table-professional">
            
			  <thead>
			    <tr>
			      <th>ID</th>
			      <th>Name</th>
			      <th>Role</th>
			    </tr>
			  </thead>
			
			  <tbody>
			    <%
			      if(members != null && !members.isEmpty()){
			        for(Employee e : members){
			    %>
			      <tr>
			        <td><%= e.getId() %></td>
			        <td><%= e.getName() %></td>
			        <td><%= e.getRole() %></td>
			      </tr>
			    <%
			        }
			      } else {
			    %>
			      <tr>
			        <td colspan="3" style="text-align:center; padding:18px;">
			          No members found
			        </td>
			      </tr>
			    <%
			      }
			    %>
			  </tbody>
			</table>
        </div>

    </div>

</body>
</html>
