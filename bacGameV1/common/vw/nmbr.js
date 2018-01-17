var bacApp = angular.module("BaccG");
    bacApp.component('dashboardNumberView', {
      templateUrl: 'common/vw/nmbr.html',
      controller: numberViewController,
       bindings: {
    data: '=',
    curr:'='
  }
    });
    function numberViewController() { 
      ctrl = this;
    }