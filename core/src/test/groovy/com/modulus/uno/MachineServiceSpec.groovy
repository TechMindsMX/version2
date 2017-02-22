package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should

@TestFor(MachineService)
@Mock([PurchaseOrder,Machine,State,Transition,Action,MachineryLink,LogRecord,Company])
class MachineServiceSpec extends Specification {

  Should "create a new machinery with an action"(){
    given:"the action"
      Action action = new Action(name:"Do Something")
      action.save(validate:false)
    when:
      Machine machine = service.createMachineWithAction(action)
    then:
      machine.initialState
      machine.initialState.transitions.size() == 1
      machine.initialState.transitions.first().id 
      machine.states.size() == 2
  }

  Should "add a transition for a machine"(){
    given:"the machine"
      Action action = new Action(name:"Do Something")
      action.save(validate:false)
      Machine machine = service.createMachineWithAction(action)
    and: "the second action"
      Action anotherAction = new Action(name:"Do another thing")
      anotherAction.save()
    when:
      Machine updatedMachine = service.createTransition(machine.id,action.id,anotherAction.id)
    then:
      updatedMachine.states.size() == 3
      updatedMachine.states.last().finalState == true
      updatedMachine.states[1].transitions.size() == 1
  }

  Should "move the instance to the first state"(){
    given:"the machine"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the company"
      Company company = new Company(bussinessName:"MakingDevs")
      company.save(validate:false)
    and:"the machinery"
      Machine machine = createMachine()
    and:"the link between the instance and its machine"
      MachineryLink machineryLink = new MachineryLink(companyRef:instance.id,
                                                      type:instance.class.simpleName)
      machineryLink.machine = machine
      machineryLink.save()
    and:"the action"
      Action action = Action.findByName("Initial Action")
    and:"the tracking service mock"
      TrackingService trackingServiceMock = Mock(TrackingService)
      service.trackingService = trackingServiceMock
    when:
      State newState = service.moveToAction(instance,action)
    then:
      1 * trackingServiceMock.addRecordToInstanceLog(_,_)
      1 * trackingServiceMock.findLastTrackingLogRecord(_)
  }

  Machine createMachine(){
    Machine machine = new Machine()
    machine.save()
    ArrayList<Action> actions = [new Action(name:"Sell"),new Action(name:"Buy"),new Action(name:"Send")] 
    actions*.save(validate:false)
    Action firstAction = new Action(name:"Initial Action")
    firstAction.save(validate:false)
    machine = service.createMachineWithAction(firstAction)
    machine = service.createTransition(machine.id,firstAction.id,actions[0].id)
   
    (1..(actions.size()-1)).each{ index ->
      machine = service.createTransition(machine.id,actions[index-1].id,actions[index].id)
    } 

    machine
  }

}
