package com.modulus.uno.machine

import grails.transaction.Transactional

@Transactional
class TransitionService {

  ArrayList<Transition> getMachineTransitions(Long machineId){
    Machine machine = Machine.get(machineId)
    State initialState = machine.initialState
    ArrayList<Transition> initialStateTransitions = machine.transitions.findAll{ transition -> transition.stateFrom.id == initialState.id }
    ArrayList<Transition> orderedTransitions = initialStateTransitions += (machine.transitions.findAll{ transition -> transition.stateFrom.id != initialState.id }.sort{ it.stateFrom.id })
    orderedTransitions
  }

}
