var bacApp = angular.module("BaccG");
 function headerController() { 
      ctrl = this;
    } 
    bacApp.component('headersection', {
      templateUrl: 'common/header.html',
      controller: headerController,
      bindings: {
       
      }
    }); 
   