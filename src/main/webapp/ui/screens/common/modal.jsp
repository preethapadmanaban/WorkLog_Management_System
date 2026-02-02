<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="modal_outlier" id="modal_outlier">
    <div class="modal_container" id="modal">
        <div class="modal_header">
            <img class="modal_status_img" id="modal_header_success_img" src="/worklog/ui/images/verified.png" alt="Status_Image">

            <img class="modal_status_img" id="modal_header_error_img" src="/worklog/ui/images/close.png" alt="Status_Image">
        </div>
        <div class="modal_body">
            <p id="modal_body_text">This is a sample alert message for you!</p> 
        </div>
        <div class="modal_action">
            <button class="success_anchor" onclick="model_inner_object_reference.hideModal()">Ok</button>
            <button class="info_anchor" onclick="model_inner_object_reference.hideModal()">Cancel</button>
        </div>
    </div>
</div>

<script src="/worklog/ui/js/Modal.js"></script>
<script> const model_inner_object_reference = new Modal(); </script>