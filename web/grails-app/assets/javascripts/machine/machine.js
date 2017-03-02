//= require machine/transition.js
//= require third-party/lodash/lodash.js
//= require third-party/graphlib/dist/graphlib.core.js
//= require third-party/dagre/dist/dagre.core.js
//= require third-party/dagre-d3/dist/dagre-d3.core.js
//= require third-party/d3/d3.js

var Machine = {
  initialState:null,
  transitions:[],
  actions:[],
  states:[],
  graph:null,

  create:function(data){
    this.actions.push({id:0,name:'Inicio'});
    this.graph = new dagreD3.graphlib.Graph().setGraph({});
    return $.extend({},this,data);
  },
  
  addInitialState:function(name) {
    this.initialState = State.create({name:name});
    this.addState(name);
  },

  addState:function(name){
    var state = State.create({name:name});
    this.states.push(state);
    this.graph.setNode(name.toUpperCase(),{label :name.toUpperCase()});

    return state;
  },

  addTransition:function(data){
    var transition = null;
    var action = data.action.toUpperCase();
    var stateFrom = $.grep(this.states,function(state,index){
      return state.name == data.stateFrom;
    })[0];

    var stateTo = $.grep(this.states,function(state,index){
      return state.name == data.stateTo;
    })[0];
    
    if(!stateTo){
      stateTo = this.addState(data.stateTo);
    } 

    this.graph.setEdge(stateFrom.name.toUpperCase(),stateTo.name.toUpperCase(), { label: data.action });
    
    var existentTransition = $.grep(this.transitions,function(transition,index){
      return transition.stateFrom == stateFrom.name && transition.stateTo == stateTo.name;
    })[0];
 
    if(existentTransition != null){
      if(existentTransition.actions.indexOf(action) >= 0){
        existentTransition.actions.push(action);
      }
    }

  },

  addAction: function(idAction,name){
    var existentAction = $.grep(this.actions,function(action,index){
      return action.id == idAction;
    });

    if(existentAction.length == 0){
      this.actions.push({id:idAction,name:name});
    }
  },

  getTransitions:function(){
    return this.transitions;
  },

  getStates:function(){
    return this.states;
  },

  getGraph:function(){
    return this.graph;
  }

};
