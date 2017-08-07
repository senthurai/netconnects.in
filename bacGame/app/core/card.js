var bacApp = angular.module("BaccG");
bacApp.component('card', {
  templateUrl: 'app/core/card.html',
  controller: cardController,
  bindings: {
    data: '='
  }
});
var cardController = function() {}