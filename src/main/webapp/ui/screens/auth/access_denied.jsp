<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Access Denied</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<style>
*{
    margin: 0;
    padding: 0;
}
.flex-container{
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 90vh;
}

.cta-button{
    background-color: #1F3A5F;
    color: white;
    padding: 12px;
    border-radius: 6px;
    font-size: 15px;
    font-weight: 500;
    text-decoration: none;
}

.cta-button:hover{
    background-color: #162C46;
}

.hero-img-div img{
    width: 50px;
}
.hero-text-div p{
    font-style: italic;
    font-weight: 500;
    background-color: rgb(184, 245, 225);
    padding: 5px 3px;
    border-radius: 5px 2px;
}
</style>
</head>
<body>
    <div class="flex-container">
        <h2>Worklog Management System</h2>
        <div class="hero-img-div">
            <img src="<%= request.getContextPath() %>/ui/assets/icons/access-denied.png" alt="Access Denied">
        </div>
        <h3>Access Denied</h3>
        <div class="hero-text-div">
            <p>&ldquo; You’re very welcome here—this page just isn’t available for your account.&rdquo;</p>
        </div>
        <a href="/worklog/controller?action=loginPage" class="cta-button">Go To Login</a>
    </div>
</body>


</html>