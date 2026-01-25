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

<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>

<h2>Pending Timesheets</h2>

<%
    List<TimeSheet> list = (List<TimeSheet>) request.getAttribute("pendingTimesheets");
%>

<table border="1" cellpadding="6">
    <tr>
        <th>ID</th>
        <th>Employee</th>
        <th>Work Date</th>
        <th>Total Hours</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

<%
    if(list != null && !list.isEmpty()){
        for(TimeSheet ts : list){
%>
    <tr>
        <td><%= ts.getId() %></td>
        <td><%= ts.getEmployee_id() %></td>
        <td><%= ts.getWork_date() %></td>
        <td><%= ts.getTotal_hours() %></td>
        <td><%= ts.getStatus() %></td>
        <td>
            <a href="<%=request.getContextPath()%>/controller?action=timesheetReview&timesheetId=<%=ts.getId()%>">
                Review
            </a>
        </td>
    </tr>
<%
        }
    } else {
%>
    <tr>
        <td colspan="6">No pending timesheets found</td>
    </tr>
<%
    }
%>

</table>

</body>
</html>
