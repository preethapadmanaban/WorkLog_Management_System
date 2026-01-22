<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
*{
	margin:0;
	padding:0;
	box-sizing:border_box;
}
h2 {
    text-align: center;
    margin-bottom: 20px;
}
	.container {
    width: 80%;
    margin: 50px auto;
    background: #fff;
    display:flex;
    flex-direction:column;
    align-items:center;
    border-radius: 6px;
}
th, td {
    padding: 12px;
    text-align: center;
    border-bottom: 1px solid #ddd;
}
a{
text-decoration: none;
color:black;
&:hover{
	color:grey;
}
}
tr:hover {
    background-color: #f1f1f1;
}
</style>
</head>
<body>
	<%List<Employee> employees = (List<Employee>) request.getAttribute("employees"); %>
	<div class="container">
		<h2>Approved TimeSheets</h2>
		<table>
			<tr>
				<th>manager_name</th>
				<th>work_date</th>
				<th>total_hours</th>
				<th>status</th>
				<th>mannager_comment</th>
				<th>details</th>
			</tr>
			<tr>
				<td>renga</td>
				<td>10-12-09</td>
				<td>8.00</td>
				<td>true</td>
				<td>is good</td>
				<td><a href="/worklog/controller/">view</a></td>
			</tr>
		</table>
	</div>

</body>
</html>