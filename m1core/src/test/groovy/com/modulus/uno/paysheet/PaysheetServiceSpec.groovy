package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*

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

  @Unroll
  void "Should create the payment dispersion #paymentSchema file dispersion way is #dispersionWay"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
      Map dispersionData = [dispersionWay:dispersionWay, chargeAccountNumber:"CompanyAccount", salary:dispersionSalary, paymentMessage:paymentMessage, paymentSchema:paymentSchema]
    when:
      def result = service."createTxtDispersionFileFor${dispersionWay}"(employees, dispersionData)
    then:
      result.text == textResult
    where:
    paymentSchema   |   dispersionWay   |   dispersionSalary  |   paymentMessage  || textResult
    "IMSS"  |   "SameBank"  | "imssSalaryNet" | "DEP ss 1"  || "000EmployeeAccount0000CompanyAccountMXN0000000001200.00DEP SS 1                      \n"
    "IMSS"  |   "InterBank" |   "imssSalaryNet" |   "TRN ss 1" || "Clabe interbanking0000CompanyAccountMXN0000000001200.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H\n"
    "Asimilable"  |   "SameBank"  | "salaryAssimilable" | "DEP ias 1"  || "000EmployeeAccount0000CompanyAccountMXN0000000003000.00DEP IAS 1                     \n"
    "Asimilable"  |   "InterBank" |   "salaryAssimilable" |   "TRN ias 1" || "Clabe interbanking0000CompanyAccountMXN0000000003000.00NAME EMPLOYEE CLEANED         40999TRN IAS 1                     ${new Date().format('ddMMyy').padLeft(7,'0')}H\n"
  }

  private PaysheetEmployee createPaysheetEmployee() {
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      paysheet: new Paysheet().save(validate:false),
      prePaysheetEmployee: new PrePaysheetEmployee(account:"EmployeeAccount", nameEmployee:"Náme ?Emplóyee Cleañed", clabe:"Clabe interbanking", bank: new Bank(bankingCode:"999").save(validate:false)).save(validate:false),
      salaryImss: getValueInBigDecimal("1000"),
      socialQuota: getValueInBigDecimal("100"),
      subsidySalary: getValueInBigDecimal("500"),
      incomeTax: getValueInBigDecimal("200"),
      salaryAssimilable: getValueInBigDecimal("3000")
    )
    paysheetEmployee.save(validate:false)
    paysheetEmployee
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
