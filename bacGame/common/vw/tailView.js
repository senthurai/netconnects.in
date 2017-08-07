var bacApp = angular.module("BaccG");
    bacApp.component('dashboardTailView', {
      templateUrl: 'common/vw/tail.html',
      controller: tailViewController,
       bindings: {
    data: '=',
    curr:'='
  }
    });
    function tailViewController() { 
      ctrl = this;
    }