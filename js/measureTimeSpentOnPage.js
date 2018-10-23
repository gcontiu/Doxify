  document.body.style.border = "5px solid red";

  var start = new Date();

  window.addEventListener('unload', function(event) {
        
        var end = new Date();
        var data = {};
        data ["spentTime"] = end - start;
        data ["url"] = document.URL;
        var url = "http://localhost:8585/user/timeSpentOnPage";
        callAjax(url, data);
		console.log( end - start);

  });

  function callAjax(url, data){
      var xmlhttp;
      xmlhttp = new XMLHttpRequest();
      xmlhttp.open("POST", url, true);
      xmlhttp.send(JSON.stringify(data));
  }

