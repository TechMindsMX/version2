//= require machine/machine.js
//= require machine/machine_view.js

var MachineListController = (function(){

  var selectors = {
    entitySelector:'select[name=entity]',
    actionListURL:'#actionListURL',
    machineListDiv:'#machine-list',
    machineListTemplate:'#machine-list-template'
  },

  showMachines = function(event){
    var select = $(event.currentTarget);
    var machines = [];
    $.get($(selectors.actionListURL).val(),{className:select.val()},function(data){
      data.forEach(function(machine){
        machines.push(machine); 
      });

      MachineView.render(selectors.machineListTemplate,selectors.machineListDiv,{machines:machines});
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
