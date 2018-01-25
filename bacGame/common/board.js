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
  boardCtrl.$boardService = $boardService;
  boardCtrl.numMap = [];
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
    var modData = this;
    boardCtrl.modulasData = [];
    if (boardCtrl.skipTie) {
      modData.length = boardCtrl.data.length - boardCtrl.totalTieWin;
    } else {
      modData.length = boardCtrl.data.length;
    }
    boardCtrl.data.forEach(function(dat, i) {
      if (!(boardCtrl.skipTie && dat.win == 'Tie')) {
        j = boardCtrl.getModPostionFor(i - dat.totalTieWin, boardCtrl.modulasNumber, modData.length);
        boardCtrl.modulasData[j] = dat;
      }
    });
    return boardCtrl.modulasData;
  };
  boardCtrl.findLastWin = function(i) {
    if (i > 0) {
      if (boardCtrl.data[--i].win != 'Tie') {
        return boardCtrl.data[i].win;
      } else {
        return boardCtrl.findLastWin(i);
      }
    } else {
      return boardCtrl.data[0].win;
    }
  };
  boardCtrl.isBreak = function(gm, index) {
    return (boardCtrl.data[index].win == "Tie" ? false : gm.win != boardCtrl.findLastWin(index));
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
    console.log("--" + playWin + "P B" + bankWin + "is Equal");
    return win != 'Tie' ? bankWin == playWin : false;
  }
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
    boardCtrl.modulasData = [];
    boardCtrl.modulasData = boardCtrl.getModulasData();
  };
  boardCtrl.newGame = function() {
    if (boardCtrl.ctrl && boardCtrl.bacGmtry.deck.length > 52) {
      boardCtrl.bacGmtry.playAGame(new boardCtrl.playGame());
      boardCtrl.data = boardCtrl.bacGmtry.games;
      boardCtrl.modulasData = boardCtrl.getModulasData();
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