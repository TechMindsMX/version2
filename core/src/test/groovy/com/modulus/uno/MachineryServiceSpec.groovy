package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import java.lang.Void as Should

@TestFor(MachineryService)
@Mock([Machinery,State,Transition,Action])
class MachineryServiceSpec extends Specification {

  Should "create a new machinary with an action"(){
    given:"the action"
      Action action = new Action(name:"Do Something")
      action.save()
    when:
      Machinery machinery = service.createMachineWithAction(action)
    then:
      machinery.initialState
      machinery.initialState.transitions.size() == 1
      machinery.initialState.transitions.first().id 
      machinery.states.size() == 2
  }

  Should "add a transition for a machine"(){
    given:"the machine"
      Action action = new Action(name:"Do Something")
      action.save()
      Machinery machinery = service.createMachineWithAction(action)
    and: "the second action"
      Action anotherAction = new Action(name:"Do another thing")
      anotherAction.save()
    when:
      Machinery machineryUpdated = service.createTransition(machinery.id,action.id,anotherAction.id)
    then:
      machineryUpdated.states.size() == 3
      machineryUpdated.states.last().isFinalState == true
      machineryUpdated.states[1].transitions.size() == 1
  }

}
