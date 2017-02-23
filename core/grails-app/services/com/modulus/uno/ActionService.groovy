package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class ActionService {

  def addActionToCompany(Action action,Long companyId){
    Company company = Company.get(companyId)
    company.addToActions(action)
    company.save()
  }

}
