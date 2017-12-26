var bacApp = angular.module("BaccG");
var bacGameAction = function(playGmCtrl) {
  var plyBcGmActnCtrl = this;
  var playGmCtrlv = playGmCtrl;
  plyBcGmActnCtrl.playAGame = function() {
    playGmCtrl.bnkr.splice(0, playGmCtrl.bnkr.length);
    playGmCtrl.plyr.splice(0, playGmCtrl.plyr.length);

  }
  plyBcGmActnCtrl.pullInitialCards = function(game) {
    game.banker.getArray().forEach(function(e) {
      playGmCtrl.bnkr.push(e);
    });
    game.player.getArray().forEach(function(e) {
      playGmCtrl.plyr.push(e);
    });

  }
  plyBcGmActnCtrl.naturalOrTie = function(game) { 

  }
  plyBcGmActnCtrl.playerGets3rdCard = function(game) {
    playGmCtrl.plyr.splice(0, playGmCtrl.plyr.length);
    game.player.getArray().forEach(function(card) {
      playGmCtrl.plyr.push(card);
    });
  }
  plyBcGmActnCtrl.bankerGets3rdCard = function(game) {
    playGmCtrl.bnkr.splice(0, playGmCtrl.bnkr.length);
    game.banker.getArray().forEach(function(card) {
      playGmCtrl.bnkr.push(card);
    });
  }
  plyBcGmActnCtrl.anounceWinner = function(game) {
     playGmCtrl.pTotal.splice(0,1);
      playGmCtrl.bTotal.splice(0,1);
    playGmCtrl.pTotal.push( game.plyrTtlVl);
    playGmCtrl.bTotal.push( game.bnkrTtlVl);
 
  }
  plyBcGmActnCtrl.clear = function() {

  }

}

var plyBcGmNwController = function($boardService) {
  playGmCtrl = this;
  playGmCtrl.bnkr = [];
  playGmCtrl.plyr = [];
  playGmCtrl.pTotal = [0];
  playGmCtrl.bTotal = [0];
  playGmCtrl.currInput = new bacPlay(true);
   playGmCtrl.betValues=[25,100,250,500,1000,2000,3000,4000,5000];
  playGmCtrl.id = {
    val: -1
  };
  playGmCtrl.nextGame = function() {
    if (playGmCtrl.bcGm.deck.length > 7) {
      playGmCtrl.bcGm.playAGame(new bacGameAction(playGmCtrl));
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


