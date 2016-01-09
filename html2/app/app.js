angular.module('app',['ngRoute', 'ngAnimate', 'ui.bootstrap', 'chart.js'])

.config(['$routeProvider', function($routeProvider){
   $routeProvider.
   		when('/', {templateUrl: '/html2/app/index.html', controller:'AccordionCtrl'}).otherwise({redirectTo: '/'});
}])

.config(function($sceDelegateProvider, $sceProvider) {
	$sceProvider.enabled(false);
   $sceDelegateProvider.resourceUrlWhitelist([
     'self',
     '*://www.youtube.com/**',
     '*://www.imdb.com/**'																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																													
   ]);
 })

.controller('AccordionCtrl', ['$scope','$http', function($scope, $http){
	$http.get('json/templates.json').success(function(data){
		$scope.templates = data; 
	});
}])

.controller('LineCtrl',['$scope','$http', function($scope, $http){
$http.get('json/dataBucket.json').success(function(data){
    var movieTitle = document.getElementById("title").dataset.name;
    movieTitle = movieTitle
    .capitalize()
    .replace(/ /g, '')
    .replace(/:/g, '')
    .replace(/,/g, '')
    .replace(/å/g, 'a')
    .replace(/-/g, '');

var json = {
        "series": ['Imdb Rating'],
        "data": [[]],
        "labels": [],
        "colours": [{ // default
          "fillColor": "rgba(132, 10, 10, 20)",
          "strokeColor": "rgba(38, 114, 114, 20)",
          "pointColor": "rgba(220,220,220,1)",
          "pointStrokeColor": "#fff",
          "pointHighlightFill": "#fff",
          "pointHighlightStroke": "rgba(151,187,205,0.8)"
        }],
        "options": [{
          "scaleShowHorizontalLines": "true",
          "scaleShowVerticalLines": "false",
          "showScale": "true",
          "tooltipTemplate": "<%= value %> $",
          "responsive": "true",
          "onAnimationComplete": "function(){}",
         /* "scaleOverride": "true",*/
          // "scaleBeginAtZero" : "true"
        }]
      };

var json2 = {
        "series": ['Popularity'],
        "data": [[]],
        "labels": [],
        "colours": [{ // default
          "fillColor": "rgba(38, 114, 114, 20)",
          "strokeColor": "rgba(132, 10, 10, 20)",
          "pointColor": "rgba(220,220,220,1)",
          "pointStrokeColor": "#fff",
          "pointHighlightFill": "#fff",
          "pointHighlightStroke": "rgba(151,187,205,0.8)"
        }],
        "options": [{
          "scaleShowHorizontalLines": "true",
          "scaleShowVerticalLines": "false",
          "tooltipTemplate": "<%= value %> $",
          "responsive": "true",
          "onAnimationComplete": "function(){}"
        }]
      };

   /* $scope.series = ['Imdb Rating', 'Popularity'];
    $scope.labels = [];
    $scope.data = [[], []];*/


    movieData = data[movieTitle].data;

    movieData.forEach(function(element) {
      json.labels.push(element.date);
      json2.labels.push(element.date);
      json.data[0].push(element.rating);
      json2.data[0].push(element.popularity);
    });

    var latestPopularity = movieData[movieData.length - 1].popularity;
    document.getElementById('popularity-text').innerHTML = latestPopularity;
  $scope.ocw = json;
  $scope.ocw2 = json2;
  });
}])

.controller('CollapseDemoCtrl', function ($scope) {
  $scope.isCollapsed = false;
})

String.prototype.capitalize = function() {
    return this.replace(/(?:^|\s)\S/g, function(a) { return a.toUpperCase(); });
};
