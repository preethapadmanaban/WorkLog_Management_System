<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Create Timesheet</title>
<link href="/worklog/ui/css/styles.css" rel="stylesheet">
<script src="${pageContext.request.contextPath}/ui/js/Modal.js"></script>
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/modal.jsp"></jsp:include>
	
	<div class="loader_outlier" id="loader_outlier">
		<div class="loader_container">
			<span class="loader"></span>
		</div>
	</div>
	
		<div class="tasks-filter">
		  <div>
		    <h3>Create Timesheet</h3>
		  </div>
			  <div class="tasks-filter-form">			
			    <div class="tasks-filter-row">
			      <div class="tasks-filter-item">
			        <label>Work Date</label>
			       	<input class="tasks-filter-input"  type="date" name="work_date" id="work_date">
			      </div>
			    </div>
			  </div>
		</div>
	    <div class="create_timesheet_section">
	        <div class="timesheet-entry-form flex-item">
	        <h3>Add Entry Form</h3>
	            <form onsubmit="add_enrty(event)">
	                <div class="nice-form-group">
	                    <label for="">Choose the Task <span class="mandatory_symbol">*</span> </label>
	                    <select class="nice-form-input" name="task_id" id="task_id" onchange="setTask(event)">
	                        <option value=" " selected>Select Task</option>
	                        <% List<Task> tasks =(List<Task>) request.getAttribute("tasks"); 
	                        	if(tasks != null && tasks.size() != 0){
	                        		for(Task task : tasks){
	                        %>
	                        		<option value="<%=task.getId()%>"><%=task.getTitle() %></option>
	                        <%
	                        		}
	                        	}
	                        %>
	                    </select>
	                    <span id="error_message"></span> 
	                </div>
	                <div class="nice-form-group">
	                    <label for="">Hourse Spent (in hours) <span class="mandatory_symbol">*</span></label>
	                    <input type="text" class="nice-form-input" id="hours_spent" name="hours_spent"  onkeyup="setHoursSpent(event)">
	                    <span id="error_message"></span> 
	                </div>
	                <div class="nice-form-group">
	                    <label for="">Notes <span class="mandatory_symbol">*</span></label>
	                    <textarea id="notes" class="nice-form-input" name="notes"  onkeyup="setNotes(event)"></textarea>
	                    <span id="error_message"></span> 
	                </div>
	                <div>
	                    <button type="submit" class="button button-secondary">Add Entry</button>
	                </div>
	            </form>
	        </div>
	        <div class="entry_section flex-item" id="entry_section">
	        	<div class="entry-header-section">
	        		<h3>Entry Section</h3>
	        	</div>
	        
	        </div>
	    </div>
	    <div class="full_div_action_button">
	        <button onclick="createTimesheet()" class="button button-primary" >Create Timesheet</button>
	    </div>
	</div>
    <script>
    	let manager_id = <%=(tasks != null || tasks.size() == 0) ? tasks.get(0).getCreated_by() : -1%>;
    	let entries = [];
        let entry_object = { task_id : 0, title : "", hours_spent: 0.0, notes: ""};

    	
        function Entry(task_id, hours_spent, notes){
            this.task_id = task_id;
            this.hours_spent = hours_spent;
            this.notes = notes;
        };
        

        // set to today
       document.addEventListener('DOMContentLoaded', ()=>{
           const today = new Date(Date.now()).toJSON().split("T")[0];
           document.getElementById("work_date").value = today
           document.getElementById("work_date").setAttribute("max", today);
           
           addNoEntriesDoc();
           clearForm();
       });

        function isNull(value){
            if(value == null || value === "")
            {
                return true;
            }
            return false;
        }

     	// notes
        function setNotes(e){
            let notesInput = e.target;
            let notesValue = notesInput.value.trim();
            entry_object[notesInput.name] = notesInput.value;
            if(isNull(notesValue))
            {
                notesInput.nextElementSibling.innerText = "Please enter valid Notes.";
            }
            else{
                notesInput.nextElementSibling.innerText = "";
            }
        }

        // hours spent
        function setHoursSpent(e){
            let hourseSpentInput = e.target;
            let hourseSpentValue = hourseSpentInput.value.trim();
            entry_object[hourseSpentInput.name] = hourseSpentValue;
            if(isNull(hourseSpentValue))
            {
                hourseSpentInput.nextElementSibling.innerText = "Please enter hours spent in numbers."
                hourseSpentInput.value = "";
            }
            else if(hourseSpentValue <= 0.0 || hourseSpentValue >8.0)
            {
                hourseSpentInput.nextElementSibling.innerText = "Hourse spend duration should be 1 - 8 hours."
                hourseSpentInput.value = "";
            }   
            else if(!(/^\d*(\.\d+)?$|^\d+\.\d*$/.test(hourseSpentValue))){
                hourseSpentInput.nextElementSibling.innerText = "Please enter hours spent in numbers."
                hourseSpentInput.value = "";
            }
            else
            {
                hourseSpentInput.nextElementSibling.innerText = "";
            }
        }
        
        function isAlreadySelected(val){
			let flag = false;
        	entries.forEach((entry)=>{
				if(entry.task_id === val){
					flag = true;
					return;
				}
        	});
        	return flag;
        }
        
     	// task
        function setTask(e){
            let taskInput = e.target;
            let val = taskInput.value.trim();
            entry_object["title"] = taskInput.options[taskInput.selectedIndex].text;
            entry_object[e.target.name] = val;
            if(isNull(val)){
                // set the error value.
                taskInput.nextElementSibling.innerText = "Please Select a Task";
            }
            else if(isAlreadySelected(val) == true){
            	taskInput.nextElementSibling.innerText = "Task Already Rercorded, Choose Others.";
            }
            else{
                taskInput.nextElementSibling.innerText = "";
            }
        }

        function clearForm(){
            document.getElementById("hours_spent").value = "";            
            // select tag
            document.querySelector("select").selectedIndex = 0;

            // text area
            document.querySelector("textarea").value = "";   
        }
         
        // final validation;
        function validate_data(){
            let isValid = true;
            Object.keys(entry_object).forEach((key)=>{
                let currentValue = entry_object[key];
                if(currentValue == "" || currentValue == null){
                    console.log(key + " vaildation failed");
                    isValid = false;
                    if(key === "title") {
                        key="task_id";
                    }
                    document.getElementById(key).nextElementSibling.innerText = "This is mandatory field";
                }
                // if(currentValue == "hours")
            });
            if(isValid == true){
                return true;
            }
            else{
                return false;
            }
        }

        // create card from the input
        function add_enrty(e){
            e.preventDefault();
            let isValid = validate_data();
            if(isValid == false){
                //alert("invalid data");
                return;
            }
            entries.push(new Entry(entry_object.task_id, entry_object.hours_spent, entry_object.notes));
            let enrty_card = document.createElement("div");
        
            enrty_card.innerHTML = '<div class="entry-card" id=entry_'+ entry_object.task_id + '>'+
							            '<div class="entry-card-header">' + 
							            '<h3 class="task-title"> Task : '+ entry_object.title +'</h3>' +
							            '<span class="task-duration">⏱  '+ entry_object.hours_spent +' hrs</span>' + 
							          '</div>' +
								
							          '<p class="task-notes"> Notes : ' + 
							            entry_object.notes + 
							          '</p>' + 							
							          '<div class="task-actions">' + 
							            '<button class="btn btn-danger" onclick="delete_entry_card(' + entry_object.task_id  + ')">Delete</button>' + 
							          '</div>' + 
							        '</div>';
            let entries_doc = document.getElementById("entries_doc"); // removes when 1st element is added.	
            if(entries_doc != null){
            	entries_doc.remove();
            }
            document.getElementById("entry_section").appendChild(enrty_card);
                

            clearForm();
            entry_object.task_id = "";
            entry_object.title = "";
            entry_object.hours_spent = "";
            entry_object.notes = "";
            console.log(entries);
        }

        // delete entry card
        function delete_entry_card(task_id){
			// remove the card from ui.
            // e.target.parentNode.parentNode.remove();
			document.getElementById("entry_" + task_id).remove();
           
            // remove the object in the stored array.
            console.log(task_id);
            entries = entries.filter(entry => parseInt(entry.task_id) !== task_id);
            console.log(entries);
            // docs in the ui
            addNoEntriesDoc();
        }
               
        function addNoEntriesDoc(){
        	/* Entries */
            if(entries.length <= 0){
 			   let doc = document.createElement("p");
 			   doc.classList.add("entries_doc");
 			   doc.setAttribute("id", "entries_doc");
 			   doc.innerText = "No entries, Fill the form to add entries!";
 			   document.getElementById("entry_section").appendChild(doc);
            }
        }
        
        function createTimesheet(){
        	if(entries == null || entries.length === 0){
				openPopup("Please Create Atleast One Entry!", "Data Required", "error");
				return;
			}
			openPopup("Be sure to submit the timesheet, You cannot edit further!", "Confirmation Required", "info", {
		            showCancel: true,
		            okText: "Yes, Proceed!",
		            cancelText: "Cancel",
		            onOk: () => {proceedToSubmit()},
		            onCancel: () => {} // do nothing
			});			
        }
        
        function proceedToSubmit(){
        	document.getElementById("loader_outlier").style.display = "flex";
			console.log("Entries => ", entries);
			let work_date = document.getElementById("work_date").value;	
        	fetch("/worklog/controller/api?action=createTimesheet", {
				method: "POST",
				header: {"Content-Type": "application/json"},
				body: JSON.stringify({"manager_id" : manager_id, 
									  "work_date" : work_date, 
									  "entries" : entries})
			})
			.then( (res) => res.json() )
			.then( (data) =>{ 
				document.getElementById("loader_outlier").style.display = "none";
				
				if(data.success === true){
					openPopup(data.message, "Success ✅", "success", {onOk:()=>{window.location.reload()}});
				}
				else{
					openPopup(data.message, "Failed ❌", "error",{onOk:()=>{window.location.reload()}});
					
				}
			})
			.catch( err=> console.log("error =>", err))
			.finally(()=>{
				document.getElementById("loader_outlier").style.display = "none";
				
			});
        };
        
     </script>
</body>
</html>