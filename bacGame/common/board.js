var boardController = function($boardService) {
  var boardCtrl = this;
  boardCtrl.$boardService= $boardService;
  boardCtrl.numMap = [];

  boardCtrl.DeleteBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
  };


  boardCtrl.newBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
  };
  boardCtrl.anlytcs = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    bacGmtry = new bacGm();
    bacGmtry.playBac();
    boardCtrl.data = bacGmtry.games;
    boardCtrl.numMap = getWeightMap(boardCtrl.data);
  };

};
var bacApp = angular.module("BaccG");
bacApp.component('bacboard', {
  controller: boardController,
  templateUrl: 'common/board.html',
  bindings: {
    data: '='
  }
});