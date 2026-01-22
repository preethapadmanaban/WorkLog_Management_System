<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>

<h1>Homepage</h1>
<h2>Hello, <%=request.getSession().getAttribute("name") %></h2> 

</body>
</html>