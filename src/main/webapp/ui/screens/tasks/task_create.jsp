<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>
<%@ page import="java.time.LocalDate" %>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create task</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
<script src="${pageContext.request.contextPath}/ui/js/Modal.js"></script>
</head>
<body class="create-task-page">
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/message.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/modal.jsp"></jsp:include>


  <section class="nice-form">
      <h3>
        New Task
      </h3>
      <p>Create a new task and assign to a employee.</p>
      
      <form action="<%= request.getContextPath() %>/controller" method="post">
   	  <input type="hidden" name="action" value="createTask">

      <div class="nice-form-group"> 
        <label>Title<span class="required">*</span></label>
        <input class="nice-form-input" name="title" type="text" placeholder="Task title"/>
        <small class="error-text" id="titleError"></small>
      </div>

      <div class="nice-form-group">
        <label>Description<span class="required">*</span></label>
        <input class="nice-form-input" name="description" type="text" placeholder="Your description"/>
        <small class="error-text" id="descError"></small>
      </div>

      <div class="nice-form-group">
        <label>Select employee<span class="required">*</span></label>
        <select class="nice-form-input" name="assigned_to" id="assigned_to">
			  <option value="">-- Select employee --</option>
			  <%
			    List<Employee> list = (List<Employee>) request.getAttribute("members");
			    if(list != null){
			      for(Employee e : list){
			  %>
			      <option value="<%= e.getId() %>"><%= e.getName() %></option>
			  <%
			      }
			    }
			  %>
		</select>
        <small class="error-text" id="empError"></small>
      </div>

      <div class="nice-form-group">
        <label for="deadline">Deadline<span class="required">*</span></label>
        <%
    		String today = LocalDate.now().toString();
		%>    
        <input class="nice-form-input" type="date" id="deadline" name="deadline" placeholder="Your password" min="<%= today %>"/>
        <small class="error-text" id="deadlineError"></small>
      </div>
      
      <p id="formError" class="form-error"></p>
      
		<button type="button" class="submit_button" onclick="submitTask()">Submit</button>
      
      </form>
    </section>
    
   <script>
		function setError(field, errorId, msg){
		  field.classList.add("input-error");
		  document.getElementById(errorId).textContent = msg;
		}
		
		function clearError(field, errorId){
		  field.classList.remove("input-error");
		  document.getElementById(errorId).textContent = "";
		}
			
		async function submitTask(){
		  const title = document.querySelector("input[name='title']");
		  const desc  = document.querySelector("input[name='description']");
		  const emp   = document.querySelector("select[name='assigned_to']");
		  const dead  = document.querySelector("input[name='deadline']");

		  clearError(title, "titleError");
		  clearError(desc, "descError");
		  clearError(emp, "empError");
		  clearError(dead, "deadlineError");

		  const allEmpty =
		    !title.value.trim() &&
		    !desc.value.trim() &&
		    !emp.value &&
		    !dead.value;
		
		  if(allEmpty){
			openPopup("Please fill all the mandatory fields marked with (*).", "Field is required", "error");
		    setError(title, "titleError", "Please enter the task title.");
		    setError(desc, "descError", "Please enter the description.");
		    setError(emp, "empError", "Please select an employee.");
		    setError(dead, "deadlineError", "Please select a deadline.");
		    title.focus();
		    return;
		  }

		  let valid = true;
		
		  if(!title.value.trim()){ setError(title,"titleError","Please enter the task title."); valid=false; }
		  if(!desc.value.trim()){ setError(desc,"descError","Please enter the description."); valid=false; }
		  if(!emp.value){ setError(emp,"empError","Please select an employee."); valid=false; }
		  if(!dead.value){ setError(dead,"deadlineError","Please select a deadline."); valid=false; }
		
		  if(!valid) return;

		  const payload = new URLSearchParams();
		  payload.append("action", "createTask");
		  payload.append("title", title.value.trim());
		  payload.append("description", desc.value.trim());
		  payload.append("assigned_to", emp.value);
		  payload.append("deadline", dead.value);
		
		  try {
			  const res = await fetch("/worklog/controller/api?action=createTask", {
				  method: "POST",
				  headers: {
				    "Content-Type": "application/x-www-form-urlencoded",
				    "X-Requested-With": "fetch" 
				  },
				  body: payload.toString()
				});
		
		    const data = await res.json();
		
		    if(data.status === "success"){
		    	  openPopup("Your task has been submitted successfully.", "Success ✅", "success");

		    	  title.value = "";
		    	  desc.value  = "";
		    	  emp.value   = "";
		    	  dead.value  = "";

		    	} else {
		    	  openPopup(data.message || "Task creation failed.", "Error ❌", "error");
		    	}
		    
		  } catch (e) {
		    console.error(e);
		    openPopup("Server error. Please try again.");
		  }
		}

		document.addEventListener("DOMContentLoaded", () => {
		  const title = document.querySelector("input[name='title']");
		  const desc  = document.querySelector("input[name='description']");
		  const emp   = document.querySelector("select[name='assigned_to']");
		  const dead  = document.querySelector("input[name='deadline']");
		
		  title.addEventListener("input", () => clearError(title,"titleError"));
		  desc.addEventListener("input",  () => clearError(desc,"descError"));
		  emp.addEventListener("change",  () => clearError(emp,"empError"));
		  dead.addEventListener("change", () => clearError(dead,"deadlineError"));
		});
		
	</script>
<<<<<<< Updated upstream
	
	
=======

	<div id="modalOverlay" class="modal-overlay" style="display:none;">
	  <div class="modal-box" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
	    <h4 id="modalTitle" class="modal-title">Message</h4>
	    <p id="modalText" class="modal-text"></p>
	    <div class="modal-actions">
	      <button type="button" class="modal-ok" onclick="closePopup()">OK</button>
	    </div>
	  </div>
	</div>
>>>>>>> Stashed changes

</body>
</html>
