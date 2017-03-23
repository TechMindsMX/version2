package com.modulus.uno.machine

class TransitionCommand {
  String stateFrom
  ArrayList<String> actions
  String stateTo

  Transition getTransition(){
    Transition transition = new Transition(stateFrom:new State(name:this.stateFrom),
                                           stateTo:new State(name:this.stateTo))
    transition.actions = actions
    return transition
  }
}
