<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Access Denied</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
</head>
<body>
	<div class="access_denied_container">
		<img class="access_denied_img" src="<%= request.getContextPath() %>/ui/images/delete-image_13434901.png">
		<div class="access_denied_body">
			<h3>Access Denied</h3>
			<a class="info_anchor" href="/worklog/">Go to login</a>
		</div>
		
	</div>

</body>
</html>