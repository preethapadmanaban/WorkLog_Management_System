class Modal{
	
    success(message){
        if(message == null){
            console.log("success message is null");
            return;
        }
        document.getElementById("modal_header_error_img").hidden = true;
        document.getElementById("modal_header_success_img").hidden = false;
        document.getElementById("modal_body_text").innerText = message;
        document.getElementById("modal_outlier").style.display = "flex";
    }

    error(message){
        if(message == null){
            console.log("error message is null");
            return;
        }
        document.getElementById("modal_header_success_img").hidden = true;
        document.getElementById("modal_header_error_img").hidden = false;
        document.getElementById("modal_body_text").innerText = message;
        document.getElementById("modal_outlier").style.display = "flex";
    }

    hideModal(){
        document.getElementById("modal_outlier").style.display = "none";
		window.location.reload();
    }
}

window.Modal = Modal;
