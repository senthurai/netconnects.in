function getMapValue(mp, key) {
  if (!mp[key]) {
    mp[key] = [];
  }
  return mp[key];
}
function getWeightMap(games,startBag) {
  bag = {};
  if (!startBag) {
    startBag = [];
    startBag.push(true);
    startBag.push(false);
    startBag.push(true);
    startBag.push(true);
    startBag.push(false);
    startBag.push(true);
    startBag.push(true);
    startBag.push(false);
    startBag.push(true);
    startBag.push(false);
  }
  for (var i = 1; i < games.length; i++) {
    prev = games[i - 1];
    nxt=games[i];
    winNext = nxt.winner(games[i]);
    var bBag = getMapValue(bag, prev.bnkrTtlVl);
    var pBag = getMapValue(bag, prev.plyrTtlVl);
    var bankerLength = bBag.length;
    var playerLength = pBag.length;
    bBag = getMapValue(bag, prev.bnkrTtlVl);
    pBag = getMapValue(bag, prev.plyrTtlVl);
    var bInvrsnBln=false;
    var pInvrsnBln=false;
    if (bankerLength > 1) {
      prvBknrBg = bBag[bankerLength - 2];
      bInvrsnBln = clcltInvrsn(prvBknrBg, winNext,"bnkr");
    }else {
      bInvrsnBln = startBag[prev.bnkrTtlVl];
    } 
    if (playerLength > 1) {
      prvPlyrBg = pBag[playerLength - 2];
      pInvrsnBln = clcltInvrsn(prvPlyrBg, winNext,"plyr");
    } else {
      pInvrsnBln = startBag[prev.plyrTtlVl];
    } 
    nxt.guess= ((pInvrsnBln?0:1)+(bInvrsnBln?0:1)) % 2;
    if (winNext === 'bnkr') {
      getMapValue(bag, prev.bnkrTtlVl).push(cnstrctAnlytcs(bankerLength + 1, "win", prev.plyrTtlVl, "bnkr", bInvrsnBln));
      getMapValue(bag, prev.plyrTtlVl).push(cnstrctAnlytcs(playerLength + 1, "lose", prev.bnkrTtlVl, "plyr", pInvrsnBln));
    } else if (winNext == 'plyr') {
      getMapValue(bag, prev.bnkrTtlVl).push(cnstrctAnlytcs(bankerLength + 1, "lose", prev.plyrTtlVl, "bnkr", bInvrsnBln));
      getMapValue(bag, prev.plyrTtlVl).push(cnstrctAnlytcs(playerLength + 1, "win", prev.bnkrTtlVl, "plyr", pInvrsnBln));
    } else {
      getMapValue(bag, prev.bnkrTtlVl).push(cnstrctAnlytcs(bankerLength + 1, "tie", prev.plyrTtlVl, "bnkr", bInvrsnBln));
      getMapValue(bag, prev.plyrTtlVl).push(cnstrctAnlytcs(playerLength + 1, "tie", prev.bnkrTtlVl, "plyr", pInvrsnBln));
    }
  }
  return bag;
}

function cnstrctAnlytcs(lngth, status, oppst, side, invrsnBlm) {
  return {
    length: lngth,
    "status": status,
    "opposite": oppst,
    "side": side,
    invrsnBln: invrsnBlm
  };
}

function clcltInvrsn(dt, wnnr,side) {
  return ((dt.invrsnBln?1:0)+(dt.status===wnnr?0:1))%2; 
}