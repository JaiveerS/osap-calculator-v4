function validate(){
	var ok = true;
	console.log("Hello world!"); 
	var p= document.getElementById("principal").value;
	if(isNaN(p) || p <= 0) {
		alert("Principal invalid!");
		document.getElementById("principal-error").style.display = "inline";
		document.getElementById("principal-error").style.color = "red";
		ok = false;
	} else{
		document.getElementById("principal-error").style.display = "none";
	}
	
	if(!ok){
		return false;
	}
	
	p = document.getElementById("interest").value;
	if(isNaN(p) || p <= 0 || p >= 100) {
		alert("Invalid. Must be in (0,100).");
		document.getElementById("interest-error").style.display = "inline";
		document.getElementById("interest-error").style.color = "red";
		ok = false;
	} else{
		document.getElementById("interest-error").style.display = "none";
	}
	
	if(!ok){
		return false;
	}
	
	p = document.getElementById("period").value;
	if(isNaN(p) || p <= 0 ) {
		alert("Period invalid!");
		document.getElementById("period-error").style.display = "inline";
		document.getElementById("period-error").style.color = "red";
		ok = false;
	} else{
		document.getElementById("period-error").style.display = "none";
	}
	
	return ok;
}

function doSimpleAjax(address){
	var request = new XMLHttpRequest();
	var data='';
	
	//grabs all parameters from form
	data += "comm=ajax&";
	data += "principal=" + document.getElementById("principal").value + "&";
	data += "interest=" + document.getElementById("interest").value + "&";
	data += "period=" + document.getElementById("period").value + "&";
	data += "grace=" + document.getElementById("grace").checked + "&";
	data += "calculate=true";
	console.log(data);
	
	
	request.onreadystatechange = function(){
		handler(request)
	}
	
	request.open("GET", (address + "?" + data), true);
	request.send(null);
}

function handler(request){
	if((request.readyState == 4) && (request.status == 200)){
		var target = document.getElementById("ajaxTarget");
		target.innerHTML = request.responseText;
	}else{
		var target = document.getElementById("ajaxTarget");
		target.innerHTML = "<STRONG> Waiting for response</STRONG>";
	}
}
