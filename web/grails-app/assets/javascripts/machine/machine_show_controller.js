//= require machine/machine_view.js

var MachineShowController = (function(){
  
  var selectors = {
    machineShowURL:'#machineShowURL',
    machineUuid:'#machineUuid'
  },

  loadMachine = function(){
    $.ajax({
      url:$(selectors.machineShowURL).val()+".json",
      headers:{
        Accept: "application/json"
      },
      type:'GET',
      data:{id:$(selectors.machineUuid).val()},
      success: function(result){
        console.log(result);
      },
      error: function(){

      }
    });
    
  },

  start = function(){
    loadMachine();
    console.log('Starting this controller');
  };

  return {
    start:start
  };

}());

jQuery(function($){
  MachineShowController.start();
});
