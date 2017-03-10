//= require machine/machine.js

var MachineListController = (function(){

  var selectors = {
    entitySelector:'select[name=entity]',
    actionListURL:'#actionListURL'
  },

  showMachines = function(event){
    var select = $(event.currentTarget);

    $.get($(selectors.actionListURL).val(),{className:select.val()},function(data){
      
    });

  },

  bindEvents = function(){
    $(selectors.entitySelector).on('change',showMachines);
  },

  start = function(){
    bindEvents();
  };

  return {
    start:start
  };
  
}());

jQuery(function($){
  MachineListController.start();
});
