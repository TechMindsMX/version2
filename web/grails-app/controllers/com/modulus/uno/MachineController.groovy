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
  TransitionService transitionService

  def index(){
    [entities:machineryLinkService.getClassesWithMachineryInterface()]
  }

  def show(String id){
    Machine machine = Machine.findByUuid(id)

    if(!machine)
      return response.sendError(404)

    respond (transitionService.getMachineTransitions(machine.id))
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
    machineService.saveMachine(machine.getMachine())
    redirect(action:"index")
  }

  def list(){
    ArrayList<Machine> machines = Machine.list()
    [machines:machines]
  }

}
