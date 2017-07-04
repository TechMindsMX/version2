package com.modulus.uno

class DataImssEmployeeController {

  DataImssEmployeeService dataImssEmployeeService

  def create() {
    BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
    EmployeeLink employee = EmployeeLink.findByEmployeeRef(businessEntity.rfc)
    respond new DataImssEmployee(), model:[businessEntity:businessEntity, employee:employee]
  }

  def save(DataImssEmployeeCommand command) {
    log.info "Data Imss to save: ${command.dump()}"

    if (command.hasErrors()) {
      BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
      EmployeeLink employee = EmployeeLink.findByEmployeeRef(businessEntity.rfc)
      render view:"create", model:[dataImssEmployee:command, businessEntity:businessEntity, employee:employee]
      return
    }

    dataImssEmployeeService.saveDataImss(command.createDataImssEmployee())

    redirect controller:"businessEntity", action:"show", id:params.businessEntityId
  }

}
