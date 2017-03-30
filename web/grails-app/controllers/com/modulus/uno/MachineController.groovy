package com.modulus.uno

import com.modulus.uno.machine.*
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.converters.JSON

@Transactional(readOnly=true)
class MachineController {

  static allowedMethods = [save: "POST", update: "POST",delete:"DELETE"]

  def springSecurityService
  CompanyService companyService
  CorporateService corporateService
  MachineService machineService
  MachineryLinkService machineryLinkService
  TransitionService transitionService
  CombinationService combinationService 
  CombinationLinkService combinationLinkService

  def index(){
    [entities:machineryLinkService.getClassesWithMachineryInterface()]
  }

  def show(String id){
    Machine machine = Machine.findByUuid(id)

    if(!machine)
      return response.sendError(404)

    ArrayList<State> states = []
    states.addAll(0,machine.states)

    respond ([transitionList:transitionService.getMachineTransitions(machine.id),stateList:states])
  }

  def edit(String id){
    Machine machine = Machine.findByUuid(id)
    if(!machine)
      return response.sendError(404)

    [transitions:transitionService.getMachineTransitions(machine.id),
     states:machine.states]
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

    if(!entity || !params.company){
      return response.sendError(404)
    }

    render view:"create",model:[entity:g.message(code:"${entity}.name"),
                                entityName:params.entity,
                                companyId:params.long("company")]
  }

  @Transactional
  def save(MachineCommand machine){
    Machine savedMachine = machineService.saveMachine(machine.getMachine())
    Company company = Company.get(params.company)
    Combination combination = combinationService.createCombinationOfInstanceWithClass(savedMachine,params.entity)
    combinationLinkService.createCombinationLinkForInstance(company,combination)
    redirect(action:"index")
  }

  @Transactional
  def update(MachineCommand machineCommand){
    Machine machine = Machine.findByUuid(params.uuid)
    machineService.updateMachine(machine.id,machineCommand.getMachine())
    redirect(action:"index")
  }

  def list(){
    ArrayList<Machine> machines = Machine.list()
    [machines:machines]
  }

}
