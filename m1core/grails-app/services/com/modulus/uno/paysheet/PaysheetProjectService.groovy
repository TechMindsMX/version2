package com.modulus.uno.paysheet

import grails.transaction.Transactional
import com.modulus.uno.Company

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
