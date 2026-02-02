window.openPopup = function (msg, popupTitle="Message", type="info"){
	  const box = document.querySelector(".modal-box");
	  box.classList.remove("success", "error");

	  if(type === "success") box.classList.add("success");
	  if(type === "error") box.classList.add("error");

	  document.getElementById("modalTitle").textContent = popupTitle;
	  document.getElementById("modalText").textContent = msg;
	  document.getElementById("modalOverlay").style.display = "flex";
}

window.closePopup = function() {
	  document.getElementById("modalOverlay").style.display = "none";
	  window.location.reload();
}