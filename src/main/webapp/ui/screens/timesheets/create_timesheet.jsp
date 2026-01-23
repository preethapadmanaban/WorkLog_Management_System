<%@page import="java.util.List"%>
<%@page import="com.worklog.entities.Task"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>	Create Timesheet</title>
<link rel="stylesheet" href="/worklog/ui/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="/worklog/ui/css/styles.css" type="text/css">
</head>
<body>
   
    <div class="container-fluid">
        <h1>New Timesheet</h1>
        <div class="row">
            <div class="col">
                    Enter work date:  <input type="date" name="work_date" id="work_date" class="form-control"> </th>
                    <!-- <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" onclick="add_new_entry_row()" class="size-6 align-self-center add_timesheet_icon">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
                        </svg> -->
            </div>
            <div class="col">
                
            </div>
        </div>
    </div>
    <div class="container">
        <table class="table">
            <thead>
                <tr class="align-items-center">
                    <th>Task</th>
                    <th>Total hours spend</th>
                    <th colspan="2">Action</th>
                </tr>
            </thead>
            <tbody id="entry_table_body">
				
            </tbody>
        </table>
        <button class="btn btn-primary text-center" onclick="add_new_entry_row()">+ Add Row</button>
    </div>
    
    <div class="text-center w-100">
        <button class="btn btn-success" onclick="collect_data()">
            Submit Timesheet
        </button>
    </div>  

	<%System.out.print("inside timesheet creation jsp."); %>
    <script>
    
	    const tasks = ${tasks};
	    console.log("tasks =>", tasks);
	    
	    document.addEventListener('DOMContentLoaded', ()=>{
			document.getElementById("work_date").value = new Date(Date.now()).toISOString().split("T")[0];
            console.log("date set...");
        });

        function add_new_entry_row(){

            const tbody = document.getElementById("entry_table_body");
            const row = tbody.insertRow(-1); // -1 = append at end
            row.classList.add("entry_row");

            let cell = document.createElement("td"); // => here we create table cell.

            let selectTasks = document.createElement("select"); // => here we create select tag.
            selectTasks.setAttribute("id", "select_assigned_task");
            selectTasks.setAttribute("class", "form-select");
            
            let defaultOption = document.createElement("option");
            defaultOption.innerText = "Select task."
            selectTasks.appendChild(defaultOption);

            // let tasks_options = document.querySelectorAll(".assigned_task_options");
            // console.log("task_option => ", tasks_options);
            tasks.forEach((task)=>{
					let option_tag = document.createElement("option");		 // => here we populate the options in the created selecte tag.
					option_tag.innerText = task.title;
					option_tag.setAttribute("value", task.id);
					selectTasks.appendChild(option_tag);
            });

            cell.appendChild(selectTasks);

            row.appendChild(cell);

            cell = document.createElement("td");

            let total_hours_input = document.createElement("input");
            total_hours_input.setAttribute("type", "number"); 
            total_hours_input.setAttribute("id", "hours_spend"); 
            total_hours_input.setAttribute("required", "true");
            total_hours_input.setAttribute("class", "form-control");

            cell.appendChild(total_hours_input);

            row.appendChild(cell);

            cell = document.createElement("td");

            let action_button = document.createElement("button");
            action_button.innerText = "edit";
            action_button.setAttribute("class", "btn btn-secondary");

            cell.appendChild(action_button);
            
            row.appendChild(cell);

            cell = document.createElement("td");
            action_button = document.createElement("button");
            action_button.innerText = "delete";
            action_button.setAttribute("class", "btn btn-danger");
            action_button.setAttribute("onclick", "delete_entry_row(event)");
            cell.appendChild(action_button);
            
            row.appendChild(cell);
        }

        function delete_entry_row(e){
            e.target.parentNode.parentNode.remove();
        }
        
        function collect_data(){
			let work_date = document.getElementById("work_date").value;
            if(work_date == null || work_date == "")
            {
                alert("Please enter work date to continue further!");
                return;
            }   
            // collect the timesheet entry data in an object;

            // this logic is in pending, all the entry objects is hold the same value for all objects.
           let entry = {id: 0, hours_spend: 1.0};
            let temp = [];
            let entry_array = [];
            let enrty_rows = document.querySelectorAll(".entry_row"); // -> <tr>
                        enrty_rows.forEach((row)=>{
                            let row_cells = row.childNodes; // => [td, td, td, td]
                            for(let i=0; i<2; i++){
                                let cells = row_cells[i].childNodes;
                                console.log("cell =>", cells);
                                for(let j=0; j<1; j++){
                                    let element = cells[j];
                                    temp[i] = element.value;
                                    console.log("element => ", element);
                                }
                            }
                            entry.id = parseInt(temp[0]);
                            entry.hours_spend = parseFloat(temp[1]);
                            console.log("entry", entry);
                            entry_array.push(entry);
                        });

            console.log("entry_array =>", entry_array);

        }
        

        
        
        
    </script>
</body>
</html>