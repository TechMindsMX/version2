package com.modulus.uno.businessEntity

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

import com.modulus.uno.Corporate

@Transactional(readOnly = true)
class BusinessEntitiesGroupController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
  
  def list() {
    params.max = 25
    params.sort = "description"
    [groupList:BusinessEntitiesGroup.list(params), groupCount:BusinessEntitiesGroup.count()]
  }

  def create() {
    Corporate corporate = session.corporate
    respond new BusinessEntitiesGroup(), model:[companies:corporate.companies.sort{it.bussinessName}]
  }

}
