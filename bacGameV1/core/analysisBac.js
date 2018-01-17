var analysis = function(burnCard) {
  mod = this;
  mod.burnCard = burnCard;
  mod.samples = [];
  mod.forecast = [];
  mod.index = 0;
  mod.pattern = [
    "0000",
    "0001",
    "0010",
    "0011",
    "0100",
    "0101",
    "0110",
    "0111",
    "1000",
    "1001",
    "1010",
    "1011",
    "1100",
    "1101",
    "1110",
    "1111"
  ];
  mod.invert = function(pat) {
    modInvert = this;
    modInvert.result = "";
    var convertedArray = [...pat];
    convertedArray.forEach(function(e) {
      modInvert.result += modInvert.e == "0" ? "1" : "0";
    });
    return modInvert.result;
  }

  mod.patternWeight = {
    key: "",
    value: 0
  };
  mod.pushBinToArr = function(win, index) {
    result = win == "banker" ? "1" : win == "tie" ? "." : "0";
    mod.samples[index / mod.burnCard | 0][index % mod.burnCard] = result;
    mod.forecast[index % mod.burnCard] = mod.forecast[index % mod.burnCard] + result;
    return result;
  }

  mod.calculateNext = function(game, index) {
    mod.pushBinToArr(game.win, index);
  }
}