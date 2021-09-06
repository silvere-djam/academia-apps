$( document ).ready(function() {
	
	$("#loginExiste").hide() ;
	
    $("#login").bind('change', function (event){
    	var login = event.target.value ;    	
    	var url = '/ws/utilisateur/web/login/'+login ;
    	type: 'GET',
    	$.ajax({url: url, 
    		       data :{format:'json'}, 
    			   success: function(result){
    				if(result == true) {
    					//$("#loginExiste").show() ;
    					$("#login").addClass('is-invalid') ;
    					$("#login").removeClass('is-valid') ;
    				}else {
    					//$("#loginExiste").hide() ;
    					$("#login").addClass('is-valid') ;
    					$("#login").removeClass('is-invalid') ;
    				}
    	 		}
    		}
    	);
     
    } );

});

