//= require machine/transition.js

var Machine = {
  initialState:null,
  transitions:[],
  actions:[],
  states:[],

  create:function(data){
    this.actions.push({id:0,name:'Inicio'});
    return $.extend({},this,data);
  },
  
  addInitialState:function(name) {
    this.initialState = State.create({name:name});
    this.addState(name);
  },

  addState:function(name){
    var state = State.create({name:name});
    this.states.push(state);
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
  }

};
