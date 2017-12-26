var burnCard;
var bacApp = angular.module("BaccG");
bacApp.component('modulasView', ['rzModule', {
  templateUrl: 'common/vw/modulas.html',
  controller: modulasViewController,

}]);

function modulasViewController($scope) {
  ctrl = this;
  burnCard = ctrl.burnCard;
  $scope.minSlider = {
    value: 10
  };
 
}