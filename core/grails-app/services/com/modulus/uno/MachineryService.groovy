package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineryService {
 
  Machinery createMachineWithAction(Action action){
    State initialState = new State()
    State finalState = new State(isFinalState:true)
    Machinery machinery = new Machinery(initialState:initialState)
    machinery.addToStates(initialState)
    machinery.addToStates(finalState)
    machinery.save()
    Transition transition = new Transition(action:action,
                                           stateTo:finalState)

    initialState.addToTransitions(transition)
    initialState.save()
    machinery
  }

  Machinery createTransition(Long machineryId,Long actionBeforeId,Long newActionId){
    Machinery currentMachinery = Machinery.get(machineryId)
    Action newAction = Action.get(newActionId)

    def criteria = Transition.createCriteria()

    Transition transition = criteria.get {
      action{
        eq("id",actionBeforeId)
      }

      state{
        machinery{
          eq("id",machineryId)
        }
      }
    }

    State newState = addTransitionToState(transition.stateTo.id,newAction)
    currentMachinery.addToStates(newState)
    currentMachinery.save()
    currentMachinery
  }

  State addTransitionToState(Long originStateId,Action action){
    State state = State.get(originStateId)
    state.isFinalState = false
    State newState = new State(isFinalState:true)
    Transition newTransition = new Transition(action:action,
                                              stateTo:newState,
                                              state:state)
    newTransition.save()
    newState
  }

}
