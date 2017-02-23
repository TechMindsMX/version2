//= require machine/transition.js

var Machine = {
  transitions:[],
  actions:[],
  states:[],

  create:function(data){
    this.actions.push({id:0,name:'Inicio'});
    return $.extend({},this,data);
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
        var stateTo = this.getNextState();
        console.log('InitialState');
        console.log(stateTo); 
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

  getActions:function(){
    return this.actions;
  },
  
  getMaxState:function(){
    var max = 0;
    $.each(this.states,function(index,state){
      if(max < state)
        max = state;
    });
    return max;
  },

  getNextState:function(){
    var nextState = this.getMaxState()+1;
    this.states.push(nextState);
    return nextState;
  }

};
