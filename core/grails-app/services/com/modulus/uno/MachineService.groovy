package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class MachineService {

  Machine createMachineWithAction(Action action){
    Machine machine = new Machine()
    State initialState = new State()
    State finalState = new State(finalState:true)
    machine.addToStates(initialState)
    machine.addToStates(finalState)
    machine.save()

    Transition transition = new Transition(action:action,
                                           stateFrom:initialState,
                                           stateTo:finalState)

    machine.initialState = initialState
    machine.addToTransitions(transition)
    machine.save()
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

      stateFrom{
        machine{
          eq("id",machineId)
        }
      }
    }

    State lastState = transition.stateTo
    lastState.finalState = false

    ArrayList<Transition> lastStateTransitions = Transition.where{
      stateFrom.id == lastState.id
    }.find()

    State newState = new State()

    if(!lastStateTransitions){
      newState.finalState = true    
    }

    currentMachine.addToStates(newState)
    currentMachine.save()

    Transition newTransition = new Transition(action:newAction,
                                              stateFrom:lastState, 
                                              stateTo:newState)

    currentMachine.addToTransitions(newTransition)
    currentMachine.save()
    currentMachine
  }

  State moveToAction(def instance,Action action){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    Machine machine = machineryLink.machine

    State state = getCurrentStateOfInstance(instance)

    if(!state)
      state = machine.initialState
    
    Transition transition = machine.transitions.find{ transition -> transition.action.id == action.id && transition.stateFrom.id == state.id }

    if(!transition)
      throw new StatelessException("There is n't a transition for the action ${action.name}.")

    State newState = transition.stateTo
    TrackingLog trackingLog = new TrackingLog(state:newState)
    machineryLink.addToTrackingLogs(trackingLog)
    machineryLink.save(failOnError:true)
    newState
  }

  State getCurrentStateOfInstance(def instance){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    machineryLink.trackingLogs?.max{ trackingLog -> trackingLog.id }?.state
  }

  ArrayList<State> findNextStatesOfInstance(def instance){
    State currentState = getCurrentStateOfInstance(instance)
    def criteria = Transition.createCriteria()

    ArrayList<Transition> transitions = criteria.list{
      stateFrom{
        eq("id",currentState.id)
      }
    }

    transitions*.stateTo
  }

}
