//= require machine/transition.js
//= require third-party/lodash/lodash.js
//= require third-party/graphlib/dist/graphlib.core.js
//= require third-party/dagre/dist/dagre.core.js
//= require third-party/dagre-d3/dist/dagre-d3.core.js
//= require third-party/graphlib-dot/dist/graphlib-dot.core.js
//= require third-party/d3/d3.js

var Machine = {
  initialState:null,
  transitions:[],
  states:[],

  create:function(data){
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
      
      if(this.transitions[transitionIndex].actions.length > 1){
        var actionIndex = this.transitions[transitionIndex].actions.indexOf(data.action);
        this.transitions[transitionIndex].actions.splice(actionIndex,1);
      }
      else
        this.transitions.splice(transitionIndex,1);

      if(data.stateTo != this.initialState.name)
        this.removeState(data.stateTo);
    }

  },
  
  removeState:function(destinyState){
    var states = [destinyState];
    var that = this;

    while(states.length > 0){
      var currentState = states.splice(0,1)[0]; 

      var transitionsToDestinyState = $.grep(this.transitions,function(transition,index){
        return transition.stateTo.name == currentState;
      });
        
      if(transitionsToDestinyState.length == 0){
        var transitionsFromDestinyState = $.grep(this.transitions,function(transition,index){
          return transition.stateFrom.name == currentState;
        });

        $.each(transitionsFromDestinyState,function(index,transition){
          if(transition.stateTo.name!= that.initialState.name)
            states.push(transition.stateTo.name);

          var transitionIndex = that.findIndexOfTransition(transition.stateFrom.name,transition.stateTo.name);
          that.transitions.splice(transitionIndex,1);
        });
        
        var indexOfCurrentState = this.findIndexOfState(currentState);
        this.states.splice(indexOfCurrentState,1);
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
    var digraphText = "digraph { node [rx=5 ry=5 labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] " +
                      "edge [labelStyle=\"font: 300 14px 'Helvetica Neue', Helvetica\"] ";

    $.each(this.states,function(index,state){
      digraphText += "\""+state.name + "\"; ";
    });
    
    $.each(this.transitions,function(i,transition){
      $.each(transition.actions,function(j,action){
        digraphText += "\""+transition.stateFrom.name + "\" -> \"" +transition.stateTo.name + "\"";
        digraphText += " [labelType=\"html\" label=\"<span>"+action+"</span>\"]; ";
      });
    });

    digraphText += "}";

    return graphlibDot.read(digraphText);
  },

  deserialize: function(data){
    var _self = Machine.create({});
    
    Object.keys(_self).forEach(function(key){
      if(typeof _self[key] == 'function'){
        _self[key] = data[key];
      }
    });

    return _self;
  }

};
