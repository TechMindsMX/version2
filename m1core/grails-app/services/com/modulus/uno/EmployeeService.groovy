package com.modulus.uno

import org.springframework.context.i18n.LocaleContextHolder as LCH

import grails.transaction.Transactional

@Transactional
class EmployeeService {

  def messageSource
  def emailSenderService

  def addEmployeeToCompany(EmployeeBusinessEntity employee, Company company, String curp){
    if(isEmployeeOfThisCompany(employee, company))throw new BusinessException(messageSource.getMessage('exception.employee.already.exist', null, LCH.getLocale()))
    def employeeLink = new EmployeeLink(type:employee.class.simpleName, employeeRef: employee.rfc, company: company,curp:curp).save()
    emailSenderService.sendEmailForNewEmployee(company, employee)
    company.addToBusinessEntities(employee)
    employeeLink
  }

  def isEmployeeOfThisCompany(EmployeeBusinessEntity employee, Company company){
    EmployeeLink.countByTypeAndEmployeeRefAndCompany(employee?.class?.simpleName,employee?.rfc,company)
  }

  def isEmployee(instance){
    EmployeeLink.countByTypeAndEmployeeRef(instance.class.simpleName, instance.rfc)
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
