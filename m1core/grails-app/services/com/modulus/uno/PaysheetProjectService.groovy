package com.modulus.uno

import grails.transaction.Transactional

class PaysheetProjectService {

  @Transactional
  PaysheetProject savePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.save()
    paysheetProject
  }

  @Transactional
  void deletePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.delete()
  }

  PaysheetProject getPaysheetProjectByCompanyAndName(Company company, String name) {
    PaysheetProject.findByCompanyAndName(company, name)
  }

}
