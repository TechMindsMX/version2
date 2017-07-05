package com.modulus.uno

import grails.transaction.Transactional

class PaysheetProjectService {

  @Transactional
  PaysheetProject savePaysheetProject(PaysheetProject paysheetProject) {
    paysheetProject.save()
    paysheetProject
  }

}
