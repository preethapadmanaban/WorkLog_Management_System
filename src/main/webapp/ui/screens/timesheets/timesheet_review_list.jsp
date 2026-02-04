<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>
<%@ page import="java.util.Map" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pending Timesheets</title>
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>


<h2>Pending Timesheets</h2>

<%
    List<TimeSheet> pendingList = (List<TimeSheet>) request.getAttribute("pending");

	Map<Integer, String> empNameMap = (Map<Integer, String>) request.getAttribute("empNameMap");

%>
<div class="container_70">
<table class="table table-striped table-hover">
    <tr>
        <th>Employee Name</th>
        <th>Work Date</th>
        <th>Total Hours</th>
        <th>Status</th>
        <th>Submitted Date</th>
        <th>Action</th>
    </tr>

<%
    if (pendingList != null && !pendingList.isEmpty()) {
        for (TimeSheet t : pendingList) {
%>
    <tr>
        <td>
		    <%= empNameMap != null? empNameMap.getOrDefault(t.getEmployee_id(), "Emp-" + t.getEmployee_id()): "Emp-" + t.getEmployee_id()%>
		</td>
        <td><%= t.getWork_date() %></td>
        <td><%= t.getTotal_hours() %> hrs</td>
        <td><%= t.getStatus().toString() %></td>
        <td><%= t.getCreated_at() %></td>
        <td class="action_button_array">
            <a class="info_anchor" href="<%=request.getContextPath()%>/controller?action=timesheetReview&timesheetId=<%= t.getId() %>">
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
</div>
</body>
</html>
