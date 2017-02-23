//= require third-party/jquery-validation/dist/jquery.validate.js
//= require machine/machine.js

var MachineCreateController = (function(){
  
  var selectors = {
    actionFrom:'select[name=actionFrom]',
    actionTo:'select[name=actionTo]',
    transitionForm:'form[name=transitionForm]'
  },
  
  initValidations = function(){
    $(selectors.transitionForm).validate({
      rules:{
        actionFrom:{
          required:true
        },
        actionTo:{
          required:true
        }
      },
      errorPlacement: function(error,element){
        $(element).parent('div').addClass("has-error");
      },
      success: function(label,element){
        $(element).parent('div').addClass("has-error");
      }
    });
  },

  addNewTransition = function(event){
    event.preventDefault();
    var form = $(event.currentTarget);
    if(form.valid()){
      console.log('True');
    }
    else{
      console.log('False');
    }
    console.log(form);
  },

  bindEvents = function(){
    $(selectors.transitionForm).on('submit',addNewTransition);
  },

  start = function(){
    initValidations();
    bindEvents();
  };

  return {
    start:start
  };

}());

jQuery(function($){
  MachineCreateController.start();
});
