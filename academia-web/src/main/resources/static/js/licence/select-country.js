$(document).ready(function () {  
    $('#pays').bind('change', function () { 
       selectCountry($('#pays').val()) ; 
    }) ;
    
    function selectCountry(country) {  
    	alert('cameroun') ;
        if (country.localeCompare('Cameroun') {  
            $('#indicateur').val('+237') ;
        } else  if (country.localeCompare('Gabon') {  
        	$('#indicateur').val('+241') ;
        } else  if (country.localeCompare('Congo') {  
        	$('#indicateur').val('+242') ;
        } else  if (country.localeCompare('RCA') {  
        	$('#indicateur').val('+236') ;
        } else  if (country.localeCompare('Tchad') {  
        	$('#indicateur').val('+235') ;
        } 
    }  
    
});  