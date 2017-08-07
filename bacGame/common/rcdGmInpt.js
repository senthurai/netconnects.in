var bacApp = angular.module("BaccG");
bacApp.component('rcdgminpt', {
  templateUrl: 'common/rcdGmInpt.html',
  controller: rcdGmInptController,
  bindings: {
    data: '=',
    curr:'='
  }
});
 
function rcdGmInptController($boardService) {
  gmInptCtrl = this; 
  gmInptCtrl.ignr=true;
  gmInptCtrl.data=$boardService.currentBoard;
  init(gmInptCtrl); 
  gmInptCtrl.isPrvsDsbld='false';
  gmInptCtrl.nextBac = function() {
    markNotCurrent(gmInptCtrl.data);
    if (!init(gmInptCtrl)) {
      if (gmInptCtrl.id.val < gmInptCtrl.data.length - 1) {
        gmInptCtrl.id.val++; 
      }
      gmInptCtrl.data[gmInptCtrl.id.val].isCurrent = true;
    } 
    setCurr(gmInptCtrl);
  }; 
 
  gmInptCtrl.AddBac = function() {
    if (!init(gmInptCtrl)) {
      markNotCurrent(gmInptCtrl.data);
      gmInptCtrl.data.splice(++gmInptCtrl.id.val, 0, createBacPlay());
    }
    setCurr(gmInptCtrl);
  };
  gmInptCtrl.delBac = function() {
    gmInptCtrl.data.splice(gmInptCtrl.id.val, 1);
    init(gmInptCtrl);
    setCurr(gmInptCtrl);
  };
  gmInptCtrl.previousBac = function() {
    init(gmInptCtrl);
    markNotCurrent(gmInptCtrl.data);
    if (gmInptCtrl.id.val > 0) {
      gmInptCtrl.id.val--;
      gmInptCtrl.isPrvsDsbld='false'; 
    }else{
     gmInptCtrl.isPrvsDsbld='true'; 
    }
    gmInptCtrl.data[gmInptCtrl.id.val].isCurrent = true;
    setCurr(gmInptCtrl);
  }
}

function init(ct) {
  result = false;
  if (!ct.data || typeof(ct.data) != "object" || ct.data.length === 0) {
    ct.data = [];

    ct.data.push(createBacPlay());
    result = true;
  }
  if (!ct.id || ct.id.val == -1 || ct.id.val > ct.data.length - 1) {
    ct.id = {
      val: ct.data.length - 1
    };
  }
if (!ct.curr) {
    ct.curr = createBacPlay();
  }
  setCurr(ct);
  return result;
}

function createBacPlay() {
  bp = new bacPlay(true);
  bp.isCurrent = true;
  return bp;
}

function setCurr(ct) {
  if (!ct.curr) {
    ct.curr = createBacPlay();
  }
  if (ct.data[ct.id.val] != ct.curr)
     ct.curr=(ct.data[ct.id.val]);
}

function markNotCurrent(data) {
  data.forEach(function(d) {
    d.isCurrent = false;
  });
}