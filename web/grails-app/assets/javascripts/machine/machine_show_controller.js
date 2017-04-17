//= require machine/machine.js
//= require machine/machine_view.js
//= require machine/graph_view.js

var MachineShowController = (function(){
  
  var selectors = {
    machineShowURL:'#machineShowURL',
    machineUuid:'#machineUuid'
  },
  machine = null,

  loadMachine = function(){
    $.ajax({
      url:$(selectors.machineShowURL).val()+".json",
      headers:{
        Accept: "application/json"
      },
      type:'GET',
      data:{id:$(selectors.machineUuid).val()},
      success: function(result){
        if(result.transitions.length > 0){
          machine.addInitialState(result.transitions[0].stateFrom.name); 

          result.transitions.forEach(function(transition){
            transition.actions.forEach(function(action){
              machine.addTransition({stateFrom:transition.stateFrom.name,
                                     stateTo:transition.stateTo.name,
                                     action:action.name});
            });
          });
        }

        GraphView.renderGraph(machine.getGraph());
      },
      error: function(data){
        console.log(data);
      }
    });
  },

  start = function(){
    machine = Machine.create();
    GraphView.initView();
    loadMachine();
  };

  return {
    start:start
  };

}());

jQuery(function($){
  MachineShowController.start();
});
