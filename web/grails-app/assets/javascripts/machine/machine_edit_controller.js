//= require third-party/jquery-validation/dist/jquery.validate.js
//= require third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.js
//= require machine/machine.js
//= require machine/machine_view.js
//= require machine/graph_view.js
//= require machine/machine_controller.js

var MachineEditController = (function(){

  var selectors = {
    stateFrom:'select[name=stateFrom]',
    stateTo:'input[name=stateTo]',
    action:'input[name=action]',
    machineForm:'form[name=machineForm]',
    machineUuid:'#machineUuid',
    transitionsTableContainer:'#transitionsTableContainer',
    deleteTransition:'.delete-transition'
  },

  updateAutocomplete = function(){
    var stateNames = [];
    $.each(machine.getStates(),function(index,state){
      stateNames.push(state.name);
    });
    
    var options = {data:stateNames,theme:'blue-light'};
    $(selectors.stateTo).easyAutocomplete(options);
  },

  initValidations = function(){
    $(selectors.machineForm).validate({
      rules:{
        stateFrom:{
          required:true
        },
        stateTo:{
          required:true
        },
        action:{
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
  
  bindEvents = function(){
    $(selectors.machineForm).on('submit',MachineController.addNewTransition);
    $(selectors.transitionsTableContainer).on('click',selectors.deleteTransition,MachineController.deleteMachineTransition);
  },

  start = function(){
    MachineController.start();
    initValidations();
    bindEvents();
    MachineController.loadMachine();
  };

  return{
    start:start
  };

}());

jQuery(function($){
  MachineEditController.start();
});
