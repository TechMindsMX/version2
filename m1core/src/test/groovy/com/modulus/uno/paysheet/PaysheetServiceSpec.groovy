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

  void "Should create the payment dispersion SA BBVA file"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionDataForBank = [employees:employees, chargeBankAccount:bankAccount, paymentMessage:"PERIODO-PAGO"]
    when:
      def result = service.createTxtDispersionFileSAForBBVABANCOMER(dispersionDataForBank)
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000001200.00SSA-PERIODOPAGO               "
	}

  void "Should create the payment dispersion IAS BBVA file"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionDataForBank = [employees:employees, chargeBankAccount:bankAccount, paymentMessage:"PERIODO-PAGO"]
    when:
      def result = service.createTxtDispersionFileIASForBBVABANCOMER(dispersionDataForBank)
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000003000.00IAS-PERIODOPAGO               "
	}

  void "Should create the payment dispersion SA Default file"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionDataForBank = [employees:employees, chargeBankAccount:bankAccount, paymentMessage:"DEFAULTLAYOUT"]
    when:
      def result = service.createTxtDispersionFileSADefault(dispersionDataForBank)
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000001200.00SSA-DEFAULTLAYOUT             "
	}

  void "Should create the payment dispersion IAS Default file"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionDataForBank = [employees:employees, chargeBankAccount:bankAccount, paymentMessage:"DEFAULTLAYOUT"]
    when:
      def result = service.createTxtDispersionFileIASDefault(dispersionDataForBank)
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000003000.00IAS-DEFAULTLAYOUT             "
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

	void "Should complement the dispersion data"() {
		given:
			String[] ids = ["1","2","3"]
			Map dispersionData = [chargeBankAccountsIds:ids]
		and:
			BankAccount bankAccount1 = new BankAccount().save(validate:false)
			BankAccount bankAccount2 = new BankAccount().save(validate:false)
			BankAccount bankAccount3 = new BankAccount().save(validate:false)
		when:
			Map result = service.complementDispersionData(dispersionData)
		then:
			result.chargeBankAccountsList.size() == 3
	}

	void "Should obtain the bank accounts list for payment dispersion"() {
		given:"The paysheet with employees and each one with a bank account"
			Bank bank = new Bank(name:"BANCO").save(validate:false)
			PrePaysheetEmployee prePaysheetEmployee1 = new PrePaysheetEmployee(bank:bank).save(validate:false)
			PaysheetEmployee paysheetEmployee1 = new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee1).save(validate:false)
			PrePaysheetEmployee prePaysheetEmployee2 = new PrePaysheetEmployee(bank:new Bank(name:"OTRO BANCO").save(validate:false)).save(validate:false)
			PaysheetEmployee paysheetEmployee2 = new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee2).save(validate:false)
			Paysheet paysheet = new Paysheet().save(validate:false)
			paysheet.addToEmployees(paysheetEmployee1)
			paysheet.addToEmployees(paysheetEmployee2)
			paysheet.save(validate:false)
		and:"The company with bank accounts"
			Company company = new Company().save(validate:false)
			company.addToBanksAccounts(new BankAccount(banco:bank))
			company.addToBanksAccounts(new BankAccount(banco:new Bank(name:"BANCO2").save(validate:false)))
			company.save(validate:false)
			paysheet.company = company
			paysheet.save(validate:false)
		when:
			def result = service.getBanksAccountsToPaymentDispersion(paysheet)
	  then:
			result.size() == 1
			result.first().banco.name == "BANCO"
	}

	void "Should obtain empty bank accounts list for payment dispersion"() {
		given:"The paysheet with employees and each one with a bank account"
			PrePaysheetEmployee prePaysheetEmployee1 = new PrePaysheetEmployee(bank:new Bank(name:"BANCO A").save(validate:false)).save(validate:false)
			PaysheetEmployee paysheetEmployee1 = new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee1).save(validate:false)
			PrePaysheetEmployee prePaysheetEmployee2 = new PrePaysheetEmployee(bank:new Bank(name:"OTRO BANCO").save(validate:false)).save(validate:false)
			PaysheetEmployee paysheetEmployee2 = new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee2).save(validate:false)
			Paysheet paysheet = new Paysheet().save(validate:false)
			paysheet.addToEmployees(paysheetEmployee1)
			paysheet.addToEmployees(paysheetEmployee2)
			paysheet.save(validate:false)
		and:"The company with bank accounts"
			Bank bank = new Bank(name:"BANCO").save(validate:false)
			Company company = new Company().save(validate:false)
			company.addToBanksAccounts(new BankAccount(banco:bank))
			company.addToBanksAccounts(new BankAccount(banco:new Bank(name:"BANCO2").save(validate:false)))
			company.save(validate:false)
			paysheet.company = company
			paysheet.save(validate:false)
		when:
			def result = service.getBanksAccountsToPaymentDispersion(paysheet)
	  then:
			result.size() == 0
	}

}
