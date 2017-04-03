//= require machine/machine.js
//= require machine/machine_view.js
//= require third-party/jquery-validation/dist/jquery.validate.js

var MachineListController = (function(){

  var selectors = {
    entitySelector:'select[name=entity]',
    actionListURL:'#actionListURL',
    machineListDiv:'#machine-list',
    machineListTemplate:'#machine-list-template',
    entitySelect:'select[name=entity]',
    companySelect:'select[name=company]',
    searchMachinesButton:'#searchMachinesButton',
    machineSearchForm:'form[name=machineSearch]'
  },

  initValidations = function(){
    $(selectors.machineSearchForm).validate({
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
      
      success:function(label,element){
        $(element).parent('div').removeClass('has-error');
      }

    });
  },

  showMachines = function(event){
    event.preventDefault();

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
    $(selectors.machineSearchForm).on('submit',showMachines);
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
  MachineListController.start();
});
