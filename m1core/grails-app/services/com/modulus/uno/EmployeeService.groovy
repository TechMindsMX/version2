package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class EmployeeService {

  def messageSource

  def addEmployeeToCompany(EmployeeBusinessEntity employee, Company company, Map params){
    if (isEmployeeOfThisCompany(employee, company)) {
      throw new BusinessException(messageSource.getMessage('exception.employee.already.exist', null, LCH.getLocale()))
    }

    def employeeLink = new EmployeeLink(type:employee.class.simpleName, employeeRef: employee.rfc, company: company, curp:params.curp.toUpperCase(), number:params.number.toUpperCase())
    employeeLink.save()

    if (employeeLink.hasErrors()) {
      log.error "Error al guardar el empleado: ${employeeLink.dump()}"
      throw new BusinessException("Los datos del empleado son erróneos")
    }

    company.addToBusinessEntities(employee)
    employeeLink
  }

  def isEmployeeOfThisCompany(EmployeeBusinessEntity employee, Company company){
    EmployeeLink.countByTypeAndEmployeeRefAndCompany(employee?.class?.simpleName,employee?.rfc,company)
  }

  def isEmployee(instance){
    EmployeeLink.countByTypeAndEmployeeRef(instance.class.simpleName, instance.rfc)
  }

  EmployeeLink employeeAlreadyExistsInCompany(String rfc, Company company) {
    EmployeeLink.findByEmployeeRefAndCompany(rfc, company)
  }

  EmployeeLink createEmployeeForRowEmployee(Map rowEmployee, Company company) {
    if (rowEmployee.NO_EMPL.toString().isNumber()) {
      rowEmployee.NO_EMPL = Integer.toString(new Double(rowEmployee.NO_EMPL).intValue())
    }

    EmployeeLink employeeLink = new EmployeeLink(
      type:"BusinessEntity",
      employeeRef:rowEmployee.RFC,
      curp:rowEmployee.CURP,
      number:rowEmployee.NO_EMPL,
      company:company
    )
    employeeLink.save()
    employeeLink
  }

  def updateEmployeeToCompany(BusinessEntity businessEntity, Company company, Map params) {
    if (businessEntity.rfc.substring(0,10) != params.curp.substring(0,10)) {
      throw new BusinessException("La CURP no corresponde al RFC")
    }
    EmployeeLink employeeLink = EmployeeLink.findByEmployeeRef(params.backRfc)
    employeeLink.employeeRef = businessEntity.rfc
    employeeLink.curp = params.curp
    employeeLink
  }

}
