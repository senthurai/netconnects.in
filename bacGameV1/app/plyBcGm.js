var bacApp = angular.module("BaccG");
bacApp.component('plyBcGm', {
  templateUrl: 'app/plyBcGm.html',
  controller: plyBcGmController
});

function plyBcGmController($boardService) {
  plyBcGmCtrl = this;
  plyBcGmCtrl.data = $boardService.currentBoard();
  plyBcGmCtrl.bacGmtry = new bacGm();
  plyBcGmCtrl.bacGmtry.setupNewDeck();
  $boardService.setCurrentBoard(plyBcGmCtrl.bacGmtry.games);
  plyBcGmCtrl.id = {
    val: -1 
  };
}