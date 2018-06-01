package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*
import com.modulus.uno.Company
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.ModulusUnoAccount
import com.modulus.uno.Commission
import com.modulus.uno.CommissionType
import com.modulus.uno.CommissionTransactionService

@TestFor(PaysheetService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, ModulusUnoAccount, PaysheetContract, PayerPaysheetProject, PaysheetProject, Commission])
class PaysheetServiceSpec extends Specification {

  PaysheetEmployeeService paysheetEmployeeService = Mock(PaysheetEmployeeService)
  PaysheetDispersionFilesService paysheetDispersionFilesService = Mock(PaysheetDispersionFilesService)
  PrePaysheetService prePaysheetService = Mock(PrePaysheetService)
  PaysheetReceiptService paysheetReceiptService = Mock(PaysheetReceiptService)
  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  CommissionTransactionService commissionTransactionService = Mock(CommissionTransactionService)

  def setup() {
    service.paysheetEmployeeService = paysheetEmployeeService
    service.prePaysheetService = prePaysheetService
    service.paysheetDispersionFilesService = paysheetDispersionFilesService
    service.paysheetReceiptService = paysheetReceiptService
    service.paysheetProjectService = paysheetProjectService
    service.commissionTransactionService = commissionTransactionService
    grailsApplication.config.paysheet.banks = "40002,40012,40014"
  }

  void "Should create paysheet from a prepaysheet"() {
    given:"The prepaysheet"
      PrePaysheet prePaysheet = createPrePaysheet()
    and:
      paysheetEmployeeService.createPaysheetEmployeeFromPrePaysheetEmployee(_, _) >> new PaysheetEmployee(salaryImss:100, socialQuota:10, subsidySalary:20, incomeTax:15, netAssimilable:100, prePaysheetEmployee:new PrePaysheetEmployee(netPayment:195).save(validate:false)).save(validate:false)
    when:
      Paysheet paysheet = service.createPaysheetFromPrePaysheet(prePaysheet)
    then:
      paysheet.id
      paysheet.employees.size() == 1
			paysheet.status == PaysheetStatus.CREATED
  }

  void "Should create and reject the paysheet from a prepaysheet because exists difference in payments amount"() {
    given:"The prepaysheet"
      PrePaysheet prePaysheet = createPrePaysheet()
    and:
      paysheetEmployeeService.createPaysheetEmployeeFromPrePaysheetEmployee(_, _) >> new PaysheetEmployee(salaryImss:100, socialQuota:10, subsidySalary:20, incomeTax:15, netAssimilable:100, prePaysheetEmployee:new PrePaysheetEmployee(netPayment:200).save(validate:false)).save(validate:false)
    when:
      Paysheet paysheet = service.createPaysheetFromPrePaysheet(prePaysheet)
    then:
      paysheet.id
      paysheet.employees.size() == 1
			paysheet.status == PaysheetStatus.REJECTED
  }


  private PrePaysheet createPrePaysheet() {
    PaysheetContract paysheetContract = new PaysheetContract(company: new Company().save(validate:false)).save(validate:false)
    PrePaysheet prePaysheet = new PrePaysheet(paysheetContract:paysheetContract).save(validate:false)
    prePaysheet.addToEmployees(new PrePaysheetEmployee().save(validate:false))
    prePaysheet.save(validate:false)
    prePaysheet
  }

	void "Should complement the dispersion data"() {
		given:
			String[] ids = ["1","2","3","2"]
      String[] types = ["SameBank", "SameBank", "SameBank", "InterBank"]
			Map dispersionData = [bank:ids, saBankAccount:ids, iasBankAccount:ids, type:types]
		and:
			BankAccount bankAccount1 = new BankAccount().save(validate:false)
			BankAccount bankAccount2 = new BankAccount().save(validate:false)
			BankAccount bankAccount3 = new BankAccount().save(validate:false)
      Bank bank1 = new Bank().save(validate:false)
      Bank bank2 = new Bank().save(validate:false)
      Bank bank3 = new Bank().save(validate:false)
		when:
			Map result = service.complementDispersionData(dispersionData)
		then:
			result.dataByBank.size() == 4
	}

	void "Should get dispersion summary for paysheet"() {
		given:"The paysheet"
			PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
		and:"Stp bank"
			Bank stpBank = new Bank(name:"STP").save(validate:false)
    and:
      paysheetDispersionFilesService.getPayersToPaymentDispersion(_) >> createPayersList()
		when:
			def result = service.prepareDispersionSummary(paysheetEmployee.paysheet)
		then:
			result.size() == 1
      result.count { it.type=="SameBank" } == 0
      result.count { it.type=="InterBank" } == 1
	}

	void "Should add inter bank summary for dispersion paysheet"(){
		given:"The paysheet"
			PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
		and:"Stp bank"
			Bank stpBank = new Bank(bankingCode:"40012").save(validate:false)
		and:"Payers list"
      List payers = createPayersList()
	  and:"Summary"
			List summary = []
	  when:
			def result = service.addInterBankSummary(summary, paysheetEmployee.paysheet, payers) 
		then:
			result.size() == 1
			result.first().bank.bankingCode == "40012"
			result.first().totalSA == new BigDecimal(1200)
			result.first().totalIAS == new BigDecimal(3000)
	}

  @Unroll
	void "Should obtain the payers with bank accounts in bank=#theBank and with schema=#theSchema"() {
		given:"The companies"
      Company companyOne = new Company(rfc:"UNO", bussinessName:"ONE").save(validate:false)
      BankAccount account1 = new BankAccount(banco:theBank).save(validate:false)
      companyOne.addToBanksAccounts(account1)
      companyOne.save(validate:false)
      Company companyTwo = new Company(rfc:"DOS", bussinessName:"TWO").save(validate:false)
      Bank anotherBank = new Bank(name:"IASBANK").save(validate:false)
      BankAccount account2 = new BankAccount(banco:anotherBank).save(validate:false)
      companyTwo.addToBanksAccounts(account2)
      companyTwo.save(validate:false)
    and:"The payers"
      List payers = [new PayerPaysheetProject(paymentSchema:theSchema, company:companyOne).save(validate:false), new PayerPaysheetProject(paymentSchema:theSchema, company:companyTwo)]
    and:"The bank"
      Bank bank = theBank
    and:"The schema"
      PaymentSchema schema = theSchema
		when:
			def result = service.getPayersForBankAndSchema(payers, bank, schema)
	  then:
			result.size() == 1
      result.first().payer == thePayer
    where:
    theBank   |   theSchema   ||  thePayer
    new Bank(name:"BANK").save(validate:false)    |   PaymentSchema.IMSS  || "ONE"
    new Bank(name:"BANK").save(validate:false)    |   PaymentSchema.ASSIMILABLE  || "ONE"
	}

	void "Should obtain the banks list from paysheet payers"() {
    given:"The payers"
      List payers = createPayersList()
		when:
			def result = service.getBanksFromPayers(payers)
	  then:
			result.size() == 3
      result[0].name == "ANOTHER"
      result[1].name == "BANK01"
      result[2].name == "BANK02"
	}

	@Unroll
	void "Should reject the paysheet when exists wrong payments for employees: #listWrongEmployees"() {
		given:"The paysheet"
			Paysheet paysheet = new Paysheet().save(validate:false)
	  and:"The wrong employees"
			List<PaysheetEmployee> wrongEmployees = listWrongEmployees
	  when:
			def result = service.rejectPaysheetAndPrePaysheetForWrongPayments(paysheet, wrongEmployees)
	  then:
			result.status == PaysheetStatus.REJECTED
			result.rejectReason == expectRejectReason
	  where:
			listWrongEmployees 	|| 	expectRejectReason
			[new PaysheetEmployee(prePaysheetEmployee:new PrePaysheetEmployee(rfc:"UNO"))] 	|| "LOS SIGUIENTES EMPLEADOS RESULTARON CON UN NETO A PAGAR DISTINTO AL INDICADO EN LA PRENÓMINA: UNO"
			[new PaysheetEmployee(prePaysheetEmployee:new PrePaysheetEmployee(rfc:"UNO")), new PaysheetEmployee(prePaysheetEmployee:new PrePaysheetEmployee(rfc:"DOS"))] 	|| "LOS SIGUIENTES EMPLEADOS RESULTARON CON UN NETO A PAGAR DISTINTO AL INDICADO EN LA PRENÓMINA: UNO,DOS"
	}

  void "Should generate paysheet receipts from paysheet for schema IMSS"() {
    given:"The paysheet"
      Paysheet paysheet = createPaysheetWithEmployees()
    and:"The schema"
      PaymentSchema schema = PaymentSchema.IMSS
    when:
      def paysheetResult = service.generatePaysheetReceiptsFromPaysheetForSchema(paysheet, schema)
    then:
      3 * paysheetReceiptService.generatePaysheetReceiptForEmployeeAndSchema(_, _) >> [stampId:"UUID_PAYSHEET_RECEIPT", serie:"NOM", folio:"1"]
      3 * paysheetEmployeeService.savePaysheetReceiptUuidIMSS(_, _)
      3 * paysheetEmployeeService.setStampedStatusToEmployee(_, _)
  }

  void "Should generate paysheet receipts from paysheet for schema ASSIMILABLE"() {
    given:"The paysheet"
      Paysheet paysheet = createPaysheetWithEmployees()
    and:"The schema"
      PaymentSchema schema = PaymentSchema.ASSIMILABLE
    when:
      def paysheetResult = service.generatePaysheetReceiptsFromPaysheetForSchema(paysheet, schema)
    then:
      3 * paysheetReceiptService.generatePaysheetReceiptForEmployeeAndSchema(_, _) >> [stampId:"UUID_PAYSHEET_RECEIPT", serie:"NOM", folio:"1"]
      3 * paysheetEmployeeService.savePaysheetReceiptUuidAsimilable(_, _)
      3 * paysheetEmployeeService.setStampedStatusToEmployee(_, _)
  }

  void "Should get statuses true for payers to stamp for a paysheet"() {
    given:"The paysheet"
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(payers:createPayersList()).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:paysheetProject).save(validate:false)
      Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
    and:
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
      commissionTransactionService.getCommissionForCompanyByType(_, _) >>> [new Commission(type:CommissionType.RECIBO_NOMINA).save(validate:false), new Commission(type:CommissionType.RECIBO_NOMINA).save(validate:false)]
    when:
      def result = service.checkPayersToStamp(paysheet)
    then:
      result.statusPayerSA
      result.statusPayerIAS
  }

  void "Should get status false for  payers to stamp for a paysheet"() {
    given:"The paysheet"
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
      PaysheetProject paysheetProject = new PaysheetProject(payers:createPayersList()).save(validate:false)
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:paysheetProject).save(validate:false)
      Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
    and:
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
      commissionTransactionService.getCommissionForCompanyByType(_, _) >>> [null, null]
    when:
      def result = service.checkPayersToStamp(paysheet)
    then:
      !result.statusPayerSA
      !result.statusPayerIAS
  }

  private Paysheet createPaysheetWithEmployees() {
    Paysheet paysheet = new Paysheet(employees:[]).save(validate:false)
    paysheet.employees.addAll(createEmployeesForTestPaysheetReceipts())
    paysheet
  }

  private def createEmployeesForTestPaysheetReceipts() {
    [
      new PaysheetEmployee(status:PaysheetEmployeeStatus.PENDING, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.PAYED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.CANCELED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.REJECTED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.IMSS_PAYED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.ASSIMILABLE_PAYED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.IMSS_STAMPED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.ASSIMILABLE_STAMPED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false),
      new PaysheetEmployee(status:PaysheetEmployeeStatus.FULL_STAMPED, salaryImss:new BigDecimal(2500), socialQuota: new BigDecimal(63.44), subsidySalary: new BigDecimal(162.44), incomeTax:new BigDecimal(166.57), netAssimilable: new BigDecimal(15067.57), prePaysheetEmployee: new PrePaysheetEmployee().save(validate:false)).save(validate:false)
    ]
  }

  private PaysheetEmployee createPaysheetEmployee() {
		Company company = new Company().save(validate:false)
		ModulusUnoAccount m1Account = new ModulusUnoAccount().save(validate:false)
		company.addToAccounts(m1Account)
		company.save(validate:false)
		Bank bank = new Bank(bankingCode:"999").save(validate:false)
		BankAccount bankAccount = new BankAccount(banco:bank).save(validate:false)
		company.addToBanksAccounts(bankAccount)
		company.save(validate:false)
    PaysheetContract paysheetContract = new PaysheetContract(company:company).save(validate:false)
    PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:"SOMEPROJECT").save(validate:false)
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      paysheet: new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false),
      prePaysheetEmployee: new PrePaysheetEmployee(rfc:"RFC", account:"EmployeeAccount", nameEmployee:"Náme ?Emplóyee Cleañed", clabe:"Clabe interbanking", bank: bank , numberEmployee:"Num").save(validate:false),
      salaryImss: getValueInBigDecimal("1000"),
      socialQuota: getValueInBigDecimal("100"),
      subsidySalary: getValueInBigDecimal("500"),
      incomeTax: getValueInBigDecimal("200"),
      netAssimilable: getValueInBigDecimal("3000")
    )
    paysheetEmployee.save(validate:false)
    paysheetEmployee
  }

  private def createPayersList() {
    Company companyOne = new Company(rfc:"UNO").save(validate:false)
    BankAccount account1 = new BankAccount(banco:new Bank(name:"BANK01", bankingCode:"40002")).save(validate:false)
    BankAccount account2 = new BankAccount(banco:new Bank(name:"BANK02", bankingCode:"40012")).save(validate:false)
    BankAccount account3 = new BankAccount(banco:new Bank(name:"BANK03", bankingCode:"40072")).save(validate:false)
    BankAccount account4 = new BankAccount(banco:new Bank(name:"BANK04", bankingCode:"40127")).save(validate:false)
    companyOne.addToBanksAccounts(account1)
    companyOne.addToBanksAccounts(account3)
    companyOne.save(validate:false)
    Company companyTwo = new Company(rfc:"DOS").save(validate:false)
    Bank anotherBank = new Bank(name:"ANOTHER", bankingCode:"40014").save(validate:false)
    BankAccount another = new BankAccount(banco:anotherBank).save(validate:false)
    companyTwo.addToBanksAccounts(account2)
    companyTwo.addToBanksAccounts(another)
    companyTwo.addToBanksAccounts(account4)
    companyTwo.save(validate:false)
    [new PayerPaysheetProject(paymentSchema:PaymentSchema.IMSS, company:companyOne).save(validate:false), new PayerPaysheetProject(paymentSchema:PaymentSchema.ASSIMILABLE, company:companyTwo)] 
  }

  private def getValueInBigDecimal(String value) {
    Locale.setDefault(new Locale("es","MX"));
    DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
    df.setParseBigDecimal(true);
    BigDecimal bd = (BigDecimal) df.parse(value);
    bd
  }

}
