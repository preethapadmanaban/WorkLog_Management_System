/*window.openPopup = function (msg, popupTitle="Message", type="info"){
	  const box = document.querySelector(".modal-box");
	  box.classList.remove("success", "error");

	  if(type === "success") {
		box.classList.add("success");
		popupTitle = popupTitle === "Message" ? "Success ✅" : popupTitle;
	}
	  if(type === "error") {
		box.classList.add("error");
		popupTitle = popupTitle === "Message" ? "Error ❌" : popupTitle;
	}

	  document.getElementById("modalTitle").textContent = popupTitle;
	  document.getElementById("modalText").textContent = msg;
	  document.getElementById("modalOverlay").style.display = "flex";
}*/

window.closePopup = function() {
	  document.getElementById("modalOverlay").style.display = "none";
	  /*window.location.reload();*/
}

window.openPopup = function(msg, popupTitle="Message", type="info", options={}) {
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