package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.Company
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank

@TestFor(PaysheetService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank])
class PaysheetServiceSpec extends Specification {

  PaysheetEmployeeService paysheetEmployeeService = Mock(PaysheetEmployeeService)
  PrePaysheetService prePaysheetService = Mock(PrePaysheetService)

  def setup() {
    service.paysheetEmployeeService = paysheetEmployeeService
    service.prePaysheetService = prePaysheetService
  }

  void "Should create paysheet from a prepaysheet"() {
    given:"The prepaysheet"
      PrePaysheet prePaysheet = createPrePaysheet()
    and:
      paysheetEmployeeService.createPaysheetEmployeeFromPrePaysheetEmployee(_, _) >> new PaysheetEmployee().save(validate:false)
    when:
      Paysheet paysheet = service.createPaysheetFromPrePaysheet(prePaysheet)
    then:
      paysheet.id
      paysheet.employees.size() == 1
  }

  private PrePaysheet createPrePaysheet() {
    PrePaysheet prePaysheet = new PrePaysheet(company: new Company().save(validate:false)).save(validate:false)
    prePaysheet.addToEmployees(new PrePaysheetEmployee().save(validate:false))
    prePaysheet.save(validate:false)
    prePaysheet
  }

  void "Should create the payment dispersion imss file when charge account is of the company"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The charge bank account"
      BankAccount chargeBankAccount = new BankAccount(accountNumber:"CompanyAccount").save(validate:false)
    when:
      def result = service.createTxtImssDispersionFileForSameCompanyBank(employees, chargeBankAccount)
    then:
      result.text == "000EmployeeAccount0000CompanyAccountMXN0000000001200.00DEP SS 1                      \n"
  }

  void "Should create the payment dispersion imss file for interbankings"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The charge bank account"
      BankAccount chargeBankAccount = new BankAccount(accountNumber:"CompanyAccount").save(validate:false)
    when:
      def result = service.createTxtImssDispersionFileForInterBank(employees, chargeBankAccount)
    then:
      result.text == "Clabe interbanking0000CompanyAccountMXN0000000003000.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H\n"
  }  

  private PaysheetEmployee createPaysheetEmployee() {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      paysheet: new Paysheet().save(validate:false),
      prePaysheetEmployee: new PrePaysheetEmployee(account:"EmployeeAccount", nameEmployee:"Náme ?Emplóyee Cleañed", clabe:"Clabe interbanking", bank: new Bank(bankingCode:"999").save(validate:false)).save(validate:false),
      salaryImss: new BigDecimal(1000),
      socialQuota: new BigDecimal(100),
      subsidySalary: new BigDecimal(500),
      incomeTax: new BigDecimal(200),
      salaryAssimilable: new BigDecimal(3000)
    )
    paysheetEmployee.save(validate:false)
    paysheetEmployee
  }
}
