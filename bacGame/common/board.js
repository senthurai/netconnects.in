var boardController = function($boardService) {
    var boardCtrl = this;
    boardCtrl.$boardService = $boardService;
    boardCtrl.numMap = [];

    boardCtrl.DeleteBoard = function() {
      delete boardCtrl.numMap;
      delete boardCtrl.data;
      boardCtrl.data = [];
    };
    boardCtrl.getBoardheight = function(carNum) {
      return (carNum % 10 == 0 ? 11 : carNum % 10) + 1;
    };
boardCtrl.getBoardWidth = function(carNum) {
      return boardCtrl.noOfColumn;
    };
    boardCtrl.newBoard = function() {
      delete boardCtrl.numMap;
      delete boardCtrl.data;
      boardCtrl.data = [];
    };
    boardCtrl.burnCardFn = function(card) {
      boardCtrl.burnCard = card;
    };
    boardCtrl.getModPostionFor = function(index, modNumber, arrayLength) {
      boardCtrl.noOfColumn = (arrayLength / modNumber)|0;
      if(arrayLength%modNumber>0){
        boardCtrl.noOfColumn=boardCtrl.noOfColumn+1;
      }
     var row = (index%modNumber);
     var column=(index%boardCtrl.noOfColumn);
     var j= (row * boardCtrl.noOfColumn)+column;
      return j;

    };
    boardCtrl.getModulasData = function() {
      delete boardCtrl.modulasData;
      boardCtrl.modulasData = [];
      boardCtrl.modulasNumber = boardCtrl.getBoardheight(boardCtrl.burnCard.cardNum);
      boardCtrl.data.forEach(function(data, i) {
          j = boardCtrl.getModPostionFor(i, boardCtrl.modulasNumber, boardCtrl.data.length);
          boardCtrl.modulasData[j] = boardCtrl.data[i];
        });
      };
      boardCtrl.anlytcs = function() {
        delete boardCtrl.numMap;
        delete boardCtrl.data;
        var bacGmtry = new bacGm();
        bacGmtry.playBac(null, boardCtrl.burnCardFn, null);
        boardCtrl.data = bacGmtry.games;
        boardCtrl.modulasData = boardCtrl.getModulasData();
        boardCtrl.numMap = getWeightMap(boardCtrl.data);
      };

    };
    var bacApp = angular.module("BaccG");
    bacApp.component('bacboard', {
      controller: boardController,
      templateUrl: 'common/board.html',
      bindings: {
        data: '=',
        modulasData:'='
      }
    });