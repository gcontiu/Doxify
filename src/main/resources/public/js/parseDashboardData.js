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

function getAllByParam(elementId, userFullNameId, coinsGainedId, param) {
    var ajax = new XMLHttpRequest();
    var requestParam = "?username=" + param;
    ajax.onload = function () {

        if (ajax.status >= 200 && ajax.status < 400) {
            var response = JSON.parse(ajax.response);

            for (var i in response) {
                console.log(response[i].userFullName);

                // TODO map elements by ID into User's Statistics Page; You can use other type of mapping if useful
                document.getElementById(userFullNameId).innerHTML = 'Hello, ' + response[i].userFullName + '!';
                document.getElementById(coinsGainedId).innerHTML = response[i].coinsGained;
            }
        }
    };

    ajax.open("GET", "http://localhost:8585/" + elementId + requestParam, true);
    ajax.send();
}