<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%if(request.getAttribute("message") != null){ %>
<div class="status_<%=request.getAttribute("status")%> alert_message">
	<h3><%=request.getAttribute("message") %></h3>	
</div>
<% } %>