function getUsername() {
    var input = document.getElementById('username').value;
    var queryParam = '?username=' + input;

    location.href = 'user-dashboard.html' + queryParam;
}

function showUsername(elementId) {
    document.getElementById(elementId).innerHTML = 'Hello ' + location.href.split('username=')[1] + '!';
}