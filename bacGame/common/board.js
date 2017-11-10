var boardController = function($boardService) {
  var boardCtrl = this;
  boardCtrl.$boardService = $boardService;
  boardCtrl.numMap = [];
  boardCtrl.curl = false;
  boardCtrl.verticalBoard = false;
  boardCtrl.DeleteBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
    boardCtrl.modulasData = [];
  };
  boardCtrl.getBoardheight = function(carNum) {
    return (carNum > 9 ? 10 : carNum % 10) + 1;
  };
  boardCtrl.getBoardWidth = function(carNum) {
    return boardCtrl.noOfColumn;
  };
  boardCtrl.newBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
    boardCtrl.modulasData = [];
  };
  boardCtrl.burnCardFn = function(card) {
    boardCtrl.burnCard = card;
  };
  boardCtrl.getModPostionFor = function(index, modNumber, arrayLength) {
    boardCtrl.noOfColumn = (arrayLength / modNumber) | 0;
    if (arrayLength % modNumber > 0) {
      boardCtrl.noOfColumn = boardCtrl.noOfColumn + 1;
    }
    var row = (index % modNumber);
    var column = (index / modNumber) | 0;
    if (column % 2 == 1 && boardCtrl.curl) { 
      row = modNumber - row - 1;
    }
    var j = (row * boardCtrl.noOfColumn) + column;
    return j;

  };
  boardCtrl.getModulasData = function() {
    boardCtrl.modulasNumber = boardCtrl.getBoardheight(boardCtrl.burnCard.cardNum);
    boardCtrl.modulasData = [];
    boardCtrl.data.forEach(function(dat, i) {
      j = boardCtrl.getModPostionFor(i, boardCtrl.modulasNumber, boardCtrl.data.length);
      boardCtrl.modulasData[j] = dat;
    });
    return boardCtrl.modulasData;
  };
  boardCtrl.anlytcs = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    delete boardCtrl.modulasData;
    var bacGmtry = new bacGm();

    bacGmtry.playBac(null, boardCtrl.burnCardFn, null);
    boardCtrl.curl = boardCtrl.burnCard.cardNum % 2 == 0;
    boardCtrl.data = bacGmtry.games;
    boardCtrl.modulasData = [];
    boardCtrl.modulasData = boardCtrl.getModulasData();
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