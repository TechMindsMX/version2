//= require third-party/jquery-validation/dist/jquery.validate.js
//= require helpers/machine_helpers.js
//= require machine/machine.js
//= require machine/machine_create_view.js

var MachineCreateController = (function(){
  
  var selectors = {
    actionFrom:'select[name=actionFrom]',
    actionTo:'select[name=actionTo]',
    transitionForm:'form[name=transitionForm]'
  },
  machine = null,  

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
        $(element).parent('div').removeClass("has-error");
      }
    });
  },

  addNewTransition = function(event){
    event.preventDefault();
    var form = $(event.currentTarget);
    if(form.valid()){
      var actionFromId = $(selectors.actionFrom).val(),
      actionToId = $(selectors.actionTo).val(),
      actionToName = $(selectors.actionTo + ' option:selected').text();
      var newTransition = machine.addTransition({actionFromId:actionFromId,
                                                 actionToId:actionToId,
                                                 actionName:actionToName});
      
      MachineCreateView.render(machine);
      updateFromSelect(actionToId,actionToName);
    }
  },

  updateFromSelect = function(actionId,actionName){
    var options = $(selectors.actionFrom).find('option');

    $.each(options,function(index,option){
      if($(option).val()){
        $(option).remove();
      }
    });

    $.each(machine.getActions(),function(index,action){
      $(selectors.actionFrom).append('<option value="'+action.id+'">'+action.name+'</option>')
    });

  },

  bindEvents = function(){
    $(selectors.transitionForm).on('submit',addNewTransition);
  },

  start = function(){
    machine = Machine.create();
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
