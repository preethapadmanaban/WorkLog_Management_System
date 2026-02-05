<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.worklog.entities.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.Employee" %>
<%@ page import="java.time.LocalDate" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Edit Task</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>

</head>

<body>

<jsp:include page="/ui/screens/common/navbar.jsp"/>
<jsp:include page="/ui/screens/common/message.jsp"/>
<jsp:include page="/ui/screens/common/modal.jsp"></jsp:include>

<%
    // ---- Safe session & role handling ----
    String role = (session != null) ? (String) session.getAttribute("role") : null;
    boolean isEmployee = role != null && role.equalsIgnoreCase("employee");

    // ---- Get task from request ----
    Task t = (Task) request.getAttribute("task");
    
    boolean isManager = role != null && role.equalsIgnoreCase("manager");

    Boolean managerCanEditObj = (Boolean) request.getAttribute("managerCanEdit");
    boolean managerCanEdit = (managerCanEditObj != null) ? managerCanEditObj : false;

    // Manager can edit ONLY when status is ASSIGNED (your rule)
    boolean canEdit = isManager && managerCanEdit;
%>

<% if (t == null) { %>

    <p style="color:red;">No task found.</p>

<% } else { %>

<section class="nice-form">
    <h3>Update Task</h3>
    <p>Update the task.</p>

    <% if (isEmployee) { %>
        <p><strong>Note:</strong> You can change only the status.</p>
    <% } %>
    
  
    <form id="editTaskForm" action="<%= request.getContextPath() %>/controller" method="post" onsubmit="event.preventDefault(); updateTask();">

        <input type="hidden" name="action" value="updateTask">
        <input type="hidden" name="id" value="<%= t.getId() %>">

        <div class="nice-form-group">
            <label>Title</label>
            <input class="nice-form-input"
                   type="text"
                   name="title"
                   value="<%= t.getTitle() %>"
                   <%= canEdit ? "" : "readonly" %>>
        </div>

        <div class="nice-form-group">
            <label>Description</label>
            <input class="nice-form-input"
                   type="text"
                   name="description"
                   value="<%= t.getDescription() %>"
                   <%= canEdit ? "" : "readonly" %>>
        </div>

        <% if (isEmployee) { %>
		    <div class="nice-form-group">
		        <label>Status</label>
		        <%
		            String statusVal = (t.getStatus() == null) ? "" : t.getStatus().name();
		        %>
		        <select name="status" class="nice-form-input">
		            <option value="ASSIGNED"   <%= "ASSIGNED".equals(statusVal) ? "selected" : "" %>>Assigned</option>
		            <option value="IN_PROGRESS" <%= "IN_PROGRESS".equals(statusVal) ? "selected" : "" %>>In Progress</option>
		            <option value="COMPLETED"  <%= "COMPLETED".equals(statusVal) ? "selected" : "" %>>Completed</option>
		        </select>
		    </div>
		<% } else { %>
		    <!-- Manager doesn't see status -->
		    <input type="hidden" name="status" value="<%= t.getStatus() == null ? "" : t.getStatus().name() %>">
		<% } %>
		
        <div class="nice-form-group">
		    <label>Employee Name</label>
		
		    <% if (canEdit) {  %>
		
		        <%
		            List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
		        %>
		
		        <select name="assigned_to" class="nice-form-input">
		            <% if (employeeList != null) {
		                for (Employee e : employeeList) { %>
		                    <option value="<%= e.getId() %>"
		                        <%= (e.getId() == t.getAssigned_to()) ? "selected" : "" %>>
		                        <%= e.getName() %>
		                    </option>
		            <%  } } %>
		        </select>
		
		    <% } else { %>
		
		        <input class="nice-form-input" type="text"
		               value="<%= (String)request.getAttribute("assignedToName") %>" readonly>
		        <input type="hidden" name="assigned_to" value="<%= t.getAssigned_to() %>">
		
		    <% } %>
		</div>

      <%
		String deadlineForDateInput = "";
		if (t.getDeadline() != null && !t.getDeadline().isBlank()) {
		    String d = t.getDeadline().trim();
		    if (d.contains(" ")) d = d.split(" ")[0];
		
		    // dd/MM/yyyy -> yyyy-MM-dd
		    if (d.matches("\\d{2}/\\d{2}/\\d{4}")) {
		        String[] p = d.split("/");
		        deadlineForDateInput = p[2] + "-" + p[1] + "-" + p[0];
		    }
		    // dd-MM-yyyy -> yyyy-MM-dd
		    else if (d.matches("\\d{2}-\\d{2}-\\d{4}")) {
		        String[] p = d.split("-");
		        deadlineForDateInput = p[2] + "-" + p[1] + "-" + p[0];
		    }
		    // already yyyy-MM-dd
		    else {
		        deadlineForDateInput = d;
		    }
		}
	  %>
		
		<%
		    String todayStr = LocalDate.now().toString(); // yyyy-MM-dd
		%>
		
		<div class="nice-form-group">
		  <label>Deadline</label>
		  <input class="nice-form-input"
		         type="date"
		         name="deadline"
		         value="<%= deadlineForDateInput %>"
		         min="<%= todayStr %>"
		         <%= canEdit ? "" : "disabled" %>>
		  <% if (!canEdit) { %>
		      <input type="hidden" name="deadline" value="<%= deadlineForDateInput %>">
		  <% } %>
		</div>

        <div style="margin-top: 16px;">
		    <% if (canEdit) { %> 
				<button type="submit" class="button button-primary" id="submitBtn" disabled>Submit</button>
		    <% } else { %>
		        <p style="color:#dc3545; font-weight:600;">
		            You can't edit this task because work is already started (Status: <%= t.getStatus() %>).
		        </p>
		    <% } %>
		
		    <button type="button" class="cancel_button"
		            onclick="window.location.href='<%=request.getContextPath()%>/controller?action=listTasks'">
		        Cancel
		    </button>
		</div>

    </form>
</section>

<% } %>
<script src="${pageContext.request.contextPath}/ui/js/Modal.js"></script>

<script>
const base = "<%= request.getContextPath() %>";
let redirectUrlAfterOk = null;

function openPopup(msg, popupTitle="Message", type="info", redirectUrl=null){
  redirectUrlAfterOk = redirectUrl;

  const box = document.querySelector(".modal-box");
  box.classList.remove("success", "error");

  if(type === "success") box.classList.add("success");
  if(type === "error") box.classList.add("error");

  document.getElementById("modalTitle").textContent = popupTitle;
  document.getElementById("modalText").textContent = msg;
  document.getElementById("modalOverlay").style.display = "flex";
}

function closePopup() {
  document.getElementById("modalOverlay").style.display = "none";
  if (redirectUrlAfterOk) window.location.href = redirectUrlAfterOk;
}

document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("editTaskForm");
  const submitBtn = document.getElementById("submitBtn");
  if (!form || !submitBtn) return;

  const fields = Array.from(form.querySelectorAll("input, select, textarea"))
                      .filter(f => f.name && !f.disabled);

  const initial = {};
  fields.forEach(f => initial[f.name] = f.value);

  function checkChanged() {
    let changed = false;
    for (const f of fields) {
      if (initial[f.name] !== f.value) {
        changed = true;
        break;
      }
    }
    submitBtn.disabled = !changed;
  }

  form.addEventListener("input", checkChanged);
  form.addEventListener("change", checkChanged);
  checkChanged();
});

async function updateTask(){
  const form = document.getElementById("editTaskForm");
  const submitBtn = document.getElementById("submitBtn");
  const payload = new URLSearchParams(new FormData(form));

  try {
    const res = await fetch(base + "/controller", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "X-Requested-With": "fetch"
      },
      body: payload.toString()
    });

    const text = await res.text();

    if (text.includes('"success":true') || text.toLowerCase().includes("task updated successfully")) {
      openPopup("Task updated successfully.", "Success ✅", "success",
        base + "/controller?action=listTasks"
      );
      if(submitBtn) submitBtn.disabled = true;
      return;
    }

    openPopup("Task updation failed.", "Error ❌", "error");

  } catch (e) {
    console.error(e);
    openPopup("Server error. Please try again.", "Error ❌", "error");
  }
}
</script>

</body>
</html>
