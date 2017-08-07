var bacApp = angular.module("BaccG");
    bacApp.component('modulasView', {
      templateUrl: 'common/vw/modulas.html',
      controller: modulasViewController,
       bindings: {
    data: '=',
    curr:'='
  }
    });
    function modulasViewController() { 
      ctrl = this;
    }