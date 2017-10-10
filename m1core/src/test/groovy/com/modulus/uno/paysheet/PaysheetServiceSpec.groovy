package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*
import com.modulus.uno.Bank
import com.modulus.uno.S3Asset
import com.modulus.uno.S3AssetService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.ComposeName
import com.modulus.uno.NameType

@TestFor(PaysheetService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, S3Asset, BusinessEntity, ComposeName])
class PaysheetServiceSpec extends Specification {

  PaysheetEmployeeService paysheetEmployeeService = Mock(PaysheetEmployeeService)
  PrePaysheetService prePaysheetService = Mock(PrePaysheetService)
  S3AssetService s3AssetService = Mock(S3AssetService)

  def setup() {
    service.paysheetEmployeeService = paysheetEmployeeService
    service.prePaysheetService = prePaysheetService
    service.s3AssetService = s3AssetService
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

	void "Should prepare dispersion data for bank"() {
		given:"The paysheet"
			Bank bank = new Bank(name:"BANCO").save(validate:false)
			Paysheet paysheet = new Paysheet().save(validate:false)
			PrePaysheetEmployee prePaysheetEmployee = new PrePaysheetEmployee(bank:bank)
			paysheet.addToEmployees(new PaysheetEmployee(prePaysheetEmployee:prePaysheetEmployee).save(validate:false))
		and:"the charge bank account"
			BankAccount bankAccount = new BankAccount(banco:bank).save(validate:false)
		and:"the payment message"
			Map dispersionData = [paymentMessage:"Payment Message"]
		when:
			def result = service.prepareDispersionDataForBank(paysheet, bankAccount, dispersionData)
		then:
			result.employees.size() == 1
			result.chargeBankAccount == bankAccount
			result.paymentMessage == "Payment Message"
			result.applyDate == null
	}

	void "Should create dispersion files for dispersion data"() {
		given:"The dispersion data"
			BankAccount chargeBankAccount = new BankAccount(banco:new Bank(name:"BANCO").save(validate:false), accountNumber:"NumCuenta").save(validate:false)
			List<PaysheetEmployee> employees = [createPaysheetEmployee()]
			Map dispersionData = [chargeBankAccount:chargeBankAccount, employees:employees, paymentMessage:"Payment Message"]
		when:
			def result = service.createDispersionFilesForDispersionData(dispersionData)
		then:
			result.size() == 2
	}

  void "Should create the payment dispersion SA BBVA file"() {
    given:"employees list"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
    and:"The dispersion data"
			BankAccount bankAccount = new BankAccount(accountNumber:"CompanyAccount", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
      Map dispersionDataForBank = [employees:employees, chargeBankAccount:bankAccount, paymentMessage:"PERIODO-PAGO"]
    when:
      def result = service.createTxtDispersionFileForBBVABANCOMER(dispersionDataForBank, "SA")
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
      def result = service.createTxtDispersionFileForBBVABANCOMER(dispersionDataForBank, "IAS")
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
      def result = service.createTxtDispersionFileDefault(dispersionDataForBank, "SA")
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
      def result = service.createTxtDispersionFileDefault(dispersionDataForBank, "IAS")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount0000CompanyAccountMXN0000000003000.00IAS-DEFAULTLAYOUT             "
	}

	void "Should upload dispersion files to S3"() {
		given:"The files"
			List files = [new File("/tmp/file01.txt"), new File("/tmp/file02.txt")]
	  and:
			s3AssetService.createFileToUpload(_, _) >> new S3Asset().save(validate:false)
		when:
			def result = service.uploadDispersionFilesToS3(files)
		then:
			result.size() == 2
	}

	void "Should add the dispersion files to paysheet"() {
		given:"The paysheet"
			Paysheet paysheet = new Paysheet().save(validate:false)
		and:"The s3 files"
			List s3Files = [new S3Asset().save(validate:false)]
		when:
			service.addingDispersionFilesToPaysheet(paysheet, s3Files)
		then:
			paysheet.dispersionFiles.size() == 1
	}

  void "Should create the payment dispersion file inter bank SA"() {
    given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
      Map dispersionData = [employees:employees, paymentMessage:"TRN ss 1"]
    when:
      def result = service.createDispersionFileInterBank(dispersionData, "SA")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "Clabe interbanking000000000M1AccountMXN0000000001200.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H"
	}

  void "Should create the payment dispersion file inter bank IAS"() {
    given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
      Map dispersionData = [employees:employees, paymentMessage:"TRN ss 1"]
    when:
      def result = service.createDispersionFileInterBank(dispersionData, "IAS")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "Clabe interbanking000000000M1AccountMXN0000000003000.00NAME EMPLOYEE CLEANED         40999TRN SS 1                      ${new Date().format('ddMMyy').padLeft(7,'0')}H"
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

	void "Should create dispersion file for SANTANDER bank"() {
		given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
			BankAccount bankAccount = new BankAccount(accountNumber:"Account", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, chargeBankAccount:bankAccount, applyDate:applyDate]
		and:"The business entity"
			BusinessEntity businessEntity = new BusinessEntity(rfc:"RFC").save(validate:false)
			ComposeName name = new ComposeName(value:"NameEmp", type:NameType.NOMBRE).save(validate:false)
			ComposeName lastName = new ComposeName(value:"LastNameEmp", type:NameType.APELLIDO_PATERNO).save(validate:false)
			ComposeName motherLastName = new ComposeName(value:"MotherLastNameEmp", type:NameType.APELLIDO_MATERNO).save(validate:false)
			businessEntity.addToNames(name)
			businessEntity.addToNames(lastName)
			businessEntity.addToNames(motherLastName)
			businessEntity.save(validate:false)
		when:
			def result = service.createTxtDispersionFileForSANTANDER(dispersionData, "SA")
		then:
			result.readLines().size() == 3
			result.readLines()[0] == "100001E${new Date().format('MMddyyyy')}Account         ${applyDate.format('MMddyyyy')}"
			result.readLines()[1] == "200002${'NUM'.padRight(7,' ')}${'LASTNAMEEMP'.padRight(30,' ')}${'MOTHERLASTNAMEEMP'.padRight(20,' ')}${'NAMEEMP'.padRight(30,' ')}${'EMPLOYEEACCOUNT'.padLeft(16,' ')}${'120000'.padLeft(18,'0')}01"
			result.readLines()[2] == "30000200001${'120000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file IAS for SANTANDER bank"() {
		given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
			BankAccount bankAccount = new BankAccount(accountNumber:"Account", banco:new Bank(bankingCode:"999").save(validate:false)).save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, chargeBankAccount:bankAccount, applyDate:applyDate]
		and:"The business entity"
			BusinessEntity businessEntity = new BusinessEntity(rfc:"RFC").save(validate:false)
			ComposeName name = new ComposeName(value:"NameEmp", type:NameType.NOMBRE).save(validate:false)
			ComposeName lastName = new ComposeName(value:"LastNameEmp", type:NameType.APELLIDO_PATERNO).save(validate:false)
			ComposeName motherLastName = new ComposeName(value:"MotherLastNameEmp", type:NameType.APELLIDO_MATERNO).save(validate:false)
			businessEntity.addToNames(name)
			businessEntity.addToNames(lastName)
			businessEntity.addToNames(motherLastName)
			businessEntity.save(validate:false)
		when:
			def result = service.createTxtDispersionFileForSANTANDER(dispersionData, "IAS")
		then:
			result.readLines().size() == 3
			result.readLines()[0] == "100001E${new Date().format('MMddyyyy')}Account         ${applyDate.format('MMddyyyy')}"
			result.readLines()[1] == "200002${'NUM'.padRight(7,' ')}${'LASTNAMEEMP'.padRight(30,' ')}${'MOTHERLASTNAMEEMP'.padRight(20,' ')}${'NAMEEMP'.padRight(30,' ')}${'EMPLOYEEACCOUNT'.padLeft(16,' ')}${'300000'.padLeft(18,'0')}01"
			result.readLines()[2] == "30000200001${'300000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file SA for BANAMEX bank"() {
		given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
			BankAccount bankAccount = new BankAccount(accountNumber:"Account", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, chargeBankAccount:bankAccount, applyDate:applyDate, sequence:"1", nameCompany:"BILLING COMPANY", paymentMessage:"BANAMEX-LAYOUT", idPaysheet:1]
		and:"The business entity"
			BusinessEntity businessEntity = new BusinessEntity(rfc:"RFC", type: BusinessEntityType.FISICA).save(validate:false)
			ComposeName name = new ComposeName(value:"NameEmp", type:NameType.NOMBRE).save(validate:false)
			ComposeName lastName = new ComposeName(value:"LastNameEmp", type:NameType.APELLIDO_PATERNO).save(validate:false)
			ComposeName motherLastName = new ComposeName(value:"MotherLastNameEmp", type:NameType.APELLIDO_MATERNO).save(validate:false)
			businessEntity.addToNames(name)
			businessEntity.addToNames(lastName)
			businessEntity.addToNames(motherLastName)
			businessEntity.save(validate:false)
		when:
			def result = service.createTxtDispersionFileForBANAMEX(dispersionData, "SA")
		then:
			result.readLines().size() == 4
			result.readLines()[0] == "1000000012345${new Date().format('yyMMdd')}0001${'BILLING COMPANY'.padRight(36,' ')}${'BANAMEXLAYOUT'.padRight(20,' ')}15D01"
			result.readLines()[1] == "21001${'120000'.padLeft(18,'0')}03${'180'.padLeft(13,'0')}${'Account'.padLeft(7,' ')}${'1'.padLeft(6,'0')}"
			result.readLines()[2] == "3000101001${'120000'.padLeft(18,'0')}01${'be '.padLeft(13,'0')}${'EmployeeAccount'.padLeft(7,' ')}${'1NUM'.padRight(16,' ')}${'NameEmp LastNameEmp MotherLastNameEmp'.toUpperCase().padRight(55,' ')}${''.padRight(140,' ')}000000${''.padRight(152,' ')}"
			result.readLines()[3] == "4001${'1'.padLeft(6,'0')}${'120000'.padLeft(18,'0')}000001${'120000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file IAS for BANAMEX bank"() {
		given:"The dispersion data"
      List<PaysheetEmployee> employees = [createPaysheetEmployee()]
			BankAccount bankAccount = new BankAccount(accountNumber:"Account", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, chargeBankAccount:bankAccount, applyDate:applyDate, sequence:"1", nameCompany:"BILLING COMPANY", paymentMessage:"BANAMEX-LAYOUT", idPaysheet:1]
		and:"The business entity"
			BusinessEntity businessEntity = new BusinessEntity(rfc:"RFC", type: BusinessEntityType.FISICA).save(validate:false)
			ComposeName name = new ComposeName(value:"NameEmp", type:NameType.NOMBRE).save(validate:false)
			ComposeName lastName = new ComposeName(value:"LastNameEmp", type:NameType.APELLIDO_PATERNO).save(validate:false)
			ComposeName motherLastName = new ComposeName(value:"MotherLastNameEmp", type:NameType.APELLIDO_MATERNO).save(validate:false)
			businessEntity.addToNames(name)
			businessEntity.addToNames(lastName)
			businessEntity.addToNames(motherLastName)
			businessEntity.save(validate:false)
		when:
			def result = service.createTxtDispersionFileForBANAMEX(dispersionData, "IAS")
		then:
			result.readLines().size() == 4
			result.readLines()[0] == "1000000012345${new Date().format('yyMMdd')}0001${'BILLING COMPANY'.padRight(36,' ')}${'BANAMEXLAYOUT'.padRight(20,' ')}15D01"
			result.readLines()[1] == "21001${'300000'.padLeft(18,'0')}03${'180'.padLeft(13,'0')}${'Account'.padLeft(7,' ')}${'1'.padLeft(6,'0')}"
			result.readLines()[2] == "3000101001${'300000'.padLeft(18,'0')}01${'be '.padLeft(13,'0')}${'EmployeeAccount'.padLeft(7,' ')}${'1NUM'.padRight(16,' ')}${'NameEmp LastNameEmp MotherLastNameEmp'.toUpperCase().padRight(55,' ')}${''.padRight(140,' ')}000000${''.padRight(152,' ')}"
			result.readLines()[3] == "4001${'1'.padLeft(6,'0')}${'300000'.padLeft(18,'0')}000001${'300000'.padLeft(18,'0')}"
	}

	void "Should get banks list from bank accounts to payment dispersion"(){
		given:"Bank accounts list"
			Bank bank01 = new Bank(name:"BANCO-1").save(validate:false)
			Bank bank02 = new Bank(name:"BANCO-2").save(validate:false)
			List bankAccounts = [new BankAccount(banco:bank01).save(validate:false), new BankAccount(banco:bank02).save(validate:false), new BankAccount(banco:bank01).save(validate:false)]
		when:
			def result = service.getListBanksFromBankAccountsToPaymentDispersion(bankAccounts)
		then:
			result.size() == 2
	}

	void "Should get dispersion summary for paysheet"() {
		given:"The paysheet"
			PaysheetEmployee paysheetEmployee = createPaysheetEmployee()
		when:
			def result = service.prepareDispersionSummary(paysheetEmployee.paysheet)
		then:
			result.size() == 1
			result.first().bank.bankingCode == "999"
			result.first().accounts.size() == 1
	}

  private PaysheetEmployee createPaysheetEmployee() {
		Company company = new Company().save(validate:false)
		Bank bank = new Bank(bankingCode:"999").save(validate:false)
		BankAccount bankAccount = new BankAccount(banco:bank).save(validate:false)
		company.addToBanksAccounts(bankAccount)
		company.save(validate:false)
    PaysheetEmployee paysheetEmployee = new PaysheetEmployee(
      paysheet: new Paysheet(company:company).save(validate:false),
      prePaysheetEmployee: new PrePaysheetEmployee(rfc:"RFC", account:"EmployeeAccount", nameEmployee:"Náme ?Emplóyee Cleañed", clabe:"Clabe interbanking", bank: bank , numberEmployee:"Num").save(validate:false),
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
