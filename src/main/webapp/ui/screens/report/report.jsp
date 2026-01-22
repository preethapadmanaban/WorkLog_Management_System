<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>Worklog Report</title>

	<link rel="stylesheet" href="/worklog/ui/styles/report.css" type="text/css">

</head>

<body>
	
	<div class="container">
	<h1>Work Log</h1>
		<form action="report" method="get" class="header-form">

  
    
    <select name="employeeId">
        <option value="employee" name="employee">Employee</option>
        <option value="task" name="employee">task</option>
        
    </select>

   
    
    <input type="date" name="fromDate">

    
    <input type="date" name="toDate">

    
    <button type="submit">generate report</button>

</form>
		
	</div>
</body>
</html>
    