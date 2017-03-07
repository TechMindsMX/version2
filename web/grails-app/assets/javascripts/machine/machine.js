//= require machine/transition.js
//= require third-party/lodash/lodash.js
//= require third-party/graphlib/dist/graphlib.core.js
//= require third-party/dagre/dist/dagre.core.js
//= require third-party/dagre-d3/dist/dagre-d3.core.js
//= require third-party/d3/d3.js

var Machine = {
  initialState:null,
  transitions:[],
  states:[],
  graph:null,

  create:function(data){
    this.graph = new dagreD3.graphlib.Graph({multigraph: true}).setGraph({});
    return $.extend({},this,data);
  },
  
  addInitialState:function(name) {
    this.initialState = State.create({name:name.toUpperCase().trim()});
    this.addState(name);
  },

  addState:function(name){
    name = name.toUpperCase().trim();
    var state;
    state = $.grep(this.states,function(state,index){
      return state.name == name
    })[0];
    
    if(state == null || state == 'undefined'){
      state = State.create({name:name});
      this.states.push(state);
      this.graph.setNode(name,{label: name});
    }

    return state;
  },

  addTransition:function(data){
    var transition = null;
    var action = data.action.toUpperCase().trim();
    var stateFrom = $.grep(this.states,function(state,index){
      return state.name == data.stateFrom;
    })[0];

    var stateTo = $.grep(this.states,function(state,index){
      return state.name == data.stateTo;
    })[0];
    
    if(!stateTo){
      stateTo = this.addState(data.stateTo);
    } 

    this.graph.setEdge(stateFrom.name,stateTo.name, { label: action },action);
    
    var existentTransition = $.grep(this.transitions,function(transition_,index){
      return transition_.stateFrom.name == stateFrom.name && transition_.stateTo.name == stateTo.name;
    })[0];

    if(existentTransition != null){
      if(existentTransition.actions.indexOf(action) < 0){
        existentTransition.actions.push(action);
      }
    }
    else{
      transition = Transition.create({stateFrom:stateFrom,
                                      stateTo:stateTo});
      transition.actions.push(action)
      this.transitions.push(transition)
    }

  },
  
  removeTransition: function(data){
    var transitionIndex = this.findIndexOfTransition(data.stateFrom,data.stateTo);

    if(transitionIndex >= 0){
      this.transitions.splice(transitionIndex,1);
      this.removeState(data.stateTo);
      this.graph.removeEdge(data.stateFrom,data.stateTo,data.action);
    }

  },
  
  removeState:function(destinyState){
    var states = [destinyState];
    var that = this;
    while(states.length > 0){
      var currentState = states.splice(0,1); 
      
      var transitionsToDestinyState = $.grep(this.transitions,function(transition,index){
        return transition.stateTo.name === currentState;
      });
        
      if(transitionsToDestinyState.length == 0){
        var transitionsFromDestinyState = $.grep(this.transitions,function(transition,index){
          return transition.stateFrom.name == currentState;
        });

        $.each(transitionsFromDestinyState,function(index,transition){
          states.push(transition.stateTo.name);
          var transitionIndex = that.findIndexOfTransition(transition.stateFrom.name,transition.stateTo.name);
          that.transitions.splice(transitionIndex,1);

          $.each(transition.actions,function(i,action){
            that.graph.removeEdge(transition.stateFrom,transition.stateTo,action);
          });
        });
        
        var indexOfCurrentState = this.findIndexOfState(currentState);
        this.states.splice(indexOfCurrentState,1);
        this.graph.removeNode(currentState);
      }
    }
  },
  
  findIndexOfTransition:function(stateFrom,stateTo){
    var index = -1;
    for(var i=0;i<this.transitions.length;i++){
      if(this.transitions[i].stateFrom.name == stateFrom && this.transitions[i].stateTo.name == stateTo){
        index = i;
        break;
      }
    }
    return index;
  },

  findIndexOfState:function(name){
    var index = -1; 
    for(var i=0;i<this.states.length;i++){
      if(this.states[i].name === name){
        index = i;
        break;
      }
    }
    return index;
  },

  getTransitions:function(){
    return this.transitions;
  },

  getInitialState:function(){
    return this.initialState;
  },

  getStates:function(){
    return this.states;
  },

  getGraph:function(){
    return this.graph;
  }

};
