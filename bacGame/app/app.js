 requirejs.config({
   "baseUrl": "lib",
   shim: {
     uiBootstrap: {
       deps: ['jquery','jqueryUi']
     },
     ngStorage: {
       deps: ['angular']
     },
     ngRoute: {
       deps: ['angular']
     },
     "angular-ui-router": {
       deps: ['angular']
     },
     "app/routes": {
       deps: ['angular', 'angular-ui-router', 'ngRoute','app/baccG']
     }, 
     angularUiBootstrap:{
       deps:['angular']
       
     },
     rzSlider:{
       deps:['angular']
     },
     angular: { 
       exports: 'angular'
     }
   }, 
   paths: {
     "app": "../app",
     "common": "../common",
     "core": "../core", 
     "rzSlider":"//rawgit.com/rzajac/angularjs-slider/master/dist/rzslider",
   
     "angular": "//npmcdn.com/angular@1.6.2/angular",
     "angularUiBootstrap":"//cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls",
     "ngStorage": "//rawgithub.com/gsklee/ngStorage/master/ngStorage",
     "ngRoute": "//code.angularjs.org/1.6.2/angular-route",
     "jquery": "//code.jquery.com/jquery-3.1.1.min",
     "jqueryUi":"//code.jquery.com/ui/1.12.1/jquery-ui",
     "uiBootstrap": "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min",
     "domReady":"https://unpkg.com/domready@1.0.8/ready.min"
   },
   deps:['app/bootstrap'] 
 });
 