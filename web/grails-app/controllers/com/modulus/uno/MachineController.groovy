package com.modulus.uno

import com.modulus.uno.machine.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON

@Transactional(readOnly=true)
class MachineController {

  static allowedMethods = [save: "POST", update: "PUT",delete:"DELETE"]

  MachineryLinkService machineryLinkService
  CompanyService companyService
  CorporateService corporateService
  def springSecurityService
  MachineService machineService

  def index(){
    [entities:machineryLinkService.getClassesWithMachineryInterface()]
  }

  def show(String id){
    def machine = Machine.findByUuid(id) 

    if(!machine)
      return response.sendError(404)

    [machine:machine]
  }

  def register(){
    User user =  springSecurityService.currentUser
    Corporate corporate = corporateService.findCorporateOfUser(user)
    ArrayList<Company> companies = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,corporate.id)
    render view:"register",model:[entities:machineryLinkService.getClassesWithMachineryInterface(),
                                  companies:companies]
  }

  def create(){
    String entity = params.entity ? "${params.entity[0].toLowerCase()}${params.entity[1..params.entity.size()-1]}" : ""

    if(!entity){
      return response.sendError(404)
    }

    render view:"create",model:[entity:g.message(code:"${entity}.name")]
  }

  @Transactional
  def save(MachineCommand machine){
    String initialState = machine.initialState
    TransitionCommand initialTransition = machine.transitions.find{ it.stateFrom == initialState }
    ArrayList<TransitionCommand> stateTransitions = machine.transitions.findAll{ transition -> (transition.stateFrom != initialTransition.stateFrom || transition.stateTo != initialTransition.stateTo) }

    //TODO: Move BFS to Service
    if(initialTransition){

      Machine newMachine = machineService.createMachineWithActions(initialTransition.stateFrom,initialTransition.stateTo,initialTransition.actions)
      ArrayList<String> states = [initialState,initialTransition.stateTo]//Q
      ArrayList<TransitionCommand> transitionsToSave = []

      while(states){
        String s = states.remove(0)
        State state = newMachine.states.find{ it.name.toUpperCase() == s }
        transitionsToSave = stateTransitions.findAll{ it.stateFrom == s }
        stateTransitions.removeAll{ it.stateFrom == s }  

        transitionsToSave.each{ transition ->
          transition.actions.each{ action ->
            machineService.createTransition(state.id,transition.stateTo,action)
          }
          states << transition.stateTo
        }
      }
    }

    redirect(action:"index")
  }

  def list(){
    ArrayList<Machine> machines = Machine.list()
    [machines:machines]
  }

}
