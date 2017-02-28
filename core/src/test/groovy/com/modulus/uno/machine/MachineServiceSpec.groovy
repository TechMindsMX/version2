package com.modulus.uno.machine

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should

@TestFor(MachineService)
@Mock([PurchaseOrder,Machine,State,Transition,Action,MachineryLink,Company,TrackingLog])
class MachineServiceSpec extends Specification {

  Should "create a new machine with an action"(){
    given:"the action"
      Action action = new Action(name:"Do Something")
      action.save(validate:false)
    when:
      Machine machine = service.createMachineWithAction(action)
    then:
      machine.initialState
      machine.transitions.size() == 1
      machine.transitions.first().id 
      machine.states.size() == 2
      machine.transitions.size() == 1
      machine.transitions.first().stateFrom.id == 1
      machine.transitions.first().stateTo.id == 2
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
      updatedMachine.transitions.size() == 2
  }

  Should "get the current state of the instance"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the machine"
      createMachine()
      Machine machine = Machine.get(1)
    and:"the link between the instance and its machine"
      MachineryLink machineryLink = new MachineryLink(machineryRef:instance.id,
                                                      type:instance.class.simpleName)
      machineryLink.machine = machine
      machineryLink.save()
    and:"the movements"
      ArrayList<Action> actions = [Action.findByName("Initial Action"),Action.findByName("Sell")]
      actions.each{ action ->
        service.moveToAction(instance,action)
      }
    when:
      State state = service.getCurrentStateOfInstance(instance)
    then:
      state.id == 3 
  }

  Should "move the instance to the first state"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the company"
      Company company = new Company(bussinessName:"MakingDevs")
      company.save(validate:false)
    and:"the machine"
      createMachine()
      Machine machine = Machine.get(1)
    and:"the link between the instance and its machine"
      MachineryLink machineryLink = new MachineryLink(machineryRef:instance.id,
                                                      type:instance.class.simpleName)
      machineryLink.machine = machine
      machineryLink.save()
    and:"the action"
      Action action = Action.findByName("Initial Action")
    when:
      State newState = service.moveToAction(instance,action)
    then:
      machineryLink.trackingLogs.size() == 1
  }

  Should "get the next states of instance"(){
    given:"the instance"
      PurchaseOrder instance = new PurchaseOrder()
      instance.save(validate:false)
    and:"the machine"
      createMachine()
      Machine machine = Machine.get(1)
    and:"the link between the instance and its machine"
      MachineryLink machineryLink = new MachineryLink(machineryRef:instance.id,
                                                      type:instance.class.simpleName)
      machineryLink.machine = machine
      machineryLink.save()
    and:"the movements"
      ArrayList<Action> actions = [Action.findByName("Initial Action"),Action.findByName("Sell")]
      actions.each{ action ->
        service.moveToAction(instance,action)
      }
    when:
      ArrayList<State> states = service.findNextStatesOfInstance(instance)
    then:
      states.size() == 1
  }

  void createMachine(){
    Machine machine = new Machine()
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
