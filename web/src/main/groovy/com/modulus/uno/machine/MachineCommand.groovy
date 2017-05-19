package com.modulus.uno.machine

import grails.validation.Validateable

class MachineCommand implements Validateable {
  String initialState
  ArrayList<TransitionCommand> transitions;

  Machine getMachine(){
    Machine machine = new Machine(initialState:new State(name:this.initialState)) 
    ArrayList<Transition> machineTransitions = []

    this.transitions.each{ transition ->
      machineTransitions << transition.getTransition()
    }

    machine.transitions = machineTransitions
    return machine
  } 

}
