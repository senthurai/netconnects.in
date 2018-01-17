  var bacApp = angular.module("BaccG");
  bacApp.config(function($stateProvider) {
    var dashboardNumberView = {
      name: 'dashboardNumberView',
      templateUrl: 'common/vw/nmbr.html'
    };
    var dashboardTailView = {
      name: 'dashboardTailView',
      templateUrl: 'common/vw/tail.html'
    };
    var dashboardModulasView = {
      name: 'dashboardModulasView',
      templateUrl: 'common/vw/modulas.html'
    }; 
    $stateProvider.state(dashboardNumberView);
    $stateProvider.state(dashboardTailView);
    $stateProvider.state(dashboardModulasView);
  });
  var bacApp = angular.module("BaccG");
  bacApp.config(function($routeProvider, $locationProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'home.html'
      })
      .when('/home', {
        templateUrl: 'home.html'
      })
      .when('/plyBcGm', {
        templateUrl: 'app/plyBcGmHm.html'
      })

    .when('/recScr', {
      templateUrl: 'app/rcdGmHm.html'
    })

    .when('/anlytcs', {
      templateUrl: 'app/anlytcsHm.html'
    })

    $locationProvider.html5Mode(true);
  });
  // configure html5 to get links working on jsfiddle