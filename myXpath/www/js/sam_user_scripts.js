(function()
{
 "use strict";
 /*
   hook up event handlers 
 */
 function register_event_handlers()
 {
    
    
     /* graphic button  Continue */
    $(document).on("click", ".uib_w_4", function(evt)
    {
        
       
        $("#phon").val(device.model);
         activate_subpage("#uib_page_1"); 
        $("#phon").val(device.model);
    });
    
    }
 document.addEventListener("app.Ready", register_event_handlers, false);
})();
