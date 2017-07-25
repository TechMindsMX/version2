package com.modulus.uno

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PrePaysheetService)
@Mock([PrePaysheet, PrePaysheetEmployee, BusinessEntity, Company, EmployeeLink, DataImssEmployee, BankAccount, Bank])
class PrePaysheetServiceSpec extends Specification {

  BusinessEntityService businessEntityService = Mock(BusinessEntityService)

  def setup() {
    service.businessEntityService = businessEntityService
  }

  void "Should get employees available to add a prepaysheet"() {
    given: "PrePaysheet employees"
      PrePaysheet prePaysheet = new PrePaysheet(company:new Company().save(validate:false)).save(validate:false)
      PrePaysheetEmployee employeePrePaysheet = new PrePaysheetEmployee(rfc:"B").save(validate:false)
      prePaysheet.addToEmployees(employeePrePaysheet)
      prePaysheet.save(validate:false)
    and:
      List allEmployees = [new BusinessEntity(rfc:"A").save(validate:false), new BusinessEntity(rfc:"B").save(validate:false), new BusinessEntity(rfc:"C").save(validate:false)]
      businessEntityService.getAllActiveEmployeesForCompany(_) >> allEmployees
    when:
      def result = service.getEmployeesAvailableToAdd(prePaysheet)
    then:
      result.size() == 2
      result.rfc == ["A", "C"]
  }

  void "Should create and save a employee to prePaysheet without bank account"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(company:company).save(validate:false)
    and:"A employee"
      BusinessEntity employee = new BusinessEntity(rfc:"RFC").save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC").save(validate:false)
    and:"The params"
      Map params = [bankAccount1:null, netPayment1:"5000", note1:"TRAMITAR CUENTA"]
    when:
      service.createAndSavePrePaysheetEmployee(employee, prePaysheet, params)
    then:
      prePaysheet.employees.size() == 1
      prePaysheet.employees.first().rfc == "RFC"
      prePaysheet.employees.first().netPayment == 5000
  }

  void "Should create and save a employee to prePaysheet with bank account"() {
    given:"A prePaysheet"
      Company company = new Company().save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(company:company).save(validate:false)
    and:"A employee"
      BusinessEntity employee = new BusinessEntity(rfc:"RFC").save(validate:false)
      EmployeeLink empLink = new EmployeeLink(curp:"CURP", number:"NOEMP", employeeRef:"RFC").save(validate:false)
    and:"The params"
      Map params = [bankAccount1:"1", netPayment1:"5000", note1:"TRAMITAR CUENTA"]
    and:"The bank account"
      BankAccount bankAccount = new BankAccount(accountNumber:"cuenta", clabe:"clabe", cardNumber:"tarjeta", banco:new Bank().save(validate:false)).save(validate:false)
    when:
      service.createAndSavePrePaysheetEmployee(employee, prePaysheet, params)
    then:
      prePaysheet.employees.size() == 1
      prePaysheet.employees.first().rfc == "RFC"
      prePaysheet.employees.first().netPayment == 5000
      prePaysheet.employees.first().bank.id == 1
      prePaysheet.employees.first().clabe == "clabe"
  }

  void "Should get net payments for employees"() {
    given:"The employees"
      List<BusinessEntity> beEmployees = [
        new BusinessEntity(rfc:"A").save(validate:false),
        new BusinessEntity(rfc:"B").save(validate:false),
        new BusinessEntity(rfc:"C").save(validate:false)
      ]
      EmployeeLink elA = new EmployeeLink(employeeRef:"A").save(validate:false)
      EmployeeLink elB = new EmployeeLink(employeeRef:"B").save(validate:false)
      EmployeeLink elC = new EmployeeLink(employeeRef:"C").save(validate:false)
    and:"The data imss"
      DataImssEmployee dieA = new DataImssEmployee(employee:elA, netMonthlySalary:new BigDecimal(10000)).save(validate:false)
      DataImssEmployee dieC = new DataImssEmployee(employee:elC, netMonthlySalary:new BigDecimal(20000)).save(validate:false)
    and:"The pre-paysheet"
      PrePaysheet prePaysheet = new PrePaysheet(paymentPeriod:PaymentPeriod.BIWEEKLY).save(validate:false)
    when:
      def result = service.getNetPaymentForEmployees(beEmployees, prePaysheet)
    then:
      result.size() == 3
      result[1] == 0
      result[0] == 5000
      result[2] == 10000
  }
}
