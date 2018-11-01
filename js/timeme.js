/*Copyright (c) 2017 Jason Zissman
 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in
 the Software without restriction, including without limitation the rights to
 use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 the Software, and to permit persons to whom the Software is furnished to do so,
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

(function () {
    (function (root, factory) {
        if (typeof module !== 'undefined' && module.exports) {
            // CommonJS
            return module.exports = factory();
        } else if (typeof define === 'function' && define.amd) {
            // AMD
            define([], function () {
                return (root.TimeMe = factory());
            });
        } else {
            // Global Variables
            return root.TimeMe = factory();
        }
    })(this, function () {

        var TimeMe = {

            startStopTimes: {},
            idleTimeoutMs: 30 * 1000,
            currentIdleTimeMs: 0,
            checkStateRateMs: 250,
            active: false,
            idle: false,
            currentPageName: "default-page-name",
            timeElapsedCallbacks: [],
            userLeftCallbacks: [],
            userReturnCallbacks: [],

            trackTimeOnElement: function (elementId) {
                var element = document.getElementById(elementId);
                if (element) {
                    element.addEventListener("mouseover", function () {
                        TimeMe.startTimer(elementId);
                    });
                    element.addEventListener("mousemove", function () {
                        TimeMe.startTimer(elementId);
                    });
                    element.addEventListener("mouseleave", function () {
                        TimeMe.stopTimer(elementId);
                    });
                    element.addEventListener("keypress", function () {
                        TimeMe.startTimer(elementId);
                    });
                    element.addEventListener("focus", function () {
                        TimeMe.startTimer(elementId);
                    });
                }
            },

            getTimeOnElementInSeconds: function (elementId) {
                var time = TimeMe.getTimeOnPageInSeconds(elementId);
                if (time) {
                    return time;
                } else {
                    return 0;
                }
            },

            startTimer: function (pageName) {
                if (!pageName) {
                    pageName = TimeMe.currentPageName;
                }

                if (TimeMe.startStopTimes[pageName] === undefined) {
                    TimeMe.startStopTimes[pageName] = [];
                } else {
                    var arrayOfTimes = TimeMe.startStopTimes[pageName];
                    var latestStartStopEntry = arrayOfTimes[arrayOfTimes.length - 1];
                    if (latestStartStopEntry !== undefined && latestStartStopEntry.stopTime === undefined) {
                        // Can't start new timer until previous finishes.
                        return;
                    }
                }
                TimeMe.startStopTimes[pageName].push({
                    "startTime": new Date(),
                    "stopTime": undefined
                });
                TimeMe.active = true;
            },

            stopAllTimers: function () {
                var pageNames = Object.keys(TimeMe.startStopTimes);
                for (var i = 0; i < pageNames.length; i++) {
                    TimeMe.stopTimer(pageNames[i]);
                }
            },

            stopTimer: function (pageName) {
                if (!pageName) {
                    pageName = TimeMe.currentPageName;
                }
                var arrayOfTimes = TimeMe.startStopTimes[pageName];
                if (arrayOfTimes === undefined || arrayOfTimes.length === 0) {
                    // Can't stop timer before you've started it.
                    return;
                }
                if (arrayOfTimes[arrayOfTimes.length - 1].stopTime === undefined) {
                    arrayOfTimes[arrayOfTimes.length - 1].stopTime = new Date();
                }
                TimeMe.active = false;
            },

            getTimeOnCurrentPageInSeconds: function () {
                return TimeMe.getTimeOnPageInSeconds(TimeMe.currentPageName);
            },

            getTimeOnPageInSeconds: function (pageName) {
                var timeInMs = TimeMe.getTimeOnPageInMilliseconds(pageName);
                if (timeInMs === undefined) {
                    return undefined;
                } else {
                    return TimeMe.getTimeOnPageInMilliseconds(pageName) / 1000;
                }
            },

            getTimeOnCurrentPageInMilliseconds: function () {
                return TimeMe.getTimeOnPageInMilliseconds(TimeMe.currentPageName);
            },

            getTimeOnPageInMilliseconds: function (pageName) {

                var totalTimeOnPage = 0;

                var arrayOfTimes = TimeMe.startStopTimes[pageName];
                if (arrayOfTimes === undefined) {
                    // Can't get time on page before you've started the timer.
                    return;
                }

                var timeSpentOnPageInSeconds = 0;
                for (var i = 0; i < arrayOfTimes.length; i++) {
                    var startTime = arrayOfTimes[i].startTime;
                    var stopTime = arrayOfTimes[i].stopTime;
                    if (stopTime === undefined) {
                        stopTime = new Date();
                    }
                    var difference = stopTime - startTime;
                    timeSpentOnPageInSeconds += (difference);
                }

                totalTimeOnPage = Number(timeSpentOnPageInSeconds);
                return totalTimeOnPage;
            },

            getTimeOnAllPagesInSeconds: function () {
                var allTimes = [];
                var pageNames = Object.keys(TimeMe.startStopTimes);
                for (var i = 0; i < pageNames.length; i++) {
                    var pageName = pageNames[i];
                    var timeOnPage = TimeMe.getTimeOnPageInSeconds(pageName);
                    allTimes.push({
                        "pageName": pageName,
                        "timeOnPage": timeOnPage
                    });
                }
                return allTimes;
            },

            setIdleDurationInSeconds: function (duration) {
                var durationFloat = parseFloat(duration);
                if (isNaN(durationFloat) === false) {
                    TimeMe.idleTimeoutMs = duration * 1000;
                } else {
                    throw {
                        name: "InvalidDurationException",
                        message: "An invalid duration time (" + duration + ") was provided."
                    };
                }
                return this;
            },

            setCurrentPageName: function (pageName) {
                TimeMe.currentPageName = pageName;
                return this;
            },

            resetRecordedPageTime: function (pageName) {
                delete TimeMe.startStopTimes[pageName];
            },

            resetAllRecordedPageTimes: function () {
                var pageNames = Object.keys(TimeMe.startStopTimes);
                for (var i = 0; i < pageNames.length; i++) {
                    TimeMe.resetRecordedPageTime(pageNames[i]);
                }
            },

            resetIdleCountdown: function () {
                if (TimeMe.idle) {
                    TimeMe.triggerUserHasReturned();
                }
                TimeMe.idle = false;
                TimeMe.currentIdleTimeMs = 0;
            },

            callWhenUserLeaves: function (callback, numberOfTimesToInvoke) {
                this.userLeftCallbacks.push({
                    callback: callback,
                    numberOfTimesToInvoke: numberOfTimesToInvoke
                })
            },

            callWhenUserReturns: function (callback, numberOfTimesToInvoke) {
                this.userReturnCallbacks.push({
                    callback: callback,
                    numberOfTimesToInvoke: numberOfTimesToInvoke
                })
            },

            triggerUserHasReturned: function () {
                if (!TimeMe.active) {
                    for (var i = 0; i < this.userReturnCallbacks.length; i++) {
                        var userReturnedCallback = this.userReturnCallbacks[i];
                        var numberTimes = userReturnedCallback.numberOfTimesToInvoke;
                        if (isNaN(numberTimes) || (numberTimes === undefined) || numberTimes > 0) {
                            userReturnedCallback.numberOfTimesToInvoke -= 1;
                            userReturnedCallback.callback();
                        }
                    }
                }
                TimeMe.startTimer();
            },

            triggerUserHasLeftPage: function () {
                if (TimeMe.active) {
                    for (var i = 0; i < this.userLeftCallbacks.length; i++) {
                        var userHasLeftCallback = this.userLeftCallbacks[i];
                        var numberTimes = userHasLeftCallback.numberOfTimesToInvoke;
                        if (isNaN(numberTimes) || (numberTimes === undefined) || numberTimes > 0) {
                            userHasLeftCallback.numberOfTimesToInvoke -= 1;
                            userHasLeftCallback.callback();
                        }
                    }
                }
                TimeMe.stopAllTimers();
            },

            callAfterTimeElapsedInSeconds: function (timeInSeconds, callback) {
                TimeMe.timeElapsedCallbacks.push({
                    timeInSeconds: timeInSeconds,
                    callback: callback,
                    pending: true
                });
            },

            checkState: function () {
                for (var i = 0; i < TimeMe.timeElapsedCallbacks.length; i++) {
                    if (TimeMe.timeElapsedCallbacks[i].pending && TimeMe.getTimeOnCurrentPageInSeconds() > TimeMe.timeElapsedCallbacks[i].timeInSeconds) {
                        TimeMe.timeElapsedCallbacks[i].callback();
                        TimeMe.timeElapsedCallbacks[i].pending = false;
                    }
                }

                if (TimeMe.idle === false && TimeMe.currentIdleTimeMs > TimeMe.idleTimeoutMs) {
                    TimeMe.idle = true;
                    TimeMe.triggerUserHasLeftPage();
                } else {
                    TimeMe.currentIdleTimeMs += TimeMe.checkStateRateMs;
                }
            },

            visibilityChangeEventName: undefined,
            hiddenPropName: undefined,

            listenForVisibilityEvents: function () {

                if (typeof document.hidden !== "undefined") {
                    TimeMe.hiddenPropName = "hidden";
                    TimeMe.visibilityChangeEventName = "visibilitychange";
                } else if (typeof document.mozHidden !== "undefined") {
                    TimeMe.hiddenPropName = "mozHidden";
                    TimeMe.visibilityChangeEventName = "mozvisibilitychange";
                } else if (typeof document.msHidden !== "undefined") {
                    TimeMe.hiddenPropName = "msHidden";
                    TimeMe.visibilityChangeEventName = "msvisibilitychange";
                } else if (typeof document.webkitHidden !== "undefined") {
                    TimeMe.hiddenPropName = "webkitHidden";
                    TimeMe.visibilityChangeEventName = "webkitvisibilitychange";
                }

                document.addEventListener(TimeMe.visibilityChangeEventName, function () {
                    if (document[TimeMe.hiddenPropName]) {
                        TimeMe.triggerUserHasLeftPage();
                    } else {
                        TimeMe.triggerUserHasReturned();
                    }
                }, false);

                window.addEventListener('blur', function () {
                    TimeMe.triggerUserHasLeftPage();
                });

                window.addEventListener('focus', function () {
                    TimeMe.triggerUserHasReturned();
                });

                document.addEventListener("mousemove", function () { TimeMe.resetIdleCountdown(); });
                document.addEventListener("keyup", function () { TimeMe.resetIdleCountdown(); });
                document.addEventListener("touchstart", function () { TimeMe.resetIdleCountdown(); });
                window.addEventListener("scroll", function () { TimeMe.resetIdleCountdown(); });

                setInterval(function () {
                    TimeMe.checkState();
                }, TimeMe.checkStateRateMs);
            },

            websocket: undefined,

            websocketHost: undefined,

            setUpWebsocket: function (websocketOptions) {
                if (window.WebSocket && websocketOptions) {
                    var websocketHost = websocketOptions.websocketHost; // "ws://hostname:port"
                    try {
                        TimeMe.websocket = new WebSocket(websocketHost);
                        window.onbeforeunload = function (event) {
                            TimeMe.sendCurrentTime(websocketOptions.appId);
                        };
                        TimeMe.websocket.onopen = function () {
                            TimeMe.sendInitWsRequest(websocketOptions.appId);
                        }
                        TimeMe.websocket.onerror = function (error) {
                            if (console) {
                                console.log("Error occurred in websocket connection: " + error);
                            }
                        }
                        TimeMe.websocket.onmessage = function (event) {
                            if (console) {
                                console.log(event.data);
                            }
                        }
                    } catch (error) {
                        if (console) {
                            console.error("Failed to connect to websocket host.  Error:" + error);
                        }
                    }
                }
                return this;
            },

            websocketSend: function (data) {
                TimeMe.websocket.send(JSON.stringify(data));
            },

            sendCurrentTime: function (appId) {
                var timeSpentOnPage = TimeMe.getTimeOnCurrentPageInMilliseconds();
                var data = {
                    type: "INSERT_TIME",
                    appId: appId,
                    timeOnPageMs: timeSpentOnPage,
                    pageName: TimeMe.currentPageName
                };
                TimeMe.websocketSend(data);
            },
            sendInitWsRequest: function (appId) {
                var data = {
                    type: "INIT",
                    appId: appId
                };
                TimeMe.websocketSend(data);
            },

            initialize: function (options) {

                var idleTimeoutInSeconds = TimeMe.idleTimeoutMs || 30;
                var currentPageName = TimeMe.currentPageName || "default-page-name";
                var websocketOptions = undefined;

                if (options) {
                    idleTimeoutInSeconds = options.idleTimeoutInSeconds || idleTimeoutInSeconds;
                    currentPageName = options.currentPageName || currentPageName;
                    websocketOptions = options.websocketOptions;
                }

                TimeMe.setIdleDurationInSeconds(idleTimeoutInSeconds)
                    .setCurrentPageName(currentPageName)
                    .setUpWebsocket(websocketOptions)
                    .listenForVisibilityEvents();

                // TODO - only do this if page currently visible.

                TimeMe.startTimer();
            }
        };
        return TimeMe;
    });
}).call(this);

document.body.style.border = "5px solid blue";

var start = new Date();
TimeMe.startTimer("my-activity");

var data = {};
var blackListedArticles = ["*scrum of scrum*", "*tech alignment*"];


window.addEventListener('unload', function(event) {
    TimeMe.initialize()
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
    
    console.log( end - start);
});

function callAjax(url, data){
    TimeMe.stopTimer("my-activity");
    var timeOnActivity = TimeMe.getTimeOnPageInSeconds("my-activity")
    console.log("timeOnActivity=" + timeOnActivity + " for URL: " + JSON.stringify(data));

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

TimeMe.initialize({
    currentPageName: "my-home-page", // current page
    idleTimeoutInSeconds: 30 // seconds
});
