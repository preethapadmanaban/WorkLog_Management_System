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

    <jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>
   
   
    <div class="container">
   		<div><h3>New Timesheet</h3>
	        <div class="row">
	            <div class="col-6 col-sm-3 ">
	                   <span>Enter work date:</span> <input type="date" name="work_date" id="work_date" class="form-control"> </th>
	                    <!-- <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" onclick="add_new_entry_row()" class="size-6 align-self-center add_timesheet_icon">
	                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
	                        </svg> -->
	            </div>
	        </div>
        </div>
        <table class="table">
            <thead>
                <tr class="align-items-center">
                    <th>Task <span style="color:red;">*</span> </th>
                    <th>Total hours spend <span style="color:red;">*</span> </th>
					<th>Comments</th>
                    <th colspan="2" class="text-center">Action</th>
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
    
 		// convert the task json object => javascript object directly without any external fun.
	    const tasks = ${tasks};
	    //console.log("tasks =>", tasks);
	    
	 	// function that sets the today as default in the date select menu.
	    document.addEventListener('DOMContentLoaded', ()=>{
			document.getElementById("work_date").value = new Date(Date.now()).toISOString().split("T")[0];
            //console.log("date set...");
        });

	    // function that add new timesheet entry row in the table.
        function add_new_entry_row(){

            const tbody = document.getElementById("entry_table_body");
            const row = tbody.insertRow(-1); // -1 = append the row at the end of the table.
            
            row.classList.add("entry_row");

            let cell = document.createElement("td"); // => here we create table cell.

            // tasks selecting tag starts
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
            // tasks selecting tag ends

            // total hours input starts
            cell = document.createElement("td");

            let total_hours_input = document.createElement("input");
            total_hours_input.setAttribute("type", "number"); 
            total_hours_input.setAttribute("id", "hours_spend"); 
            total_hours_input.setAttribute("required", "true");
            total_hours_input.setAttribute("class", "form-control");

            cell.appendChild(total_hours_input);

            row.appendChild(cell);
         	// total hours input ends
            
         	// notes input starts
            cell = document.createElement("td");
            
            let notes = document.createElement("input");
            notes.setAttribute("type", "text");
            notes.classList.add("form-control");
            
            cell.appendChild(notes);

            row.appendChild(cell);
         	// notes input starts

         	// action button's starts
            cell = document.createElement("td");

         	// edit button
            /* let action_button = document.createElement("button");
            action_button.innerText = "edit";
            action_button.setAttribute("class", "btn btn-secondary");

            cell.appendChild(action_button);
            
            row.appendChild(cell); */

            // delete row button
            cell = document.createElement("td");
            cell.classList.add("text-center");
            action_button = document.createElement("button");
            action_button.innerText = "delete";
            action_button.setAttribute("class", "btn btn-danger");
            action_button.setAttribute("onclick", "delete_entry_row(event)");
            cell.appendChild(action_button);
            
            row.appendChild(cell);
            // action button ends here
        }

        function delete_entry_row(e){
            e.target.parentNode.parentNode.remove();
        }
        
        // function that collects the timsheet entry data and validates it.
        function collect_data(){
			let work_date = document.getElementById("work_date").value;
            if(work_date == null || work_date == "")
            {
                alert("Please enter work date to continue further!");
                return;
            }   
            
            // collect the timesheet entry data in an object;
            function Entry(id, hours_spend, notes){
                this.id = id;
                this.hours_spend = hours_spend;
                this.notes = notes;
            }
				
            // this object replicas out backend objects TimeSheetRequestDTO object(without managerId).
            function EntryRequestDTO(work_date, total_hours, entries){
                this.work_date = work_date;
                this.total_hours = total_hours;
                this.entries = entries;
            }

            let total_hours = 0;
            let temp = [];
            let entry_array = [];
            let entry_rows = document.querySelectorAll(".entry_row"); // -> <tr>
            let isValid = true;
            //console.log("entry_rows length=> ", entry_rows.length);
            entry_rows.forEach((row)=>{
                let row_cells = row.childNodes; // => [td, td, td, td]
                for(let i=0; i<3; i++)
                { 
                    // td elements traversa
                    let cells = row_cells[i].childNodes;
                    for(let j=0; j<1; j++)
                    { // inside td traversal
                        let element = cells[j];
                        temp[i] = element.value;
                        if(i == 2){ // i = 2, 0-task, 1-hours_spend, 2-notes.
							break; // here we skips validation for notes, its optional.
                        }
                        if(temp[i] == null || temp[i].trim() === ""){
							isValid = false;
		                	console.log("no value validation failed!");
							break;
                        }
                    }
                }
                // hours_spend validation must be <=10
                const hours_spend = parseFloat(temp[1]);
                if(hours_spend < 0.0 || hours_spend > 10.00)
                {
		
                	isValid = false;
                	console.log("hours_spend validation failed!, value=>", hours_spend);
                	return;
                }   
                const newEntry = new Entry(parseInt(temp[0]), hours_spend, temp[2]);
                // calculating total hours
                total_hours = total_hours + parseFloat(temp[1]);
                entry_array.push(newEntry);
            });
            
            if(isValid == false)
           	{
				alert("please enter all details and valid ones.");
				return;
           	}

            //console.log("entry_array =>", entry_array);
            const requestDto = new EntryRequestDTO(work_date, total_hours, entry_array);
            //console.log("entry request dto: ", requestDto);
            if(window.confirm("Confirm to submit timesheet?")){
                alert("Validation successfull");
            }
            else{
				return;
            }
        }
   
    </script>
</body>
</html>