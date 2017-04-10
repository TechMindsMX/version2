package com.modulus.uno.machine

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should

@TestFor(TransitionService)
@Mock([Machine,State,Transition])
class TransitionServiceSpec extends Specification {

  Should "get the transitions of a machine in the correct order"() {
    given:"the machine"
      State initialState = new State(name:"Created")
      initialState.save()
      Machine machine = new Machine(initialState:initialState)

      ArrayList<State> states = [new State(name:"For authorizing"),
                                 new State(name:"Authorized"),
                                 new State(name:"Canceled")]

      states.each{ state ->
        machine.addToStates(state)
      }

      machine.save()
    and:"its transitions"
      ArrayList<Transition> transitions = [new Transition(stateFrom:initialState,
                                                          stateTo:states[0],
                                                          actions:["Ask for authorization"]),
                                           new Transition(stateFrom:states[0],
                                                          stateTo:states[1],
                                                          actions:["Authorize"]),
                                           new Transition(stateFrom:states[0],
                                                          stateTo:states[1],
                                                          actions:["Cancel"])]

      transitions.each{ transition ->
        machine.addToTransitions(transition)
      }

      machine.save()
    when:
      ArrayList<Transition> orderedTransitions = service.getMachineTransitions(machine.id)
    then:
      orderedTransitions.first().stateFrom.id == initialState.id
      orderedTransitions.size() == 3
  }

}
