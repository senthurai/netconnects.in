define(["angular","jquery",  "ngStorage", "ngRoute", "angular-ui-router"],
   function(ng) {
     'use strict';
     var bacApp = ng.module("BaccG", ['ngStorage', 'ngRoute', 'ui.router']);
     bacApp.controller('bacG', function($scope) {
     var  ctrl = this;
       ctrl.id = {
         val: -1
       };
        ctrl.currInput = new bacPlay(true); 
     });
     return bacApp; 
   });   