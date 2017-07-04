package com.modulus.uno

class DataImssEmployeeController {

  def create() {
    BusinessEntity businessEntity = BusinessEntity.get(params.businessEntityId)
    EmployeeLink employee = EmployeeLink.findByEmployeeRef(businessEntity.rfc)
    respond new DataImssEmployee(employee:employee), model:[businessEntity:businessEntity, employee:employee]
  }

}
