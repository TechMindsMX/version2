package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class RequestIntegratedService {

  def grailsApplication

  def sendNotificationsOFCompanyToIntegrated(Company company,User user) {
    company.status = CompanyStatus.VALIDATE
    company.save()
    company
  }

}
