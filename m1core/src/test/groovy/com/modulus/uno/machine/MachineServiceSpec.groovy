package com.modulus.uno.machine

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Ignore
import java.lang.Void as Should
import com.modulus.uno.PurchaseOrder

@TestFor(MachineService)
@Mock([PurchaseOrder,Machine,State,Transition,MachineryLink,TrackingLog])
class MachineServiceSpec extends Specification {

  Should "create a new machine with an action"(){
    given:"the action"
      String action = "Add Coin"
    and:"the state names"
      String startName = "Locked"
      String stateToName = "Unlocked"
    when:
      Machine machine = service.createMachineWithActions(startName,stateToName,[action])
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
      String action = "Insert Card"
      Machine machine = service.createMachineWithActions("Idle","Active",[action])
    and: "the second action"
      String anotherAction = "Fix"
    and:"the state from and the state to name"
      State stateFrom = State.findByName("Active")
      String stateToName = "Out of service"
    when:
      Machine updatedMachine = service.createTransition(stateFrom.id,stateToName,anotherAction)
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
    and:"the machinery event executer mock"
      def machineEventExecuterMock = Mock(MachineEventExecuter)
      service.machineEventExecuter = machineEventExecuterMock
    and:"the movements"
      ArrayList<String> actions = ["Insert Card","Cancel","Service"]
      actions.each{ action ->
        service.moveToAction(instance,action)
      }
    when:
      State state = service.getCurrentStateOfInstance(instance)
    then:
      state.name == "Out Of Service"
      actions.size() * machineEventExecuterMock.executeEvent()
  }

  Should "move the instance to the first state"(){
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
    and:"the action"
      String action = "Insert Card"
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
      ArrayList<String> actions = ["Insert Card","Cancel","Service"]
      actions.each{ action ->
        service.moveToAction(instance,action)
      }
    when:
      ArrayList<State> states = service.findNextStatesOfInstance(instance)
    then:
      states.find{ it.name == "Idle" }
      states.size() == 1
  }

  void createMachine(){
    ArrayList<String> actions = ["Service","Insert Card","Cancel","Fix","Finish"]
    ArrayList<String> states = ["Idle","Out Of Service","Active"]
    Machine machine = service.createMachineWithActions(states[0],states[1],[actions[0]])

    def indexes = [[0,2,1],
                   [1,0,3],
                   [2,0,2],
                   [2,0,4]]

    indexes.each{ row ->
      State state = State.findByName(states[row[0]])
      machine = service.createTransition(state.id,states[row[1]],actions[row[2]])
    }    

    machine
  }

}
