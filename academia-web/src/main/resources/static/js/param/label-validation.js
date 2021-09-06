$( document ).ready(function() {
	
    $("#libelle").bind('change', function (event){
    	alert(1) ;
    	var libelle = $("#libelle").val() ;
    	if(libelle == undefined || libelle == null) {
    		$("#libelle").addClass('is-valid') ;
			$("#libelle").removeClass('is-invalid') ;
			
    	} else {
    		$("#libelle").addClass('is-invalid') ;
			$("#libelle").removeClass('is-valid') ;
    	}
     
    } );

});

