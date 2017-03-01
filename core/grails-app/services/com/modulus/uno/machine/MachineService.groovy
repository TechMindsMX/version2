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
    Machine currentMachine = stateOrigin.machine

    ArrayList<Transition> stateOriginTransitions = Transition.where{
      stateFrom.id == stateOrigin.id
    }.list()

    State newState = currentMachine.states.find{ state -> state.name == stateToName } ?: new State(name:stateToName)

    if(!stateOriginTransitions || stateOriginTransitions.findAll{ it.stateTo.finalState } ){
      stateOrigin.finalState = false
      newState.finalState = true
    }

    if(!newState.id)
      currentMachine.addToStates(newState)
    else
      newState.save()
      
    currentMachine.save()

    def criteria = Transition.createCriteria()

    Transition newTransition = criteria.get{
      machine{
        eq("id",currentMachine)
      }

      stateFrom{
        eq("id",stateOrigin.id)
      }
      
      stateTo{
        eq("id",newState.id)
      }
    } ?: new Transition(stateFrom:stateOrigin,stateTo:newState)
   
    if(!newTransition.actions?.contains(newAction)){
      newTransition.addToActions(newAction)
    }
    
    if(!newTransition.id)
      currentMachine.addToTransitions(newTransition)
    
    currentMachine.save(failOnError:true)
    currentMachine 
  }

  State moveToAction(def instance,String action){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    Machine machine = machineryLink.machine

    State state = getCurrentStateOfInstance(instance)

    if(!state)
      state = machine.initialState
    
    Transition transition = machine.transitions.find{ transition -> transition.stateFrom.id == state.id && transition.actions.contains(action) }

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
