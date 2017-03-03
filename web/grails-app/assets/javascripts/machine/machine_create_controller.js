//= require third-party/jquery-validation/dist/jquery.validate.js
//= require third-party/EasyAutocomplete/dist/jquery.easy-autocomplete.js
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

  addNewTransition = function(event){
    event.preventDefault();

    var form = $(event.currentTarget);

    if(form.valid()){
      machine.addTransition({stateFrom:$(selectors.stateFrom).val(),
                             stateTo:$(selectors.stateTo).val(),
                             action:$(selectors.action).val()});
      updateFromSelect();
    }

    updateAutocomplete();
    renderGraph(machine.getGraph());
    renderTransitionsTable();
  },

  updateFromSelect = function(){
    var options = $(selectors.stateFrom).find('option');

    $.each(options,function(index,option){
      if($(option).val()){
        $(option).remove();
      }
    });

    $.each(machine.getStates(),function(index,state){
      $(selectors.stateFrom).append('<option value="'+state.name+'">'+state.name+'</option>')
    });

    $.each($(selectors.machineForm).find("input[type=text],select"),function(index,input){
      $(input).val('');
    });
  },
  
  createInitialState = function(event){
    event.preventDefault();
    machine.addInitialState($(selectors.stateFrom).val());
    MachineCreateView.render('#transitions-form-template','#transitionsDiv',{states:machine.getStates()});
    addNewRules();
    $(selectors.machineForm).unbind('submit');
    $(selectors.machineForm).on('submit',addNewTransition);
    updateAutocomplete();
    renderGraph(machine.getGraph());
  },

  bindEvents = function(){
    $(selectors.machineForm).on('submit',createInitialState);
  },

  renderGraph = function(graph){
    render(inner, graph);
    var center = ($('svg').width() - graph.graph().width) / 2;
    inner.attr("transform", "translate(" + center + ", 20)");
    svg.attr("height", graph.graph().height + 40);
  },

  renderTransitionsTable = function(){
    MachineCreateView.render('#transitionsTable','#transitionsTableContainer',{transitions:machine.getTransitions()});
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
