function getAllAuthors() {
    var response = null;
    var ajax = new XMLHttpRequest();
    ajax.open("GET", "http://localhost:8585/allAuthors", true);
    ajax.send();

    ajax.onload = function () {
        response = JSON.parse(this.response);
        if (ajax.status >= 200 && ajax.status < 400) {
            console.log(response);
            return response;
        }
    };

    return response;
}