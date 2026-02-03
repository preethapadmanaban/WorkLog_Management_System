<%@page import="com.worklog.dto.TimeSheetEntryForReviewDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.worklog.entities.TimeSheet" %>
<%@ page import="com.worklog.entities.TimeSheetEntry" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Review</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/ui/css/styles.css">
<jsp:include page="/ui/screens/common/app_logo.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/ui/screens/common/navbar.jsp"></jsp:include>


	<%
	
    TimeSheet ts = (TimeSheet) request.getAttribute("timesheet");
    List<TimeSheetEntryForReviewDTO> entries = (List<TimeSheetEntryForReviewDTO>) request.getAttribute("entries");
    
	%>
	
	<h2>TimeSheet review</h2>
	
	<%
		if(ts == null){
	%>
	
		<p>Sorry! Time sheet not found</p>
	
	<%
    	} else {
	%>
	
	<div class="container_70">
	<h3>Timesheet Details</h3>
	<div class="timesheet_review_detail_with_image">
	<table class="table table-striped table-hover border border-secondary">
		    <tr><th>Employee Id</th><td><%= ts.getEmployee_id() %></td></tr>
		    <tr><th>Work Date</th><td><%= ts.getWork_date() %></td></tr>
		    <tr><th>Total Hours</th><td><%= ts.getTotal_hours() %> hours</td></tr>
		    <% String comment = ts.getManager_comment() == null ? "No Comment" : ts.getManager_comment();%>
		    <tr><th>Manager Comment</th><td><%=comment%></td></tr>
		    <tr><th>Status</th><td><%= ts.getStatus().toString()%></td></tr>
		    <tr><th>Submitted Date</th><td><%=ts.getCreated_at()%></td></tr>
	</table>
	
	
	<%
	if(((String)session.getAttribute("role")).equalsIgnoreCase("employee") == true)
	{
	if(ts.getStatus().equalsIgnoreCase("approved") == true)
	{ 
	%>
	<div class="timesheet_status_img_div" id="timesheet_approved_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_approved.png">
	</div>
	<%
	}
	else if(ts.getStatus().equalsIgnoreCase("rejected") == true)
	{
	%>
	<div class="timesheet_status_img_div" id="timesheet_rejected_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_rejected.png">
	</div>
	<%
	}
	else
	{
	%>
	<div class="timesheet_status_img_div" id="timesheet_pending_img">
		<img class="timesheet_status_img" alt="Approved" src="<%=request.getContextPath()%>/ui/icons/timesheet_pending.png">
	</div>
	<%} 
	}
	%>
	
	</div>
	<br>
	
	
	
	<!-- Entries Table -->
	<h3>Entries</h3>
	
	<table class="table table-striped table-hover border border-secondary">
	    <tr>
	        <!-- <th>Entry ID</th> -->
	        <th>Task Title</th>
	        <th>Notes</th>
	        <th>Hours Spent</th>
	    </tr>
	    
	    <%
    if(entries != null && !entries.isEmpty()){
        for(TimeSheetEntryForReviewDTO e : entries){
	%>
	    <tr>
	        <%-- <td><%= e.getId() %></td> --%>
	        <td><%= e.getTitle() %></td>
	        <td><%= e.getNotes() %></td>
	        <td class="action_button_array"><%= e.getHours_spent() %> hours</td>
	    </tr>
	<%
	        }
	    } else {
	%>
	    <tr>
	        <td colspan="4">No entries found</td>
	    </tr>
	<%
	    }
	%>
	
	</table>
	
	
	
	<% 
		if(((String)session.getAttribute("role")).equalsIgnoreCase("manager"))
		{
	%>
	<h3>Manager Action</h3>

	<form id="managerActionForm" action="<%=request.getContextPath()%>/controller" method="post">
	    <input type="hidden" name="timesheetId" value="<%= ts.getId() %>">
	    <input type="hidden" name="action" id="actionField" value="">
	
	    <div class="nice-form-group">
	      <label>Comment <span class="required">*</span></label>
	      <textarea id="manager_comment" name="manager_comment" rows="4" cols="50"></textarea>
	      <small class="error-text" id="commentError"></small>
	    </div>
		
		<button class="submit_button" type="button" id="approveBtn">Approve</button>
		<button class="reject_button" type="button" id="rejectBtn">Reject</button>

	</form>

	<br>
	
	<br>
	<a class="info_anchor" href="<%=request.getContextPath() %>/controller?action=pending">Back to Pending List</a>
	
	<%
		}
	  }
	%>
	
	<% if(((String)session.getAttribute("role")).equalsIgnoreCase("employee") == true)
	{
	%>
	<div style="margin-top: 20px;">
		<a class="info_anchor" href="/worklog/controller?action=timesheetHistory">Back to Timesheet History</a>
	</div>
	<% 
	}
	%>
	</div>
	
	<div id="modalOverlay" class="modal-overlay" style="display:none;">
	  <div class="modal-box" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
	    <h4 id="modalTitle" class="modal-title">Message</h4>
	    <p id="modalText" class="modal-text"></p>
	    <div class="modal-actions">
	      <button type="button" class="modal-cancel" id="modalCancelBtn" style="display:none;">Cancel</button>
	      <button type="button" class="modal-ok" id="modalOkBtn">OK</button>
	    </div>
	  </div>
	</div>

	
<script>
  let onOk = null;
  let onCancel = null;

  function openPopup(msg, popupTitle="Message", type="info", options={}) {
    const box = document.querySelector(".modal-box");
    box.classList.remove("success", "error");

    if (type === "success") box.classList.add("success");
    if (type === "error") box.classList.add("error");

    document.getElementById("modalTitle").textContent = popupTitle;
    document.getElementById("modalText").textContent = msg;

    const okBtn = document.getElementById("modalOkBtn");
    const cancelBtn = document.getElementById("modalCancelBtn");

    onOk = options.onOk || null;
    onCancel = options.onCancel || null;

    okBtn.textContent = options.okText || "OK";

    if (options.showCancel) {
      cancelBtn.style.display = "inline-block";
      cancelBtn.textContent = options.cancelText || "Cancel";
    } else {
      cancelBtn.style.display = "none";
    }

    document.getElementById("modalOverlay").style.display = "flex";
  }

  function closePopup() {
    document.getElementById("modalOverlay").style.display = "none";
    onOk = null;
    onCancel = null;
  }

  document.addEventListener("DOMContentLoaded", () => {
    const okBtn = document.getElementById("modalOkBtn");
    const cancelBtn = document.getElementById("modalCancelBtn");

    okBtn.addEventListener("click", () => {
      const fn = onOk;
      closePopup();
      if (fn) fn();
    });

    cancelBtn.addEventListener("click", () => {
      const fn = onCancel;
      closePopup();
      if (fn) fn();
    });
  });

  function setError(field, errorId, msg){
    if(field) field.classList.add("input-error");
    const el = document.getElementById(errorId);
    if(el) el.textContent = msg;
  }

  function clearError(field, errorId){
    if(field) field.classList.remove("input-error");
    const el = document.getElementById(errorId);
    if(el) el.textContent = "";
  }

  async function submitManagerAction(actionValue) {
    const form = document.getElementById("managerActionForm");
    const comment = document.getElementById("manager_comment");
    const actionField = document.getElementById("actionField");

    clearError(comment, "commentError");

    const val = comment.value.trim();
    if (!val) {
      setError(comment, "commentError", "Please enter your comment.");
      openPopup(
        "Comment is required to proceed. Please provide clear feedback for the employee.",
        "Comment required",
        "error"
      );
      comment.focus();
      return;
    }

    actionField.value = actionValue;

    // build form payload
    const payload = new URLSearchParams(new FormData(form));

    try {
      const res = await fetch("<%=request.getContextPath()%>/controller", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          "X-Requested-With": "fetch"
        },
        body: payload.toString()
      });

      const data = await res.json();

      if (data.success) {
        // Success popup then redirect to pending list
        const successMsg = (actionValue === "approveTimesheet")
          ? "Timesheet has been approved successfully."
          : "Timesheet has been rejected successfully.";

        openPopup(successMsg, "Success ✅", "success", {
          onOk: () => {
            window.location.href = "<%=request.getContextPath()%>/controller?action=pending";
          }
        });

      } else {
        openPopup(data.message || "Action failed.", "Error ❌", "error");
      }

    } catch (e) {
      console.error(e);
      openPopup("Server error. Please try again.", "Error ❌", "error");
    }
  }

  document.addEventListener("DOMContentLoaded", () => {
    const approveBtn = document.getElementById("approveBtn");
    const rejectBtn = document.getElementById("rejectBtn");

    if (approveBtn) {
      approveBtn.addEventListener("click", () => {
        submitManagerAction("approveTimesheet");
      });
    }

    if (rejectBtn) {
      rejectBtn.addEventListener("click", () => {
        openPopup(
          "Are you sure you want to reject this timesheet?",
          "Confirm Rejection",
          "info",
          {
            showCancel: true,
            okText: "OK",
            cancelText: "Cancel",
            onOk: () => submitManagerAction("rejectTimesheet"),
            onCancel: () => {} // do nothing
          }
        );
      });
    }
  });
</script>

	
</body>
</html>