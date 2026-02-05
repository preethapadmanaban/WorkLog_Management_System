<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Not Found</title>
    <style>
        *{
            margin: 0;
            padding: 0;
        }

        body{
            text-align: center;
            background-color: #F4F6F9;
            height: 90vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            gap: 10px;
        }
        .flex-container{
            display: flex;
            gap: 20px;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            background-color: white;
            width: fit-content;
            padding: 40px;
            border-radius: 10px;
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

        .hero-text{
           color: #1F3A5F
        }

        .hero-text-div p{
            font-style: italic;
            font-weight: 500;
            /* background-color: rgb(184, 245, 225); */
            padding: 5px 3px;
            border-radius: 5px 2px;
        }

    </style>
</head>
<body>
        <h2>Worklog Management System<h2>
        <div class="flex-container">   
            <div class="hero-img-div">
                <img src="<%= request.getContextPath() %>/ui/assets/icons/404-error.png" alt="Not Found">
            </div>
            <h3 class="hero-text">Resource Not Found</h3>
            <div class="hero-text-div">
                <p>&ldquo; The reqeusted resource was not found!&rdquo;</p>
            </div>
            <a href="<%=request.getContextPath()%>/controller?action=loginPage" class="cta-button">Try Login</a>
        </div>
</body>
</html>