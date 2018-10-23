  document.body.style.border = "5px solid red";

  var start = new Date();

  window.addEventListener('unload', function(event) {
        
        var end = new Date();
        var data = end - start;
        var url = "http://localhost:8585/user/timeSpentOnPage";
        callAjax(url, data, consolLog);
		console.log( end - start);

  });

  function callAjax(url, data, callback){
    var xmlhttp;
    var formData = new FormData();
    formData.append("spentTime", data);
    // compatible with IE7+, Firefox, Chrome, Opera, Safari
    xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function(){
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200){
            callback(xmlhttp.responseText);
        }
    }
    xmlhttp.open("POST", url, true);
    //xmlhttp.setRequestHeader("crossdomain","true");
    xmlhttp.send(formData);
}

function consolLog(param){
	console.log(param);
}
