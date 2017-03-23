//= require third-party/jquery-validation/dist/jquery.validate.js
//= require third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.js
//= require machine/machine_view.js

var MachineEditController = (function(){

  var selectors = {
    machineForm:'form[name=machineForm]'
  },

  machine = null,
 
  initValidations = function(){
    $(selectors.machineForm).validate({
      rules:{
        stateFrom:{
          required:true
        }
      },
      errorPlacement: function(error,element){
        $(element).parent('div').addClass('has-error');
      },
      success: function(label,element){
        $(element).parent('div').removeClass('has-error');
      }
    })
  },

  start = function(){
    initValidations();
  };

  return{
    start:start
  };

}());

jQuery(function($){
  MachineEditController.start();
});
