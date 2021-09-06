$(document).ready(function () {  
    $('#password').bind('change', function () {  
       checkStrength($('#password').val()) ; 
       checkPasswords($('#password').val(), $('#confirmPwd').val()) ; 
    }) 
    
    $('#confirmPwd').bind('change', function () {  
        checkPasswords($('#password').val(), $('#confirmPwd').val()) ; 
     })  
   
    
    function checkStrength(password) {  
        var strength = 0  ;
        if (password.length >= 8) {  
            strength += 1 ;
        } 
        // If password contains both lower and uppercase characters, increase strength value.  
        if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/)) strength += 1  ;
        // If it has numbers and characters, increase strength value.  
        if (password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) strength += 1  ;
        // If it has one special character, increase strength value.  
        if (password.match(/([!,%,&,@,#,$,^,*,?,_,~])/)) strength += 1  ;
        if (strength >= 4) {  
            $('#password').removeClass('is-invalid')  ;
            $('#password').addClass('is-valid')  ;
        } else {  
            $('#password').removeClass('is-valid')  ;
            $('#password').addClass('is-invalid')  ;
        } 
    }  
    
    function checkPasswords(password, confirmPwd) {  
        if (password.localeCompare(confirmPwd) == 0) {  
            $('#confirmPwd').removeClass('is-invalid')  ;
            $('#confirmPwd').addClass('is-valid')  ;
        } else {  
            $('#confirmPwd').removeClass('is-valid')  ;
            $('#confirmPwd').addClass('is-invalid')  ;
        } 
    }  
    
});  