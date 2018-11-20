function populateChart(datasetLabels, inputData, colorLine) {
    var ctx = document.getElementById('userChart').getContext('2d');
    var chart = new Chart(ctx);

    chart.config = {
        type: 'line',
        data: {
            labels: datasetLabels,
            datasets: [{
                label: '# of Coins',
                data: inputData,
                borderWidth: 2,
                borderColor: colorLine,
                fill: false
            }]
        },
        options: {
            responsive: true,
            tooltips: {
                mode: 'index',
                intersect: false
            },
            hover: {
                mode: 'nearest',
                intersect: true
            }
        }
    };

    return chart;
}

function doXAxisWithDays() {
    var WEEK_DAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    var labelName = 'Week Days';
    var inputData = [12, 19, 3, 5, 2, 3, 0];
    var colorLine = 'rgb(153, 102, 255)';
    var chart = populateChart(WEEK_DAYS, inputData, colorLine);

    chart.options.scales = {
        xAxes: [{
            display: true,
            scaleLabel: {
                display: true,
                labelString: labelName
            }
        }],
        yAxes: [{
            display: true,
            scaleLabel: {
                display: true,
                labelString: 'Coins'
            }
        }]
    };
    chart.update();
}

function doXAxisWithMonths() {
    var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    var labelName = 'Months';
    var inputData = [5, 7, 9, 6, 10, 12, 19, 0, 5, 2, 3, 1];
    var colorLine = 'rgb(255, 159, 64)';
    var chart = populateChart(MONTHS, inputData, colorLine);

    chart.options.scales = {
        xAxes: [{
            display: true,
            scaleLabel: {
                display: true,
                labelString: labelName
            }
        }],
        yAxes: [{
            display: true,
            scaleLabel: {
                display: true,
                labelString: 'Coins'
            }
        }]
    };
    chart.update();
}

function doToggle() {
    var weekly = 'Toggle Weekly Data';
    var monthly = 'Toggle Monthly Data';
    var btn = document.getElementById('toggleBtn');

    if (btn.value === weekly) {
        btn.value = monthly;
        btn.className = 'btn btn-outline-warning';
        doXAxisWithDays();
    } else {
        btn.value = weekly;
        btn.className = 'btn btn-outline-primary';
        doXAxisWithMonths();
    }
}