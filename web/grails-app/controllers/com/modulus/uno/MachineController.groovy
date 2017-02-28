package com.modulus.uno

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly=true)
class MachineController {

  static allowedMethods = [save: "POST", update: "PUT",delete:"DELETE"]

  MachineryLinkService machineryLinkService
  CompanyService companyService
  CorporateService corporateService
  def springSecurityService

  def index(){
    User user =  springSecurityService.currentUser
    Corporate corporate = corporateService.findCorporateOfUser(user)
    ArrayList<Company> companies = companyService.findCompaniesByCorporateAndStatus(CompanyStatus.ACCEPTED,corporate.id)
    render view:"index",model:[entities:machineryLinkService.getClassesWithMachineryInterface(),
                               companies:companies]
  }

  def create(){
    String entity = params.entity ? "${params.entity[0].toLowerCase()}${params.entity[1..params.entity.size()-1]}" : ""

    if(!entity){
      return response.sendError(404)
    }

    render view:"create",model:[entity:g.message(code:"${entity}.name"),
                                actions:Action.list()]
  }

}
