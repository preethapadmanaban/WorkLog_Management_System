<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Worklog management system</title>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>
<link rel="stylesheet" href="/worklog/ui/css/styles.css">
</head>
<body> 
<div class="mobile_auth_page">
	<div class="app_name_div"><h3>Worklog Management</h3></div>
	
	<!-- <form action="login" method="post" class="mx-auto my-20 max-w-md space-y-4 rounded-lg border border-gray-300 bg-gray-100 p-6"> -->
	<form action="/worklog/controller" method="post" class="mx-auto my-20 max-w-md space-y-4 rounded-lg border border-gray-300 bg-gray-100 p-6 myform">
	  
		<div class="flex justify-center">
		 	<label class="block text-md text-2xl font-medium text-gray-900">Login Screen</label>
		</div>	
		<div>
			<input class="mt-1 w-full rounded-lg border-black-700 border p-2" value="login" name="action" id="action" type="hidden">
		</div>
		<div>
			<label class="block text-sm font-medium text-gray-900" for="email">Your Email</label>
			<input class="mt-1 w-full rounded-lg border-black-700 border p-2" name="email" id="email" type="text" placeholder="Your email">
			<div  class="flex justify-center">
				<span id="error_message_email" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>
		
		<div>
		  	<label class="block text-sm font-medium text-gray-900" for="message">Your Password</label>
			<input class="mt-1 w-full rounded-lg border-black-700 border p-2" name="password" id="password" type="password" placeholder="Your password">
			<div  class="flex justify-center">
				<span id="error_message_password" class="text-red-700 font-medium error_message"></span>
			</div>
		</div>
		<div class="flex justify-center">
		 	<span  class="text-red-700 font-medium"><%=request.getAttribute("message") !=null ? request.getAttribute("message"): "" %></span>
		</div>
	
		<button class="block w-full rounded-lg border border-indigo-600 bg-indigo-600 px-12 py-3 text-sm font-medium text-white transition-colors hover:bg-transparent hover:text-indigo-600" type="submit">
		  	Login
		</button>
		 <div class="flex justify-center">
			<a href="/worklog/controller?action=signupPage" class="mx-auto inline-flex items-center font-medium text-fg-brand hover:underline">
				New Employee? Create account 
				<svg class="w-5 h-5 ms-1 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 12H5m14 0-4 4m4-4-4-4"/></svg>
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
	let form=document.querySelector(".myform");
	
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
			document.getElementById("password").style.border = "2px solid red";
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
			document.getElementById("password").style.border = "2px solid red";
			document.getElementById("error_message_password").innerText=err_msg;
			
		}
		if(password_regex.test(password)){
			document.getElementById("password").style.border = "0.5px solid grey";
		}
		if(isValid){
			form.submit();
		}
		
	})
</script>
</body>
</html>
