<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Task</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>

</head>

<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>

<%
    // ---- Safe session & role handling ----
    String role = (session != null) ? (String) session.getAttribute("role") : null;
    boolean isEmployee = role != null && role.equalsIgnoreCase("employee");

    // ---- Get task from request ----
    Task t = (Task) request.getAttribute("task");
%>

<% if (t == null) { %>

    <p style="color:red;">No task found.</p>

<% } else { %>

<section class="nice-form">
    <h3>Update Task</h3>
    <p>Update the task according to the status.</p>

    <% if (isEmployee) { %>
        <p><strong>Note:</strong> You can change only the status.</p>
    <% } %>

    <form action="<%= request.getContextPath() %>/controller" method="post">

        <input type="hidden" name="action" value="updateTask">
        <input type="hidden" name="id" value="<%= t.getId() %>">

        <div class="nice-form-group">
            <label>Title</label>
            <input class="nice-form-input"
                   type="text"
                   name="title"
                   value="<%= t.getTitle() %>"
                   <%= isEmployee ? "readonly" : "" %>>
        </div>

        <div class="nice-form-group">
            <label>Description</label>
            <input class="nice-form-input"
                   type="text"
                   name="description"
                   value="<%= t.getDescription() %>"
                   <%= isEmployee ? "readonly" : "" %>>
        </div>

        <div class="nice-form-group">
            <label>Status</label>
            <select name="status" class="nice-form-input">
                <option value="Assigned" <%= "Assigned".equals(t.getStatus()) ? "selected" : "" %>>Assigned</option>
                <option value="In Progress" <%= "In Progress".equals(t.getStatus()) ? "selected" : "" %>>In Progress</option>
                <option value="Completed" <%= "Completed".equals(t.getStatus()) ? "selected" : "" %>>Completed</option>
            </select>
        </div>

        <div class="nice-form-group">
            <label>Assigned To</label>
            <input class="nice-form-input"
                   type="text"
                   name="assigned_to"
                   value="<%= t.getAssigned_to() %>"
                   <%= isEmployee ? "readonly" : "" %>>
        </div>

        <div class="nice-form-group">
            <label>Deadline</label>
            <input class="nice-form-input"
                   type="date"
                   name="deadline"
                   value="<%= t.getDeadline() %>"
                   <%= isEmployee ? "readonly" : "" %>>
        </div>

        <button class="submit_button">Submit</button>

    </form>
</section>

<% } %>

</body>
</html>
