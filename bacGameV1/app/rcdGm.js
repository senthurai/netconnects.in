var bacApp = angular.module("BaccG");
bacApp.component('rcdGm', {
  templateUrl: 'app/rcdGm.html',
  controller: rcdGmController
});

function rcdGmController( $boardService) {
  recGmCtrl = this;
  recGmCtrl.data = $boardService.currentBoard();
  recGmCtrl.currInput = new bacPlay(true);
  
  recGmCtrl.id = {
    val: -1
  };
}