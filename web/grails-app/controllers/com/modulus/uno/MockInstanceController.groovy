package com.modulus.uno
import com.modulus.uno.machine.*

class MockInstanceController {

  MachineryLinkService machineryLinkService
  MachineService machineService

  static allowedMethods = [advance: "GET"]

  def index() { }

  def create(){ }

  def save(){
    MockInstance instance = new MockInstance(name:params.name).save()
    MachineryLink machineryLink = machineryLinkService.createMachineryLinkForThisInstance(instance, Machine.list().first())
    redirect action:'show',id:instance.id
  }
  
  def show(MockInstance mockInstance){
    State currentState = machineService.getCurrentStateOfInstance(mockInstance)
    ArrayList<Transition> transitions = machineService.findTransitionsForStates([currentState])
    ArrayList<String> actions = []
    transitions.each{ transition ->
      actions += transition.actions
    }
    respond mockInstance,model:[actions:actions]
  }

  def advance(MockInstance instance){
    machineService.moveToActionAndListen(instance,params.currentAction)
    redirect action:'show',id:instance.id
  }

}
