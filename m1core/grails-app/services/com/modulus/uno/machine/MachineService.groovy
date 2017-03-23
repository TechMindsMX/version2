package com.modulus.uno.machine

import grails.transaction.Transactional
import org.springframework.transaction.annotation.Propagation

@Transactional
class MachineService {

  MachineEventExecuterService machineEventExecuterService

  Machine createMachineWithActions(String startName,String stateToName,ArrayList<String> actions){
    Machine machine = new Machine()

    State initialState = new State(name:startName)
    State finalState = new State(name:stateToName,
                                 finalState:true)

    machine.addToStates(initialState)
    machine.addToStates(finalState)
    machine.save()

    Transition transition = new Transition(stateFrom:initialState,
                                           stateTo:finalState)

    actions.each{ action ->
      transition.addToActions(action)
    }

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
        eq("id",currentMachine.id)
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

  State moveToActionAndListen(def instance,String action){
    State currentState = moveToAction(instance,action)
    machineEventExecuterService.executeEvents(instance)
    currentState
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  State moveToAction(def instance,String action){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    Machine machine = machineryLink.machine

    State state = getCurrentStateOfInstance(instance)

    Transition transition = machine.transitions.find{ transition -> transition.stateFrom.id == state.id && transition.actions.contains(action) }

    if(!transition)
      throw new StatelessException("There is n't a transition for the action ${action}.")

    State newState = transition.stateTo
    TrackingLog trackingLog = new TrackingLog(state:newState.name)
    machineryLink.addToTrackingLogs(trackingLog)
    machineryLink.save(failOnError:true)
    newState
  }

  State getCurrentStateOfInstance(def instance){
    MachineryLink machineryLink = MachineryLink.findByMachineryRefAndType(instance.id,instance.class.simpleName)
    String currentState = machineryLink.trackingLogs?.max{ trackingLog -> trackingLog.id }?.state
    Machine stateMachine = machineryLink.machine
    stateMachine.states.find{ state -> state.name == currentState } ?: stateMachine.initialState
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

  void saveMachine(Machine machine){
    State initialState = machine.initialState
    Transition initialTransition = machine.transitions.find{ it.stateFrom.name == initialState.name }
    ArrayList<Transition> stateTransitions = machine.transitions.findAll{ transition -> (transition.stateFrom.name != initialTransition.stateFrom.name || transition.stateTo.name != initialTransition.stateTo.name) }

    if(initialTransition){
      ArrayList<String> actions = []
      actions.addAll(0,initialTransition.actions)
      Machine newMachine = createMachineWithActions(initialTransition.stateFrom.name.toUpperCase(),initialTransition.stateTo.name.toUpperCase(),actions)
      ArrayList<State> states = [initialState,initialTransition.stateTo]
      ArrayList<Transition> transitionsToSave = []

      while(states){
        State s = states.remove(0)
        State state = newMachine.states.find{ it.name == s.name.toUpperCase() }
        transitionsToSave = stateTransitions.findAll{ it.stateFrom.name == s.name }
        stateTransitions.removeAll{ it.stateFrom.name == s.name }
        
        transitionsToSave.each{ transition ->
          transition.actions.each{ action ->
            createTransition(state.id,transition.stateTo.name.toUpperCase(),action)
          }
          states << transition.stateTo
        }
      }
    }
  }

  void updateMachine(Long machineId,Machine machine){
    Machine machineToUpdate = Machine.get(machineId)
    machineToUpdate.initialState.name = machine.initialState.name.toUpperCase()
    machineToUpdate.initialState.save()

    ArrayList<Transition> transitions = machineToUpdate.transitions 

    transitions.each{ transition ->
      machineToUpdate.removeFromTransitions(transition)
    }

    transitions*.delete()
    State initialState = machineToUpdate.initialState
    
    ArrayList<State> previousStates = machineToUpdate.states.findAll{ it.name != initialState.name }

    previousStates.each{ state ->
      machineToUpdate.removeFromStates(state)
    } 

    previousStates*.delete() 
    machineToUpdate.save()
    
    ArrayList<State> states = [initialState]
    ArrayList<Transition> transitionsToSave = []

    while(states){
      State s = states.remove(0)
      State state = machineToUpdate.states.find{ it.name == s.name.toUpperCase() }
      transitionsToSave = machine.transitions.findAll{ it.stateFrom.name.toUpperCase() == s.name }
      machine.transitions.removeAll{ it.stateFrom.name == s.name }

      transitionsToSave.each{ transition ->
        transition.actions.each{ action ->
          createTransition(state.id,transition.stateTo.name.toUpperCase(),action)
        }
        states << transition.stateTo
      }
    }

  }

}
