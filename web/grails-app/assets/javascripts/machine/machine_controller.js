//= require third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.js

var MachineController = (function(){

  var selectors = {
    action:'input[name=action]',
    stateFrom:'input[name=stateFrom],select[name=stateFrom]',
    stateTo:'input[name=stateTo]',
    machineForm:'form[name=machineForm]',
    machineUuid:'#machineUuid',
    transitionsTableContainer:'#transitionsTableContainer',
    machineShowURL:'#machineShowURL'
  },
  machine = null,

  updateAutocomplete = function(){
    var stateNames = [];
    $.each(machine.getStates(),function(index,state){
      stateNames.push(state.name);
    });

    var options = {
      data:stateNames,
      theme:'blue-light'
    };

    $(selectors.stateTo).easyAutocomplete(options);
  },

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
        updateAutocomplete();
      },
      error: function(data){
        console.log(data);
      }
    });
  },

  updateFromSelect = function(){
    var options = $(selectors.stateFrom).find('option');
    $.each(options,function(index,option){
      if($(option).val()){
        $(option).remove();
      }
    });

    $.each(machine.getStates(),function(index,state){
      $(selectors.stateFrom).append('<option value="'+state.name+'">'+state.name+'</option>');
    });

    $.each($(selectors.machineForm).find('input[type=text],select'),function(index,input){
      $(input).val('');
    });
  },

  addNewTransition = function(event){
    event.preventDefault();

    var form = $(event.currentTarget);

    if(form.valid()){
      machine.addTransition({stateFrom:$(selectors.stateFrom).val(),
                             stateTo:$(selectors.stateTo).val(),
                             action:$(selectors.action).val()});
      updateFromSelect();
    }
  },

  start = function(){
    machine = Machine.create();
    GraphView.initView();
  };

  return{
    addNewTransition:addNewTransition,
    loadMachine:loadMachine,
    start:start
  };

}());
