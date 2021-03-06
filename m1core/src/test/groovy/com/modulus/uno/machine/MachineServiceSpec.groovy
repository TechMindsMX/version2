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
      def machineEventExecuterServiceMock = Mock(MachineEventExecuterService)
      service.machineEventExecuterService = machineEventExecuterServiceMock
    and:"the movements"
      ArrayList<String> actions = ["Insert Card","Cancel","Service"]
      actions.each{ action ->
        service.moveToAction(instance,action)
      }
    when:
      State state = service.getCurrentStateOfInstance(instance)
    then:
      state.name == "Out Of Service"
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
    and:"the machinery event executer mock"
      def machineEventExecuterServiceMock = Mock(MachineEventExecuterService)
      service.machineEventExecuterService = machineEventExecuterServiceMock
    when:
      State newState = service.moveToActionAndListen(instance,action)
    then:
      machineryLink.trackingLogs.size() == 1
      1 * machineEventExecuterServiceMock.executeEvents(_)
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
    and:"the machinery event executer mock"
      def machineEventExecuterServiceMock = Mock(MachineEventExecuterService)
      service.machineEventExecuterService = machineEventExecuterServiceMock
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

  Should "save the complete machine"(){
    given:
      Machine machine = new Machine(initialState:new State(name:"Idle"))
      ArrayList<Transition> transitions = [new Transition(stateFrom:new State(name:"Idle"),
                                                          stateTo:new State(name:"Active"),
                                                          actions:["INSERT CARD"]),
                                           new Transition(stateFrom:new State(name:"Active"),
                                                          actions:["DONE"],
                                                          stateTo:new State(name:"Idle")),
                                           new Transition(stateFrom:new State(name:"Idle"),
                                                          actions:["SERVICE"],
                                                          stateTo:new State(name:"Out Of Service")),
                                           new Transition(stateFrom:new State(name:"Out Of Service"),
                                                          actions:["FIX"],
                                                          stateTo:new State(name:"Idle"))]
      machine.transitions = transitions
    when:
      service.saveMachine(machine)
      ArrayList<Machine> machineList = Machine.list()
    then:
      machineList.size() == 1 
  }

  Should "edit a machine"(){
    given:"the existent machine"
      createMachine();
    and:"the new Machine"
      Machine updatedMachine = new Machine(initialState:new State(name:"Initial_State"))
      ArrayList<Transition> transitions = [new Transition(stateFrom:new State(name:"Initial_State"),
                                                          stateTo:new State(name:"Active"),
                                                          actions:["INSERT CARD"]),
                                           new Transition(stateFrom:new State(name:"Initial_State"),
                                                          stateTo:new State(name:"Inactive"),
                                                          actions:["REMOVE CARD"]),
                                           new Transition(stateFrom:new State(name:"Initial_State"),
                                                          stateTo:new State(name:"Out Of Service"),
                                                          actions:["SERVICE"])]
      
      transitions.each{ transition ->
        updatedMachine.addToTransitions(transition)
      }

    when:
      service.updateMachine(1,updatedMachine)
      Machine machine = Machine.list().first()
    then:
      machine.initialState.name == "INITIAL_STATE"
      machine.transitions.size() == 3
  }

  Should "Get the transitions for the states"(){
    given:"the states"
      ArrayList<State> states = [new State(name:"created").save(validate:false),new State(name:"waitingForAuthorization").save(validate:false),
        new State(name:"canceled").save(validate:false),new State(name:"authorized").save(validate:false)]
    and:"the transitions"
      ArrayList<Transition> transitions = [new Transition(stateFrom: State.findByName("created"), stateTo:State.findByName("waitingForAuthorization"), actions: ["ASK FOR AUTHORIZATION"]), 
                                            new Transition(stateFrom: State.findByName("waitingForAuthorization"), stateTo:State.findByName("canceled"), actions: ["CANCEL"]),
                                            new Transition(stateFrom: State.findByName("waitingForAuthorization"), stateTo:State.findByName("authorized"), actions: ["AUTHORIZE"])]
      transitions*.save(validate:false)
    when:
      ArrayList<Transition> stateTransitions = service.findTransitionsForStates([states[0], states[3]])
    then:
      stateTransitions.size() == 1
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
