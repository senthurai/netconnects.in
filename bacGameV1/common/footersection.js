var bacApp = angular.module("BaccG");
  function footerController() { 
      ctrl = this;
    }
    bacApp.component('footersection', {
      templateUrl: 'common/footer.html',
      controller: footerController,
      bindings: {
       
      } 
    }); 
  