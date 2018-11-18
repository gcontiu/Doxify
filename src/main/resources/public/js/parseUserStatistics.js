function populateChart() {
    var WEEK_DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"];
    var ctx = document.getElementById("userChart").getContext("2d");

    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: WEEK_DAYS,
            datasets: [{
                label: '# of Coins',
                data: [12, 19, 3, 5, 2, 3, 0],
                borderWidth: 2,
                borderColor: 'rgb(153, 102, 255)',
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
            },
            scales: {
                xAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Week Days'
                    }
                }],
                yAxes: [{
                    display: true,
                    scaleLabel: {
                        display: true,
                        labelString: 'Coins'
                    }
                }]
            }
        }
    });
}