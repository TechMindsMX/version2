//= require machine/machine.js
//= require machine/machine_view.js

var MachineListController = (function(){

  var selectors = {
    entitySelector:'select[name=entity]',
    actionListURL:'#actionListURL',
    machineListDiv:'#machine-list',
    machineListTemplate:'#machine-list-template',
    entitySelect:'select[name=entity]',
    companySelect:'select[name=company]',
    searchMachinesButton:'#searchMachinesButton'
  },

  showMachines = function(event){
    var entitySelect = $(selectors.entitySelect),
    companySelect = $(selectors.companySelect);

    var machines = [];
    
    $.get($(selectors.actionListURL).val(),{className:entitySelect.val(),
                                            company:companySelect.val()},function(data){
      data.forEach(function(machine){
        machines.push(machine);
      });

      MachineView.render(selectors.machineListTemplate,selectors.machineListDiv,{machines:machines});
    });
  },
  
  bindEvents = function(){
    $(selectors.searchMachinesButton).on('click',showMachines);
  },

  start = function(){
    bindEvents();
    $(selectors.entitySelector).trigger('change');
  };

  return {
    start:start
  };
  
}());

jQuery(function($){
  MachineListController.start();
});
