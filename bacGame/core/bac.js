var bacPlay = function(isInp) {
  var bPlay = this;
  bPlay.isCurrent = false;
  bPlay.isGmInput = false || isInp;
  bPlay.bnkrTtlVl = 0;
  bPlay.plyrTtlVl = 0;
  bPlay.p3 = false;
  bPlay.b3 = false;
  bPlay.guess = 0;
  var cal = function() {
    bPlay.plyrTtl();
    bPlay.bnkrTtl();
  }
  bPlay.player = new EventedArray(cal);
  bPlay.banker = new EventedArray(cal);

  bPlay.getBanker = function() {
    return bPlay.banker;
  }
  bPlay.getPlayer = function() {
    return bPlay.player;
  }
  bPlay.winner = function(bp) {
    return !bp ? "" : bp.bnkrTtlVl >
      bp.plyrTtlVl ? "bnkr" : bp.bnkrTtlVl == bp.plyrTtlVl ? "te" : "plyr"
  };
  bPlay.getCardValue = function(card) {
    return card.cardNum > 9 ? 10 : card.cardNum;
  } 
  bPlay.calculateTotal = function(cards) {
    var total = 0; 

    cards.getArray().forEach(function(card) {
      total += bPlay.getCardValue(card);
    });
    return total % 10;
  } 
  bPlay.plyrTtl = function() {
    if (!bPlay.isGmInput) {
      bPlay.p3 = bPlay.player.getArray()[2] ? true : false;
      bPlay.plyrTtlVl = bPlay.calculateTotal(bPlay.player);
    }
    return bPlay.plyrTtlVl;
  }
  bPlay.bnkrTtl = function() {
    if (!bPlay.isGmInput) {
      bPlay.b3 = bPlay.banker.getArray()[2] ? true : false;
      bPlay.bnkrTtlVl = bPlay.calculateTotal(bPlay.banker);
    }
    return bPlay.bnkrTtlVl;
  }
  return bPlay;
};
var bacGm = function() {
  var bg = this;
  bg.deck = [];
  bg.cardCollector = [];
  bg.games = [];
  var Banker3rdCardRule = [
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    [0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0],
    [1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0],
    [1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0],
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1],
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1],
    [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
  ];

  bg.getCardValue = function(card) {
    return card.cardNum > 9 ? 10 : card.cardNum;
  }
  bg.calculateTotal = function(cards) {
    var total = 0;

    cards.getArray().forEach(function(card) {
      total += bg.getCardValue(card);
    });
    return total % 10;
  }

  bg.initialBurn = function(deck, callbackFunction) {
    var card = deck[deck.length - 1];
    bg.cardCollector.push(card);
    var noOfCardToBurn=bg.getCardValue(card) + 1;
    for (var i = 2; i <= noOfCardToBurn; i++) {
      var nextCard = deck[deck.length - i];
      bg.cardCollector.push(nextCard);
    }
    deck.slice(0, deck.length - (bg.getCardValue(card.cardNum) + 1));
    // burn the cards
    // burn card count is (value of the first card+1).
    // push the burnt card to the card collector.
    if (checkCallBackFunction(callbackFunction)) {
      if (callbackFunction(card)) {
        return;
      }
    }
  }

  bg.playBac = function(setupDeckCbf, initialBurnCbf, playAGameCbf) {
    bg.setupNewDeck(setupDeckCbf);
    bg.initialBurn(bg.deck, initialBurnCbf);
    while (bg.deck.length > 30) {
      bg.playAGame(playAGameCbf);
    }
  }
bg.setupNewDeck=function(setupDeckCbf){
  bg.deck = setupDeck(8, setupDeckCbf);
    bg.deck = shuffleDeck(bg.deck);
}
 bg.playAGame =function (callbackFunction) {
    if (checkCallBackFunction(callbackFunction)) {
      if (callbackFunction.playAGame()) {
        return;
      }
    }
    var game = pullInitialCards(bg.deck);
    if (checkCallBackFunction(callbackFunction)) {
      if (callbackFunction.pullInitialCards(game)) {
        return;
      }
    }
    if (bg.isNaturalOrTie(game)) {
      bg.games.push(game);
      if (checkCallBackFunction(callbackFunction)) {
        callbackFunction.naturalOrTie(game);
      }
      return;
    }
    if (willPlayerGet3rdCard(game.player)) {
      push3rdCardToPlayer(game, bg.deck);
      if (checkCallBackFunction(callbackFunction)) {
        if (callbackFunction.playerGets3rdCard(game)) {
          return;
        }
      }
    }
    if (willBankerGetCard(game)) {
      pushThe3rdCardToBanker(game, bg.deck);
      if (checkCallBackFunction(callbackFunction)) {
        if (callbackFunction.bankerGets3rdCard(game)) {
          return;
        }
      }
    }
    bg.games.push(game);
    if (checkCallBackFunction(callbackFunction)) {
      if (callbackFunction.anounceWinner(game)) {
        return;
      }
    }
  }

  var pullInitialCards = function(deckToPlay) {


    var game = new bacPlay();
    // pull alternate cards drom deck and give two card each to player and banker.
    game.player.push(deckToPlay.pop(0));
    game.banker.push(deckToPlay.pop(0));
    game.player.push(deckToPlay.pop(0));
    game.banker.push(deckToPlay.pop(0));

    return game;

  }

  var willPlayerGet3rdCard = function(player) {

    return player.getArray().length == 2 && bg.calculateTotal(player) < 6;

  }

  var push3rdCardToPlayer = function(game, deckToPlay) {
    game.player.push(deckToPlay.pop(0));
    return game;
  }

  var willBankerGetCard = function(game) {
    if (game.player.length == 2) {
      return true;
    }
    return Banker3rdCardRule[9 - bg.calculateTotal(game.banker)][9 - bg.calculateTotal(game.banker)] == 1;
  }

  var pushThe3rdCardToBanker = function(game, deckToPlay) {
    game.banker.push(deckToPlay.pop(0));
    return game;
  }

  this.isNaturalOrTie = function(game) {
    // if either player or banker gets  8 more return true else false. or if 6 or greater with same value Tie(66,77,88,99).
    return (game.plyrTtlVl >= 8) || (game.bnkrTtlVl >= 8) || (game.plyrTtlVl >5 && game.plyrTtlVl == game.bnkrTtlVl);
  }



};