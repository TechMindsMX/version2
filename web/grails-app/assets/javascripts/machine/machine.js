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
    this.graph.setNode(name,{});
  },

  addTransition:function(data){
    var transition = null;

    if(this.transitions.length == 0){
      
      transition = Transition.create({stateFrom:this.getNextState(),
                                      action:data.actionName,
                                      actionId:data.actionToId,
                                      stateTo:this.getNextState()})
      this.transitions.push(transition);
    }
    else{
      var transitionOfLastAction = $.grep(this.transitions,function(transition,index){
        return transition.actionId == data.actionFromId;
      })[0];
      
      if(transitionOfLastAction != null){
        var stateFrom = transitionOfLastAction.stateTo;
        var existentTransition  = $.grep(this.transitions,function(transition,index){
          return transition.stateFrom == stateFrom && transition.actionId == data.actionToId;
        })[0];

        if(existentTransition == null){
          var stateTo = (data.actionFromId == data.actionToId) ? stateFrom : this.getNextState();

          transition = Transition.create({stateFrom:stateFrom,
                                          action:data.actionName,
                                          actionId:data.actionToId,
                                          stateTo:stateTo});
          this.transitions.push(transition)
        }
      }
      
    }

    this.addAction(data.actionToId,data.actionName);
    return transition;
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
