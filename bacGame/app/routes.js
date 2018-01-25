  var bacApp = angular.module("BaccG");
  bacApp.config(function($stateProvider) {
    var dashboardNumberView = {
      name: 'dashboardNumberView',
      templateUrl: '/bacGame/common/vw/nmbr.html'
    };
    var dashboardTailView = {
      name: 'dashboardTailView',
      templateUrl: '/bacGame/common/vw/tail.html'
    };
    var dashboardModulasView = {
      name: 'dashboardModulasView',
      templateUrl: '/bacGame/common/vw/modulas.html'
    };
    var dashboardNeutralView = {
      name: 'dashboardNeutralView',
      templateUrl: '/bacGame/common/vw/neutral.html'
    };

    $stateProvider.state(dashboardNumberView);
    $stateProvider.state(dashboardTailView);
    $stateProvider.state(dashboardModulasView);
    $stateProvider.state(dashboardNeutralView);
  });
  var bacApp = angular.module("BaccG");
  bacApp.config(function($routeProvider, $locationProvider) {
    var routes = [];
   
    $routeProvider
      .when('/', {
        templateUrl: 'home.html'
      })
      .when('/home', {
        templateUrl: 'home.html'
      })
      .when('/mobile/', {
        templateUrl: 'mobile/home.html'
      })
      .when('/mobile/plyBcGm', {
        templateUrl: 'mobile/app/plyBcGmMobile.html'
      })
      .when('/plyBcGm', {
        templateUrl: 'app/plyBcGmHm.html'
      })
      .when('/mobile/recScr', {
        templateUrl: 'mobile/app/rcdGmMobile.html'
      })
      .when('/recScr', {
        templateUrl: 'app/rcdGmHm.html'
      })
      .when('/mobile/anlytcs', {
        templateUrl: 'mobile/app/anlytcsMobile.html'
      })
      .when('/anlytcs', {
        templateUrl: 'app/anlytcsHm.html'
      })

    $locationProvider.html5Mode(true);
  });
  // configure html5 to get links working on jsfiddle