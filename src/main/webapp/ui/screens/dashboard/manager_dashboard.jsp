<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Manager Dashboard</title>

<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/styles/styles.css">

</head>

<body>

    <%
        List<Employee> members = (List<Employee>) request.getAttribute("Members");
        Integer pendingCount = (Integer) request.getAttribute("PendingTimesheetCount");

        Integer assigned = (Integer) request.getAttribute("Assigned");
        Integer inProgress = (Integer) request.getAttribute("InProgress");
        Integer completed = (Integer) request.getAttribute("Completed");
    %>


    <jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
    <div class="navbar">

        <!-- LOGO ONLY -->
        <div class="nav_left">
            <img src="<%= request.getContextPath() %>/ui/images/worklog_logo.png" class="nav_logo" alt="worklog">
        </div>

        <div class="navLinks">
            <a href="<%=request.getContextPath()%>/controller?action=createTaskPage">Assign Task</a>
            <a href="<%=request.getContextPath()%>/controller?action=listTasks">View Tasks</a>
            <a href="<%=request.getContextPath()%>/controller?action=pending">Review Timesheets</a>
            <a href="<%=request.getContextPath()%>/controller?action=report">Reports</a>
            <a href="<%=request.getContextPath()%>/controller?action=logout">Logout</a>
        </div>

    </div>

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

            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Role</th>
                </tr>

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
                    <td colspan="3">No members found</td>
                </tr>
                <%
                    }
                %>
            </table>
        </div>

    </div>

</body>
</html>
