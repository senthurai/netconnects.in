function gmCard(iCardSym,iCardNum){
  this.cardSym=iCardSym;
	var gmCardLoc=this;
  this.cardNum=iCardNum;
	this.toString =function(){
			var sym;
		switch(gmCardLoc.cardSym){
			
			case "Spade":
				 sym='♤';
				break;
				case "Flower":
				sym='♧';
				break;
				case "Diamond":
					sym='◊';
				break;
			case "Heart":
				sym='♡';
				break;
		}
	
		return sym+gmCardLoc.cardNum;
	};
}
 
function setupDeck(noOfDeck, callbackFunction){
	var cardno=[1,2,3,4,5,6,7,8,9,10,11,12,13];
	var cs=["Spade","Flower","Diamond","Heart"];
	var cardDeck=[];
	for(h=0;h<noOfDeck;h++)
	for(i=0;i<cs.length;i++){
		for(j=0;j<cardno.length;j++){
		  var card=new gmCard(cs[i],cardno[j]);
			cardDeck.push(card);
		}
	}
 if(checkCallBackFunction(callbackFunction)){
    callbackFunction();
  }
	return cardDeck;
}
function shuffleDeck(shuffledCards,callbackFunction){
	 var currentIndex = shuffledCards.length, temporaryValue, randomIndex;
  // While there remain elements to shuffle...
  while (0 !== currentIndex) {
    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    // And swap it with the current element.
    temporaryValue = shuffledCards[--currentIndex];
    shuffledCards[currentIndex] = shuffledCards[randomIndex];
    shuffledCards[randomIndex] = temporaryValue;
  }
  if(checkCallBackFunction(callbackFunction)){
    callbackFunction();
  }
  return shuffledCards;
}