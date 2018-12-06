TimeMe.initialize({

    currentPageName: "my-home-page", // current page

    // the timer will stop if there is no action after this ammount of seconds (30s good fir demo)
    idleTimeoutInSeconds: 30 // seconds
});


TimeMe.startTimer("my-activity");

var data = new Object();
var blackListedArticles = ["*scrum of scrum*", "*tech alignment*"];


// this is fired for closing the tab or window
window.addEventListener('unload', function(event) {

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
    data ["timeSpentInSeconds"] = timeOnActivity;
    var xmlHttp= new XMLHttpRequest();
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type", "application/json");
    xmlHttp.send(JSON.stringify(data));
}

function validateContent(content) {
    var title = content.getElementsByClassName("page-title")[0].textContent;
    var lowerCaseTitle = title.toLowerCase();
    var pageContent = content.getElementsByClassName("entry-content")[0].textContent;
    data["title"] = title;
    analyzeContent(pageContent);
    for(var blackListedElement in blackListedArticles){
        var regex = new RegExp(blackListedElement);
        if(regex.test(lowerCaseTitle)){
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
    var authorName = null;
    var username = null;
	var collected = false;

    var authors = document.querySelectorAll("a[rel*='author']");
	
	//collect author only from a post [age and not the main page
    if (authors != null && authors.length == 1) {
		
		//extract author user from link
    	author = authors[0].href.substr(42);
    	author = author.substr(0,author.length-1);
    	
    	var authorDTO = new Object();

    	user = document.querySelector("span.username").textContent;
    	authorName = document.querySelector("a[rel*='author']").textContent;
    	username = document.querySelector("span.display-name").textContent;

        data["url"] = document.URL;
		
		//do not collect data if the logged in user is the same as the autor
    	if (username != null && authorName != null && username != authorName) {
            authorDTO ["username"] = author;
            authorDTO ["fullName"] = authorName;
            data ["author"] = authorDTO;
			collected = true;
		}
	}
	return collected;
}

function collectComments(){
	var comments = document.querySelectorAll("a.url");
	var commentsContent = document.querySelectorAll("div.comment-content");
    var commentList = new Array();

	if (comments != null){
		for (var i = 0; i < comments.length; i++){
			var commentAuthor = comments[i].textContent;
			var commentContent = commentsContent[i].textContent;
			var commentUser = comments[i].href.substr(34);
			//extract user from link
			commentUser = commentUser.substr(0,commentUser.length-1);

			var commenter = new Object();
			commenter["author"] = commentAuthor;
            commenter["user"] = commentUser;
            commenter["hash"] = getHashCode(commentContent);

            commentList.push(commenter);
		}

		data ["commentList"] = commentList;
	}
}

// mimics the String hashCode function
function getHashCode(text) {
	var hash = 0;
	if (Array.prototype.reduce) {
        hash = text.split("").reduce(
			function(a,b){
				a = ((a<<5)-a) + b.charCodeAt(0);
				return a & a
			},0);
    } else {
		// for old browsers
		if (text.length == 0) return hash;
		for (i = 0; i < text.length; i++) {
			var code = text.charCodeAt(i);
			hash  = ((hash << 5) - hash) + code;
			hash = hash & hash; // Convert to 32bit integer
		}
	}
	return Math.abs(hash);
}