   var bacApp = angular.module("BaccG");
   bacApp.component('bactiles', {
     templateUrl: 'common/tiles.html',
     controller: tilesController,
     bindings: {
       data: '<',
       ignrSlctn: '<'
     }
   });

   function tilesController() {
     ctrl = this;

     isNtrl = function(p13, p23, p1, p2) {
       return ((!p13 && !p23) && (p1 > p2) && p1 > 7 && p1 != p2);
     };
     isDrgn = function(p13, p23, p1, p2) {
       return ((!(!p13 && !p23)) && ((p13 || p23) && (p1 - p2 > 3))); // drag
     };
     ctrl.isBnkD = function($ctrl) {
       if (!$ctrl) {
         return false;
       }
       p13 = $ctrl.p3
       p23 = $ctrl.b3
       b = $ctrl.bnkrTtlVl;
       p = $ctrl.plyrTtlVl;
       var b3 = p13 ? p13 : false;
       var p3 = p23 ? p23 : false;
       return isNtrl(b3, p3, b, p) || isDrgn(b3, p3, b, p); // drag
     }
     ctrl.isPlrD = function($ctrl) {
       if (!$ctrl) {
         return false;
       }
       p13 = $ctrl.p3
       p23 = $ctrl.b3
       b = $ctrl.bnkrTtlVl;
       p = $ctrl.plyrTtlVl;
       var b3 = p13 ? p13 : false;
       var p3 = p23 ? p23 : false;
       return isNtrl(p3, b3, p, b) || isDrgn(p3, b3, p, b);
     }
     ctrl.isHighDiff = function($ctrl) {
       if (!$ctrl) {
         return false;
       }
       p13 = $ctrl.p3
       p23 = $ctrl.b3
       b = $ctrl.bnkrTtlVl;
       p = $ctrl.plyrTtlVl;
       var b3 = p13 ? p13 : false;
       var p3 = p23 ? p23 : false;
       return ((!isNtrl(p3, b3, p, b)) && ((p - b > 8) || (p - b < -8))); // drag;
     }
     ctrl.getDragon = function($ctrl) {
       if (!$ctrl) {
         return "";
       }
       if (ctrl.isPlrD($ctrl)) {
         if (ctrl.isHighDiff($ctrl)) {
           return "dragon";
         }
         return "plrD";

       }
       if (ctrl.isBnkD($ctrl)) {
         if (ctrl.isHighDiff($ctrl)) {
           return "dragon";
         }
         return "bnkD";
       }
       return "";
     }

     function getTileFor(position) {
       switch (postion) {
         case 1:
           break;
       }
     }
   }