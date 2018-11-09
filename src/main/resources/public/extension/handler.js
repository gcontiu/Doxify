
document.body.style.border = "5px solid blue";

var start = new Date();

TimeMe.initialize({

    currentPageName: "my-home-page", // current page

    // the timer will stop if there is no action after this ammount of seconds (5s good fir demo)
    idleTimeoutInSeconds: 5 // seconds
});


TimeMe.startTimer("my-activity");

var data = {};
var blackListedArticles = ["*scrum of scrum*", "*tech alignment*"];


// this is fired for closing the tab or window
window.addEventListener('unload', function(event) {
    console.log("Our UNLOAD event listener fired!")
    var end = new Date();

    data ["spentTime"] = end - start;
    data ["url"] = document.URL;

    if (collectAuthorAndUser()){

		var content = null;
       	content = document.getElementById("content-core");

    	if (validateContent(content)) {

			collectComments();

    		var url = "http://localhost:8585/user/timeSpentOnPage";
    		callAjax(url, data);
    	}
    }

    console.log("basic Measurement: " + ((end - start)/1000) + " s");
});

function callAjax(url, data){
    TimeMe.stopTimer("my-activity");
    var timeOnActivity = TimeMe.getTimeOnPageInSeconds("my-activity")
    console.log("TIMEME.JS: "+ timeOnActivity + " s | Title: " + data["title"] + " | Author: " + data["author"] );

    var xmlhttp;
    xmlhttp = new XMLHttpRequest();
    xmlhttp.open("POST", url, true);
    xmlhttp.send(JSON.stringify(data));
}

function validateContent(content) {
    var title = content.getElementsByClassName("page-title")[0].textContent.toLowerCase();
    for(var blackListedElement in blackListedArticles){
        var regex = new RegExp(blackListedElement);
        if(regex.test(title)){
            console.log("The '" + title + "' is a recurring article.");
            return false;
        }
    }
    var pageContent = content.getElementsByClassName("entry-content")[0].textContent;
    data["title"] = title;
    analyzeContent(pageContent);
    return true;
}

function analyzeContent(pageContent){
    var charLength = pageContent.length;
    var lines = pageContent.split("\n").length;
    var words = pageContent.split(/\b\S+\b/g).length;
    var stats = [charLength, words, lines];
    data["stats"] = stats;
}

function collectAuthorAndUser(){

    var author = null;
    var user = null;
    var authorname = null;
    var username = null;
	var collected = false;

    var authors = document.querySelectorAll("a[rel*='author']");
    if (authors != null && authors.length == 1) {
    	author = authors[0].href.substr(42);
    	author = author.substr(0,author.length-1);
    	data ["author"] = author;

    	user = document.querySelector("span.username").textContent;
    	authorname = document.querySelector("a[rel*='author']").textContent;
    	username = document.querySelector("span.display-name").textContent;

    	if (username != null && authorname != null && username != authorname) {
    		data ["authorname"] = authorname;
    		data ["user"] = user;
    		data ["username"] = username;
			collected = true;
		}
	}
	return collected;
}

function collectComments(){
	var comments = document.querySelectorAll("a.url");

	if (comments != null){
		data["comments number"] = comments.length;
		for (var i=0; i< comments.length; i++){
			var comentauthor = comments[i].textContent;
			var commentuser = comments[i].href.substr(34);
			commentuser = commentuser.substr(0,commentuser.length-1);
			data["commenter "+(i+1)] = [comentauthor,commentuser];
		}
	}
}


