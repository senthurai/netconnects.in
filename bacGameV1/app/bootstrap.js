/**
 * bootstraps angular onto the window.document node
 * NOTE: the ng-app attribute should not be on the index.html when using ng.bootstrap
 */
define([
    'require',
    'angular',
    "angular-ui-router",
    'app/baccG',
    'app/routes', 
    'uiBootstrap'
], function (require, ng) { 
  
    /*
     * place operations that need to initialize prior to app start here
     * using the `run` function on the top-level module
     */
    require([
      'domReady',
      'uiBootstrap',
      'core/deck',
      'core/bacAnalysis',
      'core/util',
      'core/bac',
      'app/core/card',
       'app/plyBcGm',
      'common/footersection',
      'common/headersection',
      'common/plyBcGmNw', 
      'common/nmbrTls', 
      'common/tiles',
      'common/board',
      'common/rcdGmInpt', 
      'common/dataService',
      'app/rcdGm'], function (document) {
        angular.bootstrap(document, ['BaccG']); 
    }); 
});