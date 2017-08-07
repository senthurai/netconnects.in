var bacApp = angular.module("BaccG");
    bacApp.component('bacNumTiles', {
      templateUrl: 'common/nmbrtls.html',
      controller: numberTilesController,
      bindings: {
        data:'<'
      }
    }); 
    function numberTilesController() { 
      ctrl = this;
     
    }