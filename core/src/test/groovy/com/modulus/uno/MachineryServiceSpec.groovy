package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should

@TestFor(MachineryService)
@Mock([PurchaseOrder,Machinery,State,Transition,Action,MachineryLink,LogRecord])
class MachineryServiceSpec extends Specification {

  
  
  Should "create a new machinery with an action"(){
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
      machineryUpdated.states.last().finalState == true
      machineryUpdated.states[1].transitions.size() == 1
  }

  Should "move the instance to the first state"(){
    given:"the machinery"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the machinery"
      Machinery machinery = createMachinery()
    and:"the link between the instance and its machine"
      MachineryLink machineryLink = new MachineryLink(machineryRef:instance.id,
                                                      type:instance.class.simpleName)
      machineryLink.machinery = machinery
      machineryLink.save()
    and:"the action"
      Action action = Action.findByName("Initial Action")
      action.save()
    and:"the tracking service mock"
      TrackingService trackingServiceMock = Mock(TrackingService)
      service.trackingService = trackingServiceMock
    when:
      State newState = service.moveToAction(instance,action)
    then:
      1 * trackingServiceMock.addRecordToInstanceLog(_,_)
      1 * trackingServiceMock.findLastTrackingLogRecord(_)
  }

  Machinery createMachinery(){
    Machinery machinery = new Machinery()
    machinery.save()
    ArrayList<Action> actions = [new Action(name:"Sell"),new Action(name:"Buy"),new Action(name:"Send")] 
    actions*.save()
    Action firstAction = new Action(name:"Initial Action")
    firstAction.save()
    machinery = service.createMachineWithAction(firstAction)
    machinery = service.createTransition(machinery.id,firstAction.id,actions[0].id)
   
    (1..(actions.size()-1)).each{ index ->
      machinery = service.createTransition(machinery.id,actions[index-1].id,actions[index].id)
    } 

    machinery 
  }

}
