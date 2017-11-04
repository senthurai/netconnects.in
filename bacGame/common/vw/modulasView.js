var burnCard;
var bacApp = angular.module("BaccG");
bacApp.component('modulasView', ['rzModule', {
  templateUrl: 'common/vw/modulas.html',
  controller: modulasViewController,

}]);

function modulasViewController($scope) {
  ctrl = this;
  burnCard = ctrl.burnCard;
  ctrl.boardWidth = 5 * 40;
  $scope.minSlider = {
    value: 10
  };
 
}