//= require third-party/jquery-validation/dist/jquery.validate.js

var MachineRegisterController = (function(){

  var selectors = {
    entityInput:'input[name=entity]',
    registerForm:'form[name=registerForm]'
  },

  initValidations = function(){
    $(selectors.registerForm).validate({
      rules:{
        entity:{
          required:true
        },
        company:{
          required:true
        }
      },

      errorPlacement: function(error,element){
        $(element).parent('div').addClass('has-error');
      },

      success: function(label,element){
        $(element).parent('div').removeClass('has-error');
      }
    });
  },

  start = function(){
    initValidations();
  };

  return {
    start:start
  };

}());

jQuery(function($){
  MachineRegisterController.start();
});
