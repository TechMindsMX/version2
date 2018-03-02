package com.modulus.uno

class DataImssEmployeeController {

  DataImssEmployeeService dataImssEmployeeService

  def create() {
    Company company = Company.get(session.company)
    BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
    EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(businessEntity.rfc, company)
    respond new DataImssEmployee(), model:[businessEntity:businessEntity, employee:employee]
  }

  def save(DataImssEmployeeCommand command) {
    log.info "Data Imss command: ${command.dump()}"

    if (command.hasErrors()) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
      EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(businessEntity.rfc, company)
      render view:"create", model:[dataImssEmployee:command, businessEntity:businessEntity, employee:employee]
      return
    }

    DataImssEmployee dataImssEmployee = command.createDataImssEmployee()
    dataImssEmployeeService.saveDataImss(dataImssEmployee)

    log.info "Data Imss Instance: ${dataImssEmployee.dump()}"
    if (dataImssEmployee.hasErrors()) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
      EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(businessEntity.rfc, company)
      render view:"create", model:[dataImssEmployee:dataImssEmployee, businessEntity:businessEntity, employee:employee]
      return
    }

    redirect controller:"businessEntity", action:"show", id:params.businessEntityId
  }

  def edit(DataImssEmployee dataImssEmployee) {
    BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
    respond dataImssEmployee, model:[businessEntity:businessEntity, employee:dataImssEmployee.employee]
  }

  def update(DataImssEmployeeCommand command) {
    log.info "Data Imss to update: ${command.dump()}"

    if (command.hasErrors()) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
      EmployeeLink employee = EmployeeLink.findByEmployeeRefAndCompany(businessEntity.rfc, company)
      render view:"edit", model:[dataImssEmployee:command, businessEntity:businessEntity, employee:employee]
      return
    }

    DataImssEmployee dataImssEmployee = DataImssEmployee.get(params.id)
    EmployeeLink employee = dataImssEmployee.employee
    dataImssEmployee.properties = command.createDataImssEmployee().properties
    dataImssEmployee.employee = employee

    dataImssEmployeeService.saveDataImss(dataImssEmployee)

    if (dataImssEmployee.hasErrors()) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
      render view:"edit", model:[dataImssEmployee:dataImssEmployee, businessEntity:businessEntity, employee:dataImssEmployee.employee]
      return
    }

    log.info "Data Imss Instance updated: ${dataImssEmployee.dump()}"

    redirect controller:"businessEntity", action:"show", id:params.businessEntityId
  }

}
