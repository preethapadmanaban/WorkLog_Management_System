<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign-Up</title>
<script src="https://cdn.jsdelivr.net/npm/@tailwindcss/browser@4"></script>

</head>
<body>

	<form action="/worklog/controller" method="post"
		class="mx-auto my-20 max-w-md space-y-4 rounded-lg border border-gray-300 bg-gray-100 p-6">
		<div class="flex justify-center">
			<label class="block text-md text-2xl font-medium text-gray-900">Signup
				page</label>
		</div>
		<div>
			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="action" id="signup" type="text" value="signup"
				hidden>
		</div>
		<div>
			<label class="block text-sm font-medium text-gray-900" for="name">Full name</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="name" id="name" type="text"
				placeholder="Your full name">
		</div>
		
		<div>
			<label class="block text-sm font-medium text-gray-900" for="email">Email</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="email" id="email" type="email"
				placeholder="Your email">
		</div>

		<div>
			<label class="block text-sm font-medium text-gray-900" for="password">Password</label>

			<input class="mt-1 w-full rounded-lg border-black-700 border p-2"
				name="password" id="password" type="password"
				placeholder="Your password">
		</div>
		
		<div>
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
			</div>
			  
				
		</div>
		
		
		<div class="flex justify-center">
			<span class="text-red-700 font-medium"><%=request.getAttribute("message") != null ? request.getAttribute("message") : ""%></span>
		</div>

		<button
			class="block w-full rounded-lg border border-orange-600 bg-orange-600 px-12 py-3 text-sm font-medium text-white transition-colors hover:bg-transparent hover:text-indigo-600"
			type="submit">Signup</button>
		<div class="flex justify-center">
			<a href="/worklog/ui/screens/login.jsp"
				class="inline-flex items-center font-medium text-fg-brand hover:underline">
				Go to login 
				<svg class="w-5 h-5 ms-1 rtl:rotate-180" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24"> <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 12H5m14 0-4 4m4-4-4-4" /></svg>
			</a>
		</div>
	</form>
</body>
</html>