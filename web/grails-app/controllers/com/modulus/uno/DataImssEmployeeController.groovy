package com.modulus.uno

class DataImssEmployeeController {

  def create() {
    BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
    EmployeeLink employee = EmployeeLink.findByEmployeeRef(businessEntity.rfc)
    responde new DataImssEmployee(), model:[businessEntity:businessEntity, employee:employee]
  }

}
