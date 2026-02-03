<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Create Account</title>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<link rel="stylesheet" type="text/css" href="/worklog/ui/css/styles.css">
</head>
<body>

<div class="mobile_auth_page">
	<div class="app_name_div"><h3>Worklog Management</h3></div>
	<form id="myForm" action="/worklog/controller" method="post" 
		class="mx-auto my-20 max-w-md space-y-4 rounded-lg border border-gray-300 bg-gray-100 p-6">
		<div class="flex justify-center">
			<label class="block text-md text-2xl font-medium text-gray-900">Create Employee Screen</label>
		</div>
		<div>
			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="action" id="signup" type="text" value="signup"
				hidden>
		</div>
		<div >
			<label class="block text-sm font-medium text-gray-900" for="name">Employee Full Name</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="name" id="name" type="text"
				placeholder="Your full name" >
			<div class="flex justify-center">
				<span id="error_message_fullname" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>
		
		<div >
			<label class="block text-sm font-medium text-gray-900" for="email">Employee Email</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="email" id="email" type="text"
				placeholder="Your email">
			<div  class="flex justify-center">
				<span id="error_message_email" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>

		<div >
			<label class="block text-sm font-medium text-gray-900" for="password">Employee Password</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="password" id="password" type="password"
				placeholder="Your password">
			<div  class="flex justify-center">
				<span id="error_message_password" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>
		
		<div >
			<label class="block text-sm font-medium text-gray-900" for="password">Confirm Password</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="confirm_password" id="confirm_password" type="password"
				placeholder="Confirm password">
			<div class="flex justify-center">
				<span id="error_message_confirm_password" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>
		
		<!-- <div>
			<label class="block text-sm font-medium text-gray-900" for="password">Role</label>
			<div class="flex justify-center item-center gap-5">
				<label class="flex items-center">
				    <input type="radio" name="role" value="Employee" class="form-radio h-4 w-4 bg-orange-600" >
				    <span class="ml-2 text-gray-700 font-medium text-md">Employee</span>
				  </label>
				  <label class="flex items-center">
				    <input type="radio" name="role" value="Manager" class="form-radio h-4 w-4 bg-orange-600" checked>
				    <span class="ml-2 text-gray-700 font-medium text-md">Manager</span>
				  </label>
			</div> -->
			  		
		
		<div class="flex justify-center">
			<span class="text-red-700 font-medium error_message"><%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%></span>
		</div>

		<button
			class="block w-full rounded-lg border border-orange-600 bg-orange-600 px-12 py-3 text-sm font-medium text-white transition-colors hover:bg-transparent hover:text-indigo-600"">Signup</button>
		<div class="flex justify-center">
			<a href="/worklog/controller?action=loginPage"
				class="inline-flex items-center font-medium text-fg-brand hover:underline">
				Already have an account? Go to login 
				<svg class="w-5 h-5 ms-1 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"> <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 12H5m14 0-4 4m4-4-4-4" /></svg>
			</a>
		</div>
	</form>
</div>
<script>
function clearAllErrorMessage(){
	const errorElementsArray = document.querySelectorAll(".error_message");
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

	let user_name = document.getElementById("name").value.trim();
	let email = document.getElementById("email").value.trim();
	let password = document.getElementById("password").value;
	let confirm_password = document.getElementById("confirm_password").value;

	// Full name validation
	if(user_name === ""){
		document.getElementById("error_message_fullname").innerText = mandatory;
		document.getElementById("name").style.border = "2px solid red";
		isValid = false;
	} else if(user_name.length <= 3){
		document.getElementById("error_message_fullname").innerText = "Please enter a valid name.";
		document.getElementById("name").style.border = "2px solid red";
		isValid = false;
	}else{
		document.getElementById("name").style.border = "0.5px solid grey";
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
		document.getElementById("password").style.border = "2px solid red";
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
		document.getElementById("password").style.border = "2px solid red";
		document.getElementById("error_message_password").innerText=err_msg;
		
	}
	if(password_regex.test(password)){
		document.getElementById("password").style.border = "0.5px solid grey";
	}
	

	// Confirm password validation
	if(confirm_password === ""){
		document.getElementById("error_message_confirm_password").innerText = mandatory;
		document.getElementById("confirm_password").style.border = "2px solid red";
		isValid = false;
	} else if(password !== confirm_password){
		document.getElementById("error_message_confirm_password").innerText ="Passwords and confirm password do not match.";
		document.getElementById("confirm_password").style.border = "2px solid red";
		isValid = false;
	}else if(password===confirm_password){
		document.getElementById("confirm_password").style.border = "0.5px solid grey";
	}

	if(isValid){
		form.submit();
	}
});
</script>
</body>
</html>