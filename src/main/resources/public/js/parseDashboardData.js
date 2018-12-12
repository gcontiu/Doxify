function getAll(elementId) {
    var ajax = new XMLHttpRequest();
    ajax.open("GET", "http://localhost:8585/" + elementId, true);
    ajax.send();

    ajax.onload = function () {
        var response = JSON.parse(this.response);
        if (ajax.status >= 200 && ajax.status < 400) {
            document.getElementById(elementId).innerHTML = response;
        }
    };
}

function populateAuthorRankTable() {
    var ajax = new XMLHttpRequest();
    ajax.open("GET", "http://localhost:8585/authorStats", true);
    ajax.send();

    ajax.onload = function () {
        var response = JSON.parse(this.response);
        if (ajax.status === 200) {
            for (var i = 0; i < response.length; i++) {
                var tableBody = document.getElementById("authorRankTableBody");
                var row = document.createElement("tr");
                var rowHeader = document.createElement("th");
                var fullNameCell = document.createElement("td");
                var coinsCell = document.createElement("td");
                var articleCell = document.createElement("td");
                var mostReadArticleURL = document.createElement("a");

                var rank = document.createTextNode(response[i]["rank"]);
                var fullName = document.createTextNode(response[i]["fullName"]);
                var coins = document.createTextNode(response[i]["totalCoins"]);
                var mostReadArticleName = document.createTextNode(response[i]["mostReadArticleName"]);

                mostReadArticleURL.setAttribute("href", response[i]["mostReadArticleURL"]);
                mostReadArticleURL.appendChild(mostReadArticleName);

                articleCell.appendChild(mostReadArticleURL);
                coinsCell.appendChild(coins);
                fullNameCell.appendChild(fullName);
                rowHeader.appendChild(rank);
                row.appendChild(rowHeader);
                row.appendChild(fullNameCell);
                row.appendChild(coinsCell);
                row.appendChild(articleCell);
                tableBody.appendChild(row);
            }
        }
    }
}

function populateOtherArticlesTable(category) {
    var ajax = new XMLHttpRequest();
    ajax.open("GET", "http://localhost:8585/articleStats?category=" + category, true);
    ajax.send();

    ajax.onload = function () {
        var response = JSON.parse(this.response);
        if (ajax.status === 200) {
            for (var i = 0; i < response.length; i++) {

                var tableBody = document.getElementById(category + "ArticlesTableBody");
                var row = document.createElement("tr");
                var rowHeader = document.createElement("th");
                var readTimesCell = document.createElement("td");
                var articleCell = document.createElement("td");
                var articleURL = document.createElement("a");

                var rank = document.createTextNode(response[i]["rank"]);
                var readTimes = document.createTextNode(response[i]["timesRead"]);
                var articleName = document.createTextNode(response[i]["articleTitle"]);

                articleURL.setAttribute("href", response[i]["articleUrl"]);
                articleURL.appendChild(articleName);

                articleCell.appendChild(articleURL);
                readTimesCell.appendChild(readTimes);
                rowHeader.appendChild(rank);
                row.appendChild(rowHeader);
                row.appendChild(articleCell);
                row.appendChild(readTimesCell);
                tableBody.appendChild(row);
            }
        }
    }
}


function getAllByParam(elementId, userFullNameId, coinsGainedId, nrOfArticlesId, param) {
    var ajax = new XMLHttpRequest();
    var requestParam = "?username=" + param;
    ajax.onload = function () {

        if (ajax.status >= 200 && ajax.status < 400) {
            var response = JSON.parse(ajax.response);

            for (var i in response) {

                // TODO map elements by ID into User's Statistics Page; You can use other type of mapping if useful
                document.getElementById(userFullNameId).innerHTML = 'Hello, ' + response[i].userFullName + '!';
                document.getElementById(coinsGainedId).innerHTML = response[i].coinsGained;
                document.getElementById(nrOfArticlesId).innerText = response[i].totalNumberOfArticles;
            }
        }
    };

    ajax.open("GET", "http://localhost:8585/" + elementId + requestParam, true);
    ajax.send();
}