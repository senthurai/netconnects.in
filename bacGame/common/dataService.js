var bacApp = angular.module("BaccG");
bacApp.service('$boardService', function ($localStorage) {
  var boardDataCtrl = this;
  boardDataCtrl.storage = $localStorage.$default({
    "currentBoard": [],
    "currentGamIndex": 0,
    "selectedGamIndex":0
});
  boardDataCtrl.currentBoard=function(){
    return boardDataCtrl.storage.currentBoard;
  }
   boardDataCtrl.getSeletedGam=function(){
    return boardDataCtrl.storage.currentBoard[boardDataCtrl.storage.selectedGamIndex];
  }
    boardDataCtrl.getCurrentGam=function(){
    return boardDataCtrl.storage.currentBoard[boardDataCtrl.storage.currentGamIndex];
  }
  boardDataCtrl.setCurrentBoard=function(newBoard){
    boardDataCtrl.storage.currentBoard=newBoard;
  }
  boardDataCtrl.setCurrentGam=function(index){
    boardDataCtrl.storage.currentGamIndex=index;
  }
   boardDataCtrl.setSelectedGam=function(index){
    boardDataCtrl.storage.selectedGamIndex=index;
  }
  
});