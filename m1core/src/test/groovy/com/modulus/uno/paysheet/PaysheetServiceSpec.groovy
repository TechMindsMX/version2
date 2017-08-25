package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

import com.modulus.uno.Company
import com.modulus.uno.BankAccount

@TestFor(PaysheetService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount])
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

  void "Should create a txt file imss dispersion for a employee list and a charge bank account"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The charge bank account"
      BankAccount chargeBankAccount = new BankAccount(accountNumber:"CompanyAccount").save(validate:false)
    when:
      def result = service.createTxtImssDispersionFileForSameCompanyBank(employees, chargeBankAccount)
    then:
      result.text == "000EmployeeAccount0000CompanyAccountMXN0000000001200.00PAGO IMSS                     \n"
  }

  private PaysheetEmployee createPaysheetEmployee() {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      prePaysheetEmployee: new PrePaysheetEmployee(account:"EmployeeAccount").save(validate:false),
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
