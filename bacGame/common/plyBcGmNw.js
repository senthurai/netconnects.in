var bacApp = angular.module("BaccG");
var plyBcGmNwController = function($boardService) {
  recGmCtrl = this;
  recGmCtrl.bnkr = [];
  recGmCtrl.plyr = [];
  recGmCtrl.pTotal = [0,1];
  recGmCtrl.bTotal = [0,1];
  recGmCtrl.currInput = new bacPlay(true);
  recGmCtrl.id = {
    val: -1
  };
  recGmCtrl.nextGame = function() {
    if (recGmCtrl.bcGm.deck.length > 7) {
      recGmCtrl.bcGm.playAGame(new bacGameAction(recGmCtrl));
    }
  }
};
bacApp.component('plyBcGmNw', {
  templateUrl: 'common/plyBcGmNw.html',
  controller: plyBcGmNwController,
  bindings: {
    bcGm: '='
  }
});


var bacGameAction = function(recGmCtrl) {
  var plyBcGmActnCtrl = this;
  var recGmCtrlv = recGmCtrl;
  plyBcGmActnCtrl.playAGame = function() {
    recGmCtrl.bnkr.splice(0, recGmCtrl.bnkr.length);
    recGmCtrl.plyr.splice(0, recGmCtrl.plyr.length);

  }
  plyBcGmActnCtrl.pullInitialCards = function(game) {
    game.banker.getArray().forEach(function(e) {
      recGmCtrl.bnkr.push(e);
    });
    game.player.getArray().forEach(function(e) {
      recGmCtrl.plyr.push(e);
    });

  }
  plyBcGmActnCtrl.naturalOrTie = function(game) { 

  }
  plyBcGmActnCtrl.playerGets3rdCard = function(game) {
    recGmCtrl.plyr.splice(0, recGmCtrl.plyr.length);
    game.player.getArray().forEach(function(e) {
      recGmCtrl.plyr.push(e);
    });
  }
  plyBcGmActnCtrl.bankerGets3rdCard = function(game) {
    recGmCtrl.bnkr.splice(0, recGmCtrl.bnkr.length);
    game.banker.getArray().forEach(function(e) {
      recGmCtrl.bnkr.push(e);
    });
  }
  plyBcGmActnCtrl.anounceWinner = function(game) {
     plyBcGmActnCtrl.pTotal.splice(0,1);
      plyBcGmActnCtrl.bTotal.splice(0,1);
    plyBcGmActnCtrl.pTotal.push( game.plyrTtlVl);
    plyBcGmActnCtrl.bTotal.push( game.bnkrTtlVl);
 
  }
  plyBcGmActnCtrl.clear = function() {

  }

}
