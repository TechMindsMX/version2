package com.modulus.uno.businessEntity

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BusinessEntitiesGroupController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
  
  def list() {
    params.max = 25
    params.sort = "description"
    [groupList:BusinessEntitiesGroup.list(params), groupCount:BusinessEntitiesGroup.count()]
  }

}
