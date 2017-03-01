package com.modulus.uno.machine

import grails.transaction.Transactional

@Transactional
class MachineService {

  Machine createMachineWithAction(String startName,String stateToName,String action){
    Machine machine = new Machine()
    State initialState = new State(name:startName)
    State finalState = new State(name:stateToName,
                                 finalState:true)
    machine.addToStates(initialState)
    machine.addToStates(finalState)
    machine.save()

    Transition transition = new Transition(stateFrom:initialState,
                                           stateTo:finalState)
    transition.addToActions(action)

    machine.initialState = initialState
    machine.addToTransitions(transition)
    machine.save()
    machine
  }

  Machine createTransition(Long stateFromId,String stateToName,String newAction){
    State stateOrigin = State.get(stateFromId)
    Machine machine = stateOrigin.machine

    stateOrigin.finalState = false

    ArrayList<Transition> stateFromTransitions = Transition.where{
      stateFrom.id == stateOrigin.id
    }.list()

    State newState = machine.states.find{ state -> state.name == stateToName } ?: new State(name:states.stateTo)

    if(!stateFromTransitions){
      newState.finalState = true
    }

    machine.addToStates(newState)
    machine.save()

    def criteria = Transition.createCriteria()

    Transition newTransition = criteria.get{
      stateFrom{
        eq("id",stateOrigin.id)
      }
      
      stateTo{
        eq("id",newState.id)
      }
    } ?: new Transition(stateFrom:stateOrigin,stateTo:newState)
   
    if(!newTransition.actions.contains(newAction)){
      newTransition.addToActions(action)
      newTransition.save()
    }

    machine.addToTransitions(newTransition)
    machine.save()
    machine 
  }

  State moveToAction(def instance,String action){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    Machine machine = machineryLink.machine

    State state = getCurrentStateOfInstance(instance)

    if(!state)
      state = machine.initialState
    
    Transition transition = machine.transitions.find{ transition -> transition.action.id == action.id && transition.stateFrom.id == state.id }

    if(!transition)
      throw new StatelessException("There is n't a transition for the action ${action}.")

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
