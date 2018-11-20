var boardController = function($boardService, $state, $scope) {
  var boardCtrl = this;
  $scope.tt = "dashboardModulasView";
  boardCtrl.options = [{
      label: 'Modulas',
      value: 'dashboardModulasView'
    },
    {
      label: 'Tail View',
      value: 'dashboardTailView'
    },
    {
      label: 'Number',
      value: 'dashboardNumberView'
    }, {
      label: 'Neutral',
      value: 'dashboardNeutralView'
    }

  ];
  $scope.$watch('tt', function(value) {
    if (value) {
      $state.go(value);
    }
  });
  $scope.boardService = $boardService;
  boardCtrl.boardService = $boardService;
  $scope.$watch('boardService.storage.selectedGamIndex', function(value) {
    if (value) {
      boardCtrl.currentGm = boardCtrl.boardService.getSeletedGam();
    }
  });
  boardCtrl.numMap = [];
  boardCtrl.tailTable = [];
  boardCtrl.curl = false;
  boardCtrl.ctrl = true;
  boardCtrl.totalTieWin = 0;
  boardCtrl.totalPlayerWin = 0;

  boardCtrl.totalBankerWin = 0;
  boardCtrl.skipTie = true;
  boardCtrl.modulasNumber = 0;
  boardCtrl.bacGmtry = new bacGm();
  boardCtrl.DeleteBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
    boardCtrl.modulasData = [];
  };
  boardCtrl.getBoardheight = function(carNum) {
    value = (carNum > 9 ? 10 : carNum);
    if (carNum < 6) {
      value = 10 - carNum;
    }
    return value;
  };
  boardCtrl.getBoardWidth = function(carNum) {
    return boardCtrl.noOfColumn;
  };
  boardCtrl.newBoard = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    boardCtrl.data = [];
    boardCtrl.modulasData = [];
    boardCtrl.boardService.currentBoard = [];
  };
  boardCtrl.burnCardFn = function(card) {
    boardCtrl.burnCard = card;
  };
  boardCtrl.getModPostionFor = function(index, modNumber, arrayLength) {

    var row = (index % modNumber);
    var column = (index / modNumber) | 0;
    if (column % 2 == 1 && boardCtrl.curl) {
      row = modNumber - row - 1;
    }
    var j = (row * boardCtrl.noOfColumn) + column;
    return j;

  };
  boardCtrl.getModulasData = function() {
    var modData = this;
    boardCtrl.modulasData = [];
    if (boardCtrl.skipTie) {
      modData.length = boardCtrl.data.length - boardCtrl.totalTieWin;
    } else {
      modData.length = boardCtrl.data.length;
    }
    boardCtrl.noOfColumn = (modData.length / boardCtrl.modulasNumber) | 0;
    if (modData.length % boardCtrl.modulasNumber > 0) {
      boardCtrl.noOfColumn = boardCtrl.noOfColumn + 1;
    }
    var tie = 0;
    boardCtrl.data.forEach(function(dat, i) {

      if (dat.win == 'Tie') {
        tie++;
      }
      //skipTie|TIE|output
      //  1       0   - 1  
      //  1       1   - 0
      //  0       0   - 1
      //  0       1   - 1
      if (!(boardCtrl.skipTie && dat.win == 'Tie')) {
        j = boardCtrl.getModPostionFor(i - (boardCtrl.skipTie ? tie : 0), boardCtrl.modulasNumber, modData.length);
        boardCtrl.modulasData[j] = dat;
        boardCtrl.boardService.setCurrentGam(i);
      }


    });
    return boardCtrl.modulasData;
  };


  boardCtrl.getSelectedGam = function() {
    return boardCtrl.boardService.getSeletedGam();
  }

  boardCtrl.findLastWin = function(i, curr) {
    if (i > 0) {
      if (boardCtrl.data[--i].win != 'Tie') {
        return boardCtrl.data[i].win;
      } else {
        return boardCtrl.findLastWin(i);
      }
    } else {
      return boardCtrl.data[0].win == 'Tie' ? curr : boardCtrl.data[0].win;
    }
  };
  boardCtrl.isBreak = function(gm, index) {
    return (boardCtrl.data[index].win == "Tie" ? false : gm.win != boardCtrl.findLastWin(index, boardCtrl.data[index].win));
  }
  boardCtrl.playGame = function() {
    board = this;
    board.anounceWinner = function(game) {
      if (game.win == 'Banker')
        boardCtrl.totalBankerWin++;
      if (game.win == 'Player')
        boardCtrl.totalPlayerWin++;
      if (game.win == 'Tie')
        boardCtrl.totalTieWin++;

    }

  };
  boardCtrl.isEqualWin = function(win, bankWin, playWin) {

    return win != 'Tie' ? bankWin == playWin : false;
  }
  boardCtrl.getTailData = function() {
    tailCtrl = this;
    tailCtrl.columnData = [];
    boardCtrl.tailTable = [];
    boardCtrl.tailTable.push(tailCtrl.columnData);

    boardCtrl.data.forEach(function(gm, index) {
      if (boardCtrl.isBreak(gm, index)) {
        tailCtrl.columnData = [];
        boardCtrl.tailTable.push(tailCtrl.columnData);
      }
      tailCtrl.columnData.push(gm);
    });
    return boardCtrl.tailTable;
  };

  boardCtrl.anlytcs = function() {
    delete boardCtrl.numMap;
    delete boardCtrl.data;
    delete boardCtrl.modulasData;
    boardCtrl.bacGmtry = new bacGm();
    boardCtrl.totalTieWin = 0;
    boardCtrl.totalPlayerWin = 0;
    boardCtrl.totalBankerWin = 0;
    boardCtrl.modNum = 0;
    // bacGmtry.playBac(null, boardCtrl.burnCardFn, new boardCtrl.playGame());
    boardCtrl.bacGmtry.setupNewDeck(null);
    boardCtrl.bacGmtry.initialBurn(boardCtrl.bacGmtry.deck, boardCtrl.burnCardFn);
    while (boardCtrl.bacGmtry.deck.length > 52 && !boardCtrl.ctrl) {
      boardCtrl.bacGmtry.playAGame(new boardCtrl.playGame());
    }
    boardCtrl.modulasNumber = boardCtrl.getBoardheight(boardCtrl.burnCard.cardNum);

    boardCtrl.data = boardCtrl.bacGmtry.games;
    boardCtrl.boardService.setCurrentBoard(boardCtrl.data);
    boardCtrl.modulasData = [];
    boardCtrl.modulasData = boardCtrl.getModulasData();
    boardCtrl.tailTable = boardCtrl.getTailData();
  };
  boardCtrl.newGame = function() {
    if (boardCtrl.ctrl && boardCtrl.bacGmtry.deck.length > 52) {
      boardCtrl.bacGmtry.playAGame(new boardCtrl.playGame());
      boardCtrl.data = boardCtrl.bacGmtry.games;
      boardCtrl.boardService.setCurrentBoard(boardCtrl.data);
      boardCtrl.modulasData = boardCtrl.getModulasData();
      boardCtrl.tailTable = boardCtrl.getTailData();

    }
    console.log("new");
  }
  boardCtrl.previousGame = function() {
    console.log("prev");
  }
  boardCtrl.nextGame = function() {
    console.log("next");
  }
  boardCtrl.lastGame = function() {
    console.log("last");
  }
};

var bacApp = angular.module("BaccG");
bacApp.component('bacboard', {
  controller: boardController,
  templateUrl: ['$element', '$attrs', function($element, $attrs) {
    if ($attrs.device == "mobile") {
      return "mobile/common/boardMobile.html";
    }
    return 'common/board.html';
  }],
  bindings: {
    data: '='
  }
});