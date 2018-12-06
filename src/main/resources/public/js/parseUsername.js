function parseUsername() {
    var input = document.getElementById('username').value;
    var queryParam = '?username=' + input;

    location.href = 'user-dashboard.html' + queryParam;
}

function getUsername() {
    return location.href.split('username=')[1];
}