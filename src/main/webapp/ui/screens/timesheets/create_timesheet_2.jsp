<%@page import="com.worklog.entities.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Create Timesheet</title>
<link href="/worklog/ui/css/styles.css" rel="stylesheet">
<style>
	.entry-card {
	  max-width: 320px;
	  padding: 1rem;
	  background-color: #fff;
	  border-radius: 10px;
	  box-shadow: 20px 20px 30px rgba(0, 0, 0, .05);
	}
</style>
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
<jsp:include page="/ui/screens/common/modal.jsp"></jsp:include>
	
	<div class="loader_outlier" id="loader_outlier">
		<div class="loader_container">
			<span class="loader"></span>
		</div>
	</div>
	

	<div class="timesheet_container">	
		<div class="filter_section">
	        <button>Back</button>
	        <div>
	            <label for="">Enter Work Date </label>
	            <input class="nice-form-input"  type="date" name="work_date" id="work_date">
	        </div>
	    </div>
	
	    <div class="create_timesheet_section">
	        <div class="nice-form">
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
	                    <button type="submit" class="submit_button">Add Entry</button>
	                </div>
	            </form>
	        </div>
	        <div class="entry_section" id="entry_section"><h3>Entry Section</h3></div>
	    </div>
	    <div class="full_div_action_button">
	        <button onclick="createTimesheet()" class="submit_button" >Create Timesheet</button>
	    </div>
	</div>
    <script>
    	
    	let modal_obj = new Modal();
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
                hourseSpentInput.nextElementSibling.innerText = "Please enter Hourse spend in numbers."
                hourseSpentInput.value = "";
            }
            else if(hourseSpentValue <= 0.0 || hourseSpentValue >8.0)
            {
                hourseSpentInput.nextElementSibling.innerText = "Hourse spend duration should 0.1 - 8 hours."
                hourseSpentInput.value = "";
            }   
            else if(!(/^\d*(\.\d+)?$|^\d+\.\d*$/.test(hourseSpentValue))){
                hourseSpentInput.nextElementSibling.innerText = "Please enter Hourse spend in numbers."
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
        
            enrty_card.innerHTML = "<div class='entry_card'>"+
                    	"<span class='title'>🍪 "+ entry_object.title +"</span>" +  
                    	"<p class='description'>" + entry_object.notes + "</p>"+
                    	"<div class='actions'>" + 
                        "<button class='pref'> " +
                           "Taken Time  -  " + entry_object.hours_spent   + " hours " + 
                        "</button>" +
                        "<button class='accept' onclick='delete_entry_card(event, " + entry_object.task_id +")'> " +
                            "Delete" +  
                        "</button>"+
                        "</div>";


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
        function delete_entry_card(e, task_id){
			// remove the card from ui.
            e.target.parentNode.parentNode.parentNode.remove();
            addNoEntriesDoc();
            // remove the object in the stored array.
            console.log(task_id);
            entries = entries.filter(entry => parseInt(entry.task_id) !== task_id);
            console.log(entries);
        }
               
        function addNoEntriesDoc(){
        	/* Entries */
            let entriesChild = document.getElementById("entry_section").childNodes;
            if(entriesChild.length <= 1){
 			   let doc = document.createElement("p");
 			   doc.classList.add("entries_doc");
 			   doc.setAttribute("id", "entries_doc");
 			   doc.innerText = "No entries, Fill the form to add entries!";
 			   document.getElementById("entry_section").appendChild(doc);
            }
        }
        
        function createTimesheet(){
			if(entries == null || entries.length === 0){
				modal_obj.error("Please Create Atleast One Entry!");
				return;
			}
			
			document.getElementById("loader_outlier").style.display = "flex";
			console.log("Entries => ", entries);
			let work_date = document.getElementById("work_date").value;	
			console.log("payload => ", {"manager_id" : manager_id, 
				  "work_date" : work_date, 
				  "entries" : entries});
			
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
				if(data.status === "success")
				{
					modal_obj.success(data.message);
				}
				else
				{
					modal_obj.error(data.message);
				}
			})
			.catch( err=> console.log("error =>", err))
			.finally(()=>{
				document.getElementById("loader_outlier").style.display = "none";
			});
        }
        
     </script>
</body>
</html>