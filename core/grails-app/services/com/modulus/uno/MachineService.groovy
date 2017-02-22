package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineService {

  TrackingService trackingService
 
  Machine createMachineWithAction(Action action){
    State initialState = new State()
    State finalState = new State(finalState:true)
    Machine machine = new Machine(initialState:initialState)
    machine.addToStates(initialState)
    machine.addToStates(finalState)
    machine.save()
    Transition transition = new Transition(action:action,
                                           stateTo:finalState)
    
    initialState.addToTransitions(transition)
    initialState.save()
    machine
  }

  Machine createTransition(Long machineId,Long actionBeforeId,Long newActionId){
    Machine currentMachine = Machine.get(machineId)
    Action newAction = Action.get(newActionId)

    def criteria = Transition.createCriteria()

    Transition transition = criteria.get {
      action{
        eq("id",actionBeforeId)
      }

      state{
        machine{
          eq("id",machineId)
        }
      }
    }

    State newState = addTransitionToState(transition.stateTo.id,newAction)
    currentMachine.addToStates(newState)
    currentMachine.save()
    currentMachine
  }

  State addTransitionToState(Long originStateId,Action action){
    State state = State.get(originStateId)
    state.finalState= false
    State newState = new State(finalState:true)
    Transition newTransition = new Transition(action:action,
                                              stateTo:newState,
                                              state:state)
    newTransition.save()
    newState
  }

  def moveToAction(def instance,Action action){
    MachineryLink machineryLink = MachineryLink.findByCompanyRefAndType(instance.id,instance.class.simpleName)
    Machine machine = machineryLink.machine
    State state = trackingService.findLastTrackingLogRecord(instance)?.state

    if(!state)
      state = machine.initialState 

    Transition transition = state.transitions.find{ transition -> transition.action.id == action.id }

    if(!transition)
      throw new StatelessException("There is n't a transition for the action ${action.name}.")

    State newState = transition.stateTo
    trackingService.addRecordToInstanceLog(instance,newState.id)
    newState
  }

}
