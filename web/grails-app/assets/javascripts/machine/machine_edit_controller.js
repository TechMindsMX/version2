//= require third-party/jquery-validation/dist/jquery.validate.js
//= require third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.js
//= require machine/machine.js
//= require machine/machine_view.js
//= require machine/graph_view.js

var MachineEditController = (function(){

  var selectors = {
    machineForm:'form[name=machineForm]',
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
      success:function(result){
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

  start = function(){
    machine = Machine.create();
    GraphView.initView();
    initValidations();
    loadMachine();
  };

  return{
    start:start
  };

}());

jQuery(function($){
  MachineEditController.start();
});
