 requirejs.config({
   "baseUrl": "lib",
   shim: {
     uiBootstrap: {
       deps: ['jquery']
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
       deps: ['angular', 'angular-ui-router', 'ngRoute']
     }, 
     angular: { 
       exports: 'angular'
     }
   }, 
   paths: {
     "jq":"../jq",
     "app": "../app",
     "common": "../common",
     "core": "../core", 
     "jquery": "//code.jquery.com/jquery-3.1.1.min",
     "angular": "//npmcdn.com/angular@1.6.2/angular",
     "ngStorage": "//rawgithub.com/gsklee/ngStorage/master/ngStorage",
     "ngRoute": "//code.angularjs.org/1.6.2/angular-route",
     "uiBootstrap": "//maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min",
     "domReady":"https://unpkg.com/domready@1.0.8/ready.min"
   },
   deps:['app/bootstrap'] 
 });
 