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