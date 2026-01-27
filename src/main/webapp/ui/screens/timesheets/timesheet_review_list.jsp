<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pending Timesheets</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>

<h2>Pending Timesheets</h2>

<%
    List<TimeSheet> pendingList =
        (List<TimeSheet>) request.getAttribute("pending");
%>

<table border="1" cellpadding="6">
    <tr>
        <th>Employee ID</th>
        <th>Work Date</th>
        <th>Total Hours</th>
        <th>Status</th>
        <th>Created</th>
        <th>Action</th>
    </tr>

<%
    if (pendingList != null && !pendingList.isEmpty()) {
        for (TimeSheet t : pendingList) {
%>
    <tr>
        <td><%= t.getEmployee_id() %></td>
        <td><%= t.getWork_date() %></td>
        <td><%= t.getTotal_hours() %></td>
        <td><%= t.getStatus() %></td>
        <td><%= t.getCreated_at().toLocalDateTime().toString().split("T")[0] %></td>
        <td>
            <a href="<%=request.getContextPath()%>/controller?action=timesheetReview&timesheetId=<%= t.getId() %>">
                Review
            </a>
        </td>
    </tr>
<%
        }
    } else {
%>
    <tr>
        <td colspan="7">No pending timesheets found</td>
    </tr>
<%
    }
%>

</table>

</body>
</html>
