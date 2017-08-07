var bacApp = angular.module("BaccG");
bacApp.service('$boardService', function ($localStorage) {
  var boardDataCtrl = this;
  boardDataCtrl.storage = $localStorage.$default({
    "currentBoard": [],
    "id": {
        "val": 0
    }
});
  boardDataCtrl.currentBoard=function(){
    return boardDataCtrl.storage.currentBoard;
  }
  boardDataCtrl.setCurrentBoard=function(newBoard){
    boardDataCtrl.storage.currentBoard=newBoard;
  }
});