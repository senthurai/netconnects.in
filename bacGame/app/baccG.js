define(["angular","jquery",  "ngStorage", "ngRoute", "angular-ui-router",'angularUiBootstrap',"rzSlider"],
   function(ng) {
     'use strict';
     var bacApp = ng.module("BaccG", ['ngStorage', 'ngRoute', 'ui.router','rzModule','ui.bootstrap']);
     bacApp.controller('bacG', function($scope) {
     var  ctrl = this;
       ctrl.id = {
         val: -1
       };
        ctrl.currInput = new bacPlay(true); 
     });
     return bacApp; 
   });   