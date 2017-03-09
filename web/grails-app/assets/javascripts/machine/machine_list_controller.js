//= require machine/machine.js

var MachineListController = (function(){
  
  var selectors = {
    entitySelector:'select[name=entity]'
  },

  showMachines = function(){
    
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
