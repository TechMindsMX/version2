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

  @Transactional
  def addEmployeesToPrePaysheet(PrePaysheet prePaysheet, String entities) {
    List<BusinessEntity> employees = businessEntityService.getBusinessEntitiesFromIds(entities)
    employees.each { employee ->
      createAndSavePrePaysheetEmployee(employee, prePaysheet)
    }
  }

  PrePaysheet createAndSavePrePaysheetEmployee(BusinessEntity employee, PrePaysheet prePaysheet) {
    DataImssEmployee dataImssEmployee = businessEntityService.getDataImssEmployee(prePaysheet.company, employee, LeadType.EMPLEADO)
    PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(
      rfc:employee.rfc,
      curp:employee.curp,
      numberEmployee:employee.number,
      nameEmployee:employee.toString(),
      netPayment:dataImssEmployee ? dataImssEmployee.netMonthlySalary : new BigDecimal(0),
      prePaysheet:prePaysheet
    )
    prePaysheet.addToEmployees(prePaysheetEmployee)
    prePaysheet.save()
    log.info "PrePaysheetEmployee saved: ${prePaysheetEmployee?.dump()}
    prePaysheet
  }

}
