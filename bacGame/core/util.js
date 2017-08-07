var EventedArray = function(handler) {
  this.stack = [];
  this.mutationHandler = handler || function() {};
  this.setHandler = function(f) {
    this.mutationHandler = f;
  };
  this.callHandler = function() {
    if (typeof this.mutationHandler === 'function') {
      this.mutationHandler();
    }
  };
  this.push = function(obj) {
    this.stack.push(obj);
    this.callHandler();
  };
  this.pop = function() {
    this.callHandler();
    return this.stack.pop();
  };
  this.getArray = function() {
    return this.stack;
  }
  return this;
}
Array.prototype.indexOf || (Array.prototype.indexOf = function(d, e) {
  var a;
  if (null === this) throw new TypeError('"this" is null or not defined');
  var c = Object(this),
    b = c.length >>> 0;
  if (0 === b) return -1;
  a = +e || 0;
  Infinity === Math.abs(a) && (a = 0);
  if (a >= b) return -1;
  for (a = Math.max(0 <= a ? a : b - Math.abs(a), 0); a < b;) {
    if (a in c && c[a] === d) return a;
    a++
  }
  return -1
});
function checkCallBackFunction(fun) {
  return fun && (typeof fun === "function" || typeof fun ==="object")
}