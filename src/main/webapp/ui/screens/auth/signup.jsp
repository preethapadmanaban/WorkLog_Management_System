<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Work Log | Sign Up</title>

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
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.card {
    background: #ffffff;
    width: 400px;
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

/* Labels */
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
    font-size: 14px;
    border: 1px solid var(--border);
}

.field:focus {
    outline: none;
    border-color: var(--primary);
}

.password-wrapper {
    display: flex;
    align-items: center;
    border: 1px solid var(--border);
    border-radius: 6px;
    height: 45px;
}

.password-wrapper .field {
    border: none;
    background: none;
}

.eye {
    cursor: pointer;
    margin-right: 10px;
}

.eye img {
    width: 18px;
    height: 18px;
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
    margin-top: 10px;
}

.btn-primary:hover {
    background: var(--primary-hover);
}

/* Link */
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

.error_message {
    color: red;
    font-size: 12px;
    margin-bottom: 8px;
}
</style>
</head>

<body>

<div class="container">
    <h2>Worklog Management System</h2>

    <div class="card">
        <div class="logo">Create Employee Account</div>

        <form id="myForm" method="post" action="/worklog/controller?action=signup">

            <!-- Full Name -->
            <label for="fullname">Employee Full Name</label>
            <input type="text" id="fullname" class="field" placeholder="Enter your fullname">
            <p id="error_message_fullname" class="error_message"></p>

            <!-- Email -->
            <label for="email">Email Address</label>
            <input type="text" id="email" class="field" placeholder="sample@vastpro.in">
            <p id="error_message_email" class="error_message"></p>

            <!-- Password -->
            <label for="password">Password</label>
            <div class="password-wrapper" id="password-wrapper">
                <input type="password" id="password" class="field" placeholder="Enter your password">
                <span class="eye" id="togglePassword">
                    <img src="<%=request.getContextPath()%>/ui/images/eye-open-svgrepo-com.svg" alt="eye">
                </span>
            </div>
            
            <p id="error_message_password" class="error_message"></p>

            <!-- Confirm Password -->
            <label for="confirmPassword">Confirm Password</label>
            <div class="password-wrapper">
                <input type="password" id="confirmPassword" class="field" placeholder="Confirm the password">
            </div>
            <p id="error_message_confirm_password" class="error_message"></p>

            <!-- Button -->
            <button type="submit" class="btn-primary">Create Account</button>

            <!-- Login link -->
            <a href="/worklog/controller?action=loginPage" class="create-link">
                Already have an account? Go to Login
            </a>

        </form>
    </div>
</div>

<script>
function clearAllErrorMessage(){
	const errorElementsArray = document.querySelectorAll(".error_message");
	//document.getElementById("message").innerText="";
	for(let i=0;i<errorElementsArray.length;i++){
		errorElementsArray[i].innerText="";
	}
}

let form = document.getElementById("myForm");

form.addEventListener("submit", function(e){
	e.preventDefault();
	clearAllErrorMessage();

	let isValid = true;
	let mandatory = "This field is mandatory";

	let user_name = document.getElementById("fullname").value.trim();
	let email = document.getElementById("email").value.trim();
	let password = document.getElementById("password").value;
	let confirm_password = document.getElementById("confirmPassword").value;

	// Full name validation
	if(user_name === ""){
		document.getElementById("error_message_fullname").innerText = mandatory;
		document.getElementById("fullname").style.border = "2px solid red";
		isValid = false;
	} else if(user_name.length <= 3){
		document.getElementById("error_message_fullname").innerText = "Please enter a valid name.";
		document.getElementById("fullname").style.border = "2px solid red";
		isValid = false;
	}else{
		document.getElementById("fullname").style.border = "0.5px solid grey";
	}

	// Email validation
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
		err_msg=err_msg.concat("Email address must contain the ‘@’ symbol.\n")
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

	// Password validation
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
			err_msg=err_msg.concat("Password must be 6–20 characters\n")
			
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
	

	// Confirm password validation
	if(confirm_password === ""){
		document.getElementById("error_message_confirm_password").innerText = mandatory;
		document.getElementById("confirmPassword").style.border = "2px solid red";
		isValid = false;
	} else if(password !== confirm_password){
		document.getElementById("error_message_confirm_password").innerText ="Passwords and confirm password do not match.";
		document.getElementById("confirmPassword").style.border = "2px solid red";
		isValid = false;
	}else if(password===confirm_password){
		document.getElementById("confirmPassword").style.border = "0.5px solid grey";
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
 } else {
   password.type = "password";
 }
});

</script>

</body>
</html>
