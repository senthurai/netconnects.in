(function()
{
 "use strict";
 /*
   hook up event handlers 
 */
 function register_event_handlers()
 {
    
    
     /* button  Cancel */
    $(document).on("click", ".uib_w_4", function(evt)
    {
         activate_page("#mainpage"); 
    });
    
        /* button  Button */
    
    
    }
 document.addEventListener("app.Ready", register_event_handlers, false);
})();
