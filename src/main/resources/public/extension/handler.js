TimeMe.initialize({

    currentPageName: "my-home-page", // current page

    // the timer will stop if there is no action after this ammount of seconds (30s good fir demo)
    idleTimeoutInSeconds: 30 // seconds
});


TimeMe.startTimer("my-activity");

var data = {};
var blackListedArticles = ["*scrum of scrum*", "*tech alignment*"];


// this is fired for closing the tab or window
window.addEventListener('unload', function(event) {

    data ["url"] = document.URL;

    if (collectAuthorAndUser()){

		var content = document.getElementById("content-core");

        collectComments();
        var url;
    	if (validateContent(content)) {
    		url = "http://localhost:8585/articleReadAction";
    	}
    	else {
            url = "http://localhost:8585/blackListedArticle";
        }
        callAjax(url, data);
    }

});

function callAjax(url, data){
    TimeMe.stopTimer("my-activity");
    var timeOnActivity = TimeMe.getTimeOnPageInSeconds("my-activity");
    data ["spentTimeInSeconds"] = timeOnActivity;

    var xmlhttp= new XMLHttpRequest();
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type", "application/json");
    xmlhttp.send(JSON.stringify(data));
}

function validateContent(content) {
    var title = content.getElementsByClassName("page-title")[0].textContent.toLowerCase();
    var pageContent = content.getElementsByClassName("entry-content")[0].textContent;
    data["articleTitle"] = title;
    analyzeContent(pageContent);
    for(var blackListedElement in blackListedArticles){
        var regex = new RegExp(blackListedElement);
        if(regex.test(title)){
            return false;
        }
    }
    return true;
}

function analyzeContent(pageContent){
    var charLength = pageContent.length;
    var lines = pageContent.split("\n").length;
    var words = pageContent.split(/\b\S+\b/g).length;
    var stats = [charLength, words, lines];
    data["nrOfLines"] = lines;
}

function collectAuthorAndUser(){

    var author = null;
    var user = null;
    var authorname = null;
    var username = null;
	var collected = false;

    var authors = document.querySelectorAll("a[rel*='author']");
	
	//collect author only from a post [age and not the main page
    if (authors != null && authors.length == 1) {
		
		//extract author user from link
    	author = authors[0].href.substr(42);
    	author = author.substr(0,author.length-1);
    	data ["author"] = author;

    	user = document.querySelector("span.username").textContent;
    	authorname = document.querySelector("a[rel*='author']").textContent;
    	username = document.querySelector("span.display-name").textContent;
		
		//do not collect data if the logged in user is the same as the autor
    	if (username != null && authorname != null && username != authorname) {
    		data ["authorName"] = authorname;
    		data ["user"] = user;
    		data ["username"] = username;
			collected = true;
		}
	}
	return collected;
}

function collectComments(){
	var comments = document.querySelectorAll("a.url");
	var commentsContent = document.querySelectorAll("div.comment-content");

	if (comments != null){
		data["comments number"] = comments.length;
		for (var i = 0; i < comments.length; i++){
			var commentAuthor = comments[i].textContent;
			var commentContent = commentsContent[i].textContent;
			var commentUser = comments[i].href.substr(34);
			//extract user from link
			commentUser = commentUser.substr(0,commentUser.length-1);
			data["commenter "+(i+1)] = [commentAuthor,commentUser,commentContent];
		}
	}
}


