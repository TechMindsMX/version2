package com.modulus.uno

import grails.transaction.Transactional

class PrePaysheetService {

  BusinessEntityService businessEntityService

  @Transactional
  PrePaysheet savePrePaysheet(PrePaysheet prePaysheet) {
    prePaysheet.save()
    log.info "Prepaysheet saved: ${prePaysheet.dump()}"
    prePaysheet
  }

  Map getListAndCountPrePaysheetsForCompany(Company company, Map params) {
    Map prePaysheets = [:]
    prePaysheets.list = PrePaysheet.findAllByCompany(company, params)
    prePaysheets.total = PrePaysheet.countByCompany(company)
    prePaysheets
  }

  List<BusinessEntity> getEmployeesAvailableToAdd(PrePaysheet prePaysheet) {
    List<BusinessEntity> allActiveEmployeesForCompany = businessEntityService.getAllActiveEmployeesForCompany(prePaysheet.company)
    List<BusinessEntity> currentEmployees = obtainBusinessEntitiesFromEmployeesPrePaysheet(prePaysheet)
    allActiveEmployeesForCompany - currentEmployees
  }

  List<BusinessEntity> obtainBusinessEntitiesFromEmployeesPrePaysheet(prePaysheet) {
    List<BusinessEntity> beInPrePaysheet = []
    prePaysheet.employees.each { emp ->
      beInPrePaysheet.add(BusinessEntity.findByRfc(emp.rfc))
    }
    beInPrePaysheet.sort{ it.id }
  }

}
