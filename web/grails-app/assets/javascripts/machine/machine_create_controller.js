//= require third-party/jquery-validation/dist/jquery.validate.js
//= require third-party/d3/d3.js
//= require helpers/machine_helpers.js
//= require machine/machine.js
//= require machine/machine_create_view.js

var MachineCreateController = (function(){

  var selectors = {
    action:'input[name=action]',
    machineForm:'form[name=machineForm]',
    stateFrom:'input[name=stateFrom],select[name=stateFrom]',
    stateTo:'input[name=stateTo]',
    transitionsDiv:'#transitionsDiv'
  },
  machine = null,  
  svg = null,
  inner = null,
  render = null,

  initValidations = function(){
    $(selectors.machineForm).validate({
      rules:{
        stateFrom:{
          required:true
        }
      },
      errorPlacement: function(error,element){
        $(element).parent('div').addClass("has-error");
      },
      success: function(label,element){
        $(element).parent('div').removeClass("has-error");
      }
    });
  },

  addNewRules = function(){
    $(selectors.stateTo).rules("add",{required:true});
    $(selectors.action).rules("add",{required:true});
  },

  addNewTransition = function(event){
    event.preventDefault();
    var form = $(event.currentTarget);

    if(form.valid()){
      if(machine.getStates().length == 0){
        machine.addInitialState($(selectors.stateFrom).val());
        MachineCreateView.render('#transitions-form-template','#transitionsDiv',{states:machine.getStates()});
        addNewRules();
      }
      else{
        machine.addTransition({stateFrom:$(selectors.stateFrom).val(),
                               stateTo:$(selectors.stateTo).val(),
                               action:$(selectors.action).val()});
      }

      updateFromSelect();
    }

    renderGraph(machine.getGraph());
  },

  updateFromSelect = function(){
    var options = $(selectors.stateFrom).find('option');

    $.each(options,function(index,option){
      if($(option).val()){
        $(option).remove();
      }
    });

    console.log(machine.getStates());
    $.each(machine.getStates(),function(index,state){
      $(selectors.stateFrom).append('<option value="'+state.name+'">'+state.name+'</option>')
    });

  },
  
  createInitialState = function(event){
    event.preventDefault();
    machine.addInitialState($(selectors.stateFrom).val());
    MachineCreateView.render('#transitions-form-template','#machineCreationDiv',{states:machine.getStates()});
  },

  bindEvents = function(){
    $(selectors.machineForm).on('submit',addNewTransition);
  },

  renderGraph = function(graph){
    render(inner, graph);
    var xCenterOffset = ($('svg').width() - graph.graph().width) / 2;
    inner.attr("transform", "translate(" + xCenterOffset + ", 20)");
  },

  start = function(){
    machine = Machine.create();
    svg = d3.select("svg");
    inner = svg.append("g");
    render = new dagreD3.render();
    initValidations();
    bindEvents();
  };

  return {
    start:start
  };

}());

jQuery(function($){
  MachineCreateController.start();
});
