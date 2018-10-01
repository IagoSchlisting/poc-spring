$(function() {

    $('#login-form-link').click(function(e) {  changeScreen(100, 'login');  });
    $('#register-form-link').click(function(e) { changeScreen(100, 'register'); });

    // if(window.error){ changeScreen(0, 'register'); }

    function changeScreen(delay, goto){
        $("#"+goto+"-form").delay(delay).fadeIn(delay);
        var other = goto == 'register' ? 'login' : 'register';
        $("#"+other+"-form").fadeOut(delay);
        $("#"+other+"-form").removeClass('active');
        $(this).addClass('active');
        e.preventDefault();
    }

});
