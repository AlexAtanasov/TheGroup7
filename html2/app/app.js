angular.module('app',['ngRoute', 'ngAnimate', 'ui.bootstrap', 'chart.js'])

.config(['$routeProvider', function($routeProvider){
   $routeProvider.
   		when('/', {templateUrl: '/app/index.html', controller:'AccordionCtrl'}).otherwise({redirectTo: '/'});
   		/*when('/templates/:templateId', {
   			templateUrl: '/templates/template-details.html',
   			controller:'TemplateDetailsCtrl'
   		})*/
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

.controller('LineCtrl', function($scope, $http){
  // using success/error callback style
  $http.get('json/dataBucket.json').success(function(data){
    $scope.donutData1 = data;
  }).error(function(err){
    throw err;
  });
})

.controller('CollapseDemoCtrl', function ($scope) {
  $scope.isCollapsed = false;
})

.controller('TemplateDetailsCtrl', ['$scope', '$routeParams', '$http', '$filter', function($scope, $routeParams, $http, $filter){
	var templateId = $routeParams.templateId;
	$http.get('json/templates.json').success(function(data){
		$scope.templates = $filter('filter')(data, function(d){
			return d.id == templateId;
		})[0];
		$scope.mainImage = $scope.templates.images[0].name;
	});

	$scope.setImage = function(image){
		$scope.mainImage = image.name;
	}
}]);