<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Work Log | Login</title>

<style>
:root {
    --primary: #1F3A5F;
    --primary-hover: #162C46;
    --border: #D1D5DB;
    --bg: #F4F6F9;
    --text: #2E2E2E;
    --muted: #6B7280;
}

* {
    box-sizing: border-box;
}

body {
    margin: 0;
    font-family: "Segoe UI", Arial, sans-serif;
    background: var(--bg);
    color: var(--text);
}

.container {
    height: 100vh;
    display: flex;
flex-direction:column;
    justify-content: center;
    align-items: center;
}

.card {
    background: #ffffff;
    width: 380px;
    padding: 28px;
    border-radius: 10px;
    box-shadow: 0 6px 20px rgba(0,0,0,0.08);
}

.logo {
    text-align: center;
    font-size: 26px;
    font-weight: 600;
    color: var(--primary);
    margin-bottom: 20px;
}

/* Label */
label {
    display: block;
    margin-bottom: 6px;
    font-size: 13px;
    font-weight: 500;
    color: var(--muted);
}

.field {
    width: 100%;
    padding: 12px 14px;
    border-radius: 6px;
    border-radius: 6px;
    font-size: 14px;
  border: 1px solid var(--border);
}

.field:focus {
    outline: none;
    border-color: var(--primary);
}

#password-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border: 1px solid var(--border);  
    border-radius: 6px;  
    height: 45px;
}

#password-wrapper .field{
   border: none;
   background: none;
}



.eye {    
    cursor: pointer;
    font-size: 16px;
    color: var(--muted);
    margin-right: .5em;
}

.eye:hover {
    color: var(--primary);
}

/* Button */
.btn-primary {
    width: 100%;
    padding: 12px;
    background: var(--primary);
    border: none;
    border-radius: 6px;
    color: #ffffff;
    font-size: 15px;
    font-weight: 500;
    cursor: pointer;
    margin-top: 6px;
}

.btn-primary:hover {
    background: var(--primary-hover);
}

/* Anchor link */
.create-link {
    display: block;
    margin-top: 16px;
    text-align: center;
    font-size: 14px;
    color: var(--primary);
    text-decoration: none;
    font-weight: 500;
}

.create-link:hover {
    text-decoration: underline;
}
img{
height:20px;
width:20px;
}

.error_message {
  color: red;
  font-size: 12px;
}

.server_error_message {
  color: red;
  font-size: 12px;
  text-align: center;
  margin: 0px auto;
}
.close-eye{
	display: none;
}

</style>
</head>


<body>

<div class="container">
	<h2>Worklog Management System</h2>
    <div class="card">

        <div class="logo">Login Screen</div>

        <form action="/worklog/controller" method="post" class="myform">
			<input type="hidden" name="action" value="login">
            <label for="email">Email address</label>
            <input type="text"
                   id="email"
                   name="email"
                   class="field"
                   placeholder="sample@gmail.com"
                   >
			<p id="error_message_email" class="error_message"></p>
            <label for="password">Password</label>

			<div id="password-wrapper">
    			<input type="password" id="password" name="password" class="field" placeholder="Your password">
   				<span class="eye" id="togglePassword">
        		<img class="open-eye" id="open-eye" src="<%=request.getContextPath()%>/ui/icons/eye-open.svg">
        		<img class="close-eye" id="close-eye" src="<%=request.getContextPath()%>/ui/icons/eye-slash.svg">
    			</span>
			</div>

         <p id="error_message_password" class="error_message"></p>

			<% if(request.getAttribute("message") != null){
			%>
			<p class="server_error_message"><%=request.getAttribute("message") %></p>
			<%
				}
			 %>
	
            <button type="submit" class="btn-primary">
                Login
            </button>

            <a href="/worklog/controller?action=signup" class="create-link">
                Create New Account
            </a>

        </form>

    </div>
</div>

<script>


function clearAllErrorMessage(){
	const errorElementsArray = document.querySelectorAll(".error_message");
	for(let i=0;i<errorElementsArray.length;i++){
		errorElementsArray[i].innerText="";
	}
}
	let form=document.querySelector(".myform");
	let mandatory="This field is mandatory"
	form.addEventListener("submit",(e)=>{
		e.preventDefault();
		clearAllErrorMessage()
		let email=document.getElementById("email").value;
		let password=document.getElementById("password").value;
		let isValid=true;
		let valid_email=true
		let err_msg=""
		const email_regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
		if(email === ""){
			
			document.getElementById("error_message_email").innerText = mandatory;
			
			document.getElementById("email").style.border = "2px solid red";
			isValid = false;
			valid_email=false
		} if(email!=email.toLowerCase()){
			err_msg=err_msg.concat("Email address must be in lowercase.\n")
			document.getElementById("email").style.border = "2px solid red";
			isValid = false;
			
		}if(!email.includes(".")){
			err_msg=err_msg.concat("Email address must include a valid domain name '.'\n")
			document.getElementById("email").style.border = "2px solid red";
			isValid = false;
			
		}if(!email.includes("@")){
			err_msg=err_msg.concat("Email address must contain the @ symbol.\n")
			document.getElementById("email").style.border = "2px solid red";
			isValid = false;
			
		}
		
		if(valid_email){
			document.getElementById("error_message_email").innerText =err_msg;
			if(!email_regex.test(email)){
				err_msg=err_msg.concat("The email address format is invalid.")
				document.getElementById("error_message_email").innerText =err_msg;
				document.getElementById("email").style.border = "2px solid red";
			}
			
		}
		if(email_regex.test(email)){
			document.getElementById("email").style.border = "0.5px solid grey";
		}

		
		//password format validation
		const password_regex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()\-+.]).{6,20}$/;
		
		err_msg=""
		let valid_psw=true
		if(password === ""){
			document.getElementById("error_message_password").innerText = mandatory;
			document.getElementById("password-wrapper").style.border = "2px solid red";
			isValid = false;
			valid_psw=false
		}else{
			if(password.length<6){
				err_msg=err_msg.concat("Password must be 6 to 20 characters\n")
				
				isValid=false;
			}if(!/[A-Z]/.test(password)){
				err_msg=err_msg.concat("Must contain at least one uppercase letter\n")
				
				isValid=false;
			}if(!/[!@#$%^&*()\-+.]/.test(password)){
				err_msg=err_msg.concat("Must contain at least one special symbol (!@#$...)\n")
				
				isValid=false;
				
			}
			if(!/[a-z]/.test(password)){
				err_msg=err_msg.concat("Must contain at least one lowercase letter\n")
				
				isValid=false;
			}if(!/\d/.test(password)){
				err_msg=err_msg.concat("Must contain at least one number\n")
				isValid=false;
				
			}
			document.getElementById("password-wrapper").style.border = "2px solid red";
			document.getElementById("error_message_password").innerText=err_msg;
			
		}
		if(password_regex.test(password)){
			document.getElementById("password-wrapper").style.border = "0.5px solid grey";
		}
		if(isValid){
			form.submit();
		}
		
	});
	let button = document.getElementById("togglePassword");

	button.addEventListener("click", () => {
	let password = document.getElementById("password");

	 if (password.type === "password") {
		password.type = "text";
		document.getElementById("open-eye").style.display = "block";
		document.getElementById("close-eye").style.display = "none";
	 } else {
		document.getElementById("open-eye").style.display = "none";
		document.getElementById("close-eye").style.display = "block";
	   password.type = "password";
	 }
	});

</script>

</body>
</html>
