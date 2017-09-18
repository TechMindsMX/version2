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

  void "Should create the payment dispersion file for same bank"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionData = [chargeBankAccount:bankAccount, paymentMessage:"DEP ss 1"]
    when:
      def result = service.createTxtDispersionFileForSameBank(employees, dispersionData)
    then:
      result.readLines().size() == 2
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000001200.00DEP SS 1                      "
			result.readLines()[1] == "000EmployeeAccount0000CompanyAccountMXN0000000003000.00DEP SS 1                      "
	}

  void "Should create the payment dispersion file for inter bank"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"900").save(validate:false)).save(validate:false)
      Map dispersionData = [chargeBankAccount:bankAccount, paymentMessage:"TRN ss 1"]
    when:
      def result = service.createTxtDispersionFileForInterBank(employees, dispersionData)
    then:
      result.readLines().size() == 2
			result.readLines()[0] == "Clabe interbanking0000CompanyAccountMXN0000000001200.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H"
			result.readLines()[1] == "Clabe interbanking0000CompanyAccountMXN0000000003000.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H"
	}

/*

*/
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
