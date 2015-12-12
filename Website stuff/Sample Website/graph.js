function chart(key, div_id, json_file){
  var xmlhttp = new XMLHttpRequest();
  var url = json_file;

  xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
      var myArray= JSON.parse(xmlhttp.responseText);
      var myRows = rows(key, myArray);
      drawChart(myRows);
    }
  };
  xmlhttp.open("GET", url, true);
  xmlhttp.send();

  // Takes the title of a movie (key), returns an array of rows
  var rows = function(movie, datajson) {
    var rowsArray=[];
    for (var i = 0; i < datajson[movie].data.length; i++) {
      rowsArray[i] = [new Date(datajson[movie].data[i].date), 
      parseFloat(datajson[movie].data[i].rating), 
      parseInt(datajson[movie].data[i].popularity)]
    };
    return rowsArray;
  };


  google.load('visualization', '1.1', {packages: ['line', 'corechart']});
  google.setOnLoadCallback(drawChart);

  function drawChart(rows) {

    var chartDiv = document.getElementById(div_id);

    var data = new google.visualization.DataTable();
    data.addColumn('date', 'Date');
    data.addColumn('number', "IMDb Rating");
    data.addColumn('number', "Popularity");

    console.log("From chart: ", rows);
    data.addRows(rows);

    var materialOptions = {
      chart: {
        title: 'Change in Popularity and IMDb Rating over time'
      },
      width: 600,
      height: 300,
      series: {
        // Gives each series an axis name that matches the Y-axis below.
        0: {axis: 'Rating'},
        1: {axis: 'Popularity'}
      },
      axes: {
        // Adds labels to each axis; they don't have to match the axis names.
        y: {
          Rating: {label: 'IMDb Rating (out of 10)'},
          Popularity: {label: 'Number of Tweets'}
        }
      }
    };


    function drawMaterialChart() {
      var materialChart = new google.charts.Line(chartDiv);
      materialChart.draw(data, materialOptions);
    }


    drawMaterialChart();

  };
};


//chart("He Named Me Malala", "chart_div", "./example.json");
