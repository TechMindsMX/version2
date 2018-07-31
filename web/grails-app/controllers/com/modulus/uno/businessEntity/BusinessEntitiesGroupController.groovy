package com.modulus.uno.businessEntity

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import com.modulus.uno.Corporate

@Transactional(readOnly = true)
class BusinessEntitiesGroupController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  BusinessEntitiesGroupService businessEntitiesGroupService
  
  def list() {
    params.max = 25
    params.sort = "description"
    [groupList:BusinessEntitiesGroup.list(params), groupCount:BusinessEntitiesGroup.count()]
  }

  def create() {
    Corporate corporate = session.corporate
    respond new BusinessEntitiesGroup(), model:[companies:corporate.companies.sort{it.bussinessName}]
  }

  @Transactional
  def save(BusinessEntitiesGroupCommand command) {
    log.info "Command: ${command.dump()}"
    Corporate corporate = session.corporate
    if (command.hasErrors()) {
      render view:'create', model:[commandErrors:command.errors, companies:corporate.companies.sort{it.bussinessName}]
    }
    BusinessEntitiesGroup businessEntitiesGroup = command.createBusinessEntitiesGroup()
    log.info "Group to save: ${businessEntitiesGroup?.dump()}"
    if (businessEntitiesGroup.hasErrors()) {
      render view:'create', model:[businessEntitiesGroup:businessEntitiesGroup, companies:corporate.companies.sort{it.bussinessName}]
    }
    businessEntitiesGroup.save()
    log.info "Group saved: ${businessEntitiesGroup.dump()}"

    redirect action:"show", id:businessEntitiesGroup.id
  }

  def show(BusinessEntitiesGroup businessEntitiesGroup) {
    businessEntitiesAvailables = businessEntitiesGroupService.getBusinessEntitiesAvailablesForGroup(businessEntitiesGroup)
    respond businessEntitiesGroup, model:[businessEntitiesAvailables:businessEntitiesAvailables]
  }

}
