package com.modulus.uno.paysheet

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import spock.lang.Unroll
import java.text.*
import java.math.RoundingMode
import org.springframework.core.io.Resource
import org.springframework.core.io.ClassPathResource
import org.springframework.mock.web.MockMultipartFile

import com.modulus.uno.Company
import com.modulus.uno.BankAccount
import com.modulus.uno.Bank
import com.modulus.uno.S3Asset
import com.modulus.uno.S3AssetService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.BusinessEntityType
import com.modulus.uno.ComposeName
import com.modulus.uno.NameType
import com.modulus.uno.ModulusUnoAccount

@TestFor(PaysheetDispersionFilesService)
@Mock([Paysheet, PrePaysheet, Company, PaysheetEmployee, PrePaysheetEmployee, BankAccount, Bank, S3Asset, BusinessEntity, ComposeName, ModulusUnoAccount, PaysheetContract, PayerPaysheetProject, PaysheetProject])
class PaysheetDispersionFilesServiceSpec extends Specification {

  S3AssetService s3AssetService = Mock(S3AssetService)
  PaysheetProjectService paysheetProjectService = Mock(PaysheetProjectService)
  PaysheetEmployeeService paysheetEmployeeService = Mock(PaysheetEmployeeService)

  Resource resultDispersionBBVAAllCasesResource = new ClassPathResource("resultDispersion/resultDispersionBBVAAllCases.exp")

  def setup() {
    service.s3AssetService = s3AssetService
    service.paysheetProjectService = paysheetProjectService
    service.paysheetEmployeeService = paysheetEmployeeService
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
			Date applyDate = new Date()
			Map dispersionData = [paymentMessage:"Payment Message", applyDate:applyDate]
		when:
			def result = service.prepareDispersionDataForBank(paysheet, bank, dispersionData)
		then:
			result.employees.size() == 1
			result.paymentMessage == "Payment Message"
			result.applyDate == applyDate
      result.idPaysheet == 1
	}

	void "Should create dispersion files for dispersion data"() {
		given:"The dispersion data"
			List<PaysheetEmployee> employees = [createPaysheetEmployee()]
 			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(name:"SABANK", bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(name:"IASBANK", bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"BANAMEX-LAYOUT", idPaysheet:1]
		when:
			def result = service.createDispersionFilesForDispersionData(dispersionData)
		then:
			result.size() == 2
	}

  void "Should create the payment dispersion SA BBVA file"() {
    given:"employees list"
      PaysheetEmployee employeeWithoutSA = createPaysheetEmployee()
      employeeWithoutSA.salaryImss = getValueInBigDecimal("0")
      employeeWithoutSA.socialQuota = getValueInBigDecimal("0")
      employeeWithoutSA.subsidySalary = getValueInBigDecimal("0")
      employeeWithoutSA.incomeTax = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutSA]
    and:"The dispersion data"
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionDataForBank = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"BBVA-LAYOUT", idPaysheet:1]
    when:
      def result = service.createTxtDispersionFileForBBVABANCOMER(dispersionDataForBank, "SA")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "${'1'.padLeft(9,'0')}${''.padLeft(16,' ')}99${'EmployeeAccount'.padRight(20,' ')}${'120000'.padLeft(15,'0')}${'NAME EMPLOYEE CLEANED'.padRight(40,' ')}001001"
	}

  void "Should create the payment dispersion IAS BBVA file"() {
    given:"employees list"
      PaysheetEmployee employeeWithoutIAS = createPaysheetEmployee()
      employeeWithoutIAS.netAssimilable = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutIAS]
    and:"The dispersion data"
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionDataForBank = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"BBVA-LAYOUT", idPaysheet:1]
    when:
      def result = service.createTxtDispersionFileForBBVABANCOMER(dispersionDataForBank, "IAS")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "${'1'.padLeft(9,'0')}${''.padLeft(16,' ')}99${'EmployeeAccount'.padRight(20,' ')}${'300000'.padLeft(15,'0')}${'NAME EMPLOYEE CLEANED'.padRight(40,' ')}001001"
	}

  void "Should create the payment dispersion SA Default file"() {
    given:"employees list"
      PaysheetEmployee employeeWithoutSA = createPaysheetEmployee()
      employeeWithoutSA.salaryImss = getValueInBigDecimal("0")
      employeeWithoutSA.socialQuota = getValueInBigDecimal("0")
      employeeWithoutSA.subsidySalary = getValueInBigDecimal("0")
      employeeWithoutSA.incomeTax = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutSA]
    and:"The dispersion data"
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionDataForBank = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"DEFAULT-LAYOUT", idPaysheet:1]
    when:
      def result = service.createTxtDispersionFileDefault(dispersionDataForBank, "SA")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount000000000AccountSAMXN0000000001200.00SSA-DEFAULTLAYOUT             "
	}

  void "Should create the payment dispersion IAS Default file"() {
    given:"employees list"
      PaysheetEmployee employeeWithoutIAS = createPaysheetEmployee()
      employeeWithoutIAS.netAssimilable = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutIAS]
    and:"The dispersion data"
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionDataForBank = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"DEFAULT-LAYOUT", idPaysheet:1]
    when:
      def result = service.createTxtDispersionFileDefault(dispersionDataForBank, "IAS")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "000EmployeeAccount00000000AccountIASMXN0000000003000.00IAS-DEFAULTLAYOUT             "
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
      PaysheetEmployee employeeWithoutSA = createPaysheetEmployee()
      employeeWithoutSA.salaryImss = getValueInBigDecimal("0")
      employeeWithoutSA.socialQuota = getValueInBigDecimal("0")
      employeeWithoutSA.subsidySalary = getValueInBigDecimal("0")
      employeeWithoutSA.incomeTax = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutSA]
      Map dispersionData = [employees:employees, paymentMessage:"TRN ss 1"]
    when:
      def result = service.createDispersionFileInterBank(dispersionData, "SA")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "${'1'.padLeft(9,'0')}${''.padLeft(16,' ')}99${'EmployeeAccount'.padRight(20,' ')}${'120000'.padLeft(15,'0')}${'NAME EMPLOYEE CLEANED'.padRight(40,' ')}001001"
	}

  void "Should create the payment dispersion file inter bank IAS"() {
    given:"The dispersion data"
      PaysheetEmployee employeeWithoutIAS = createPaysheetEmployee()
      employeeWithoutIAS.netAssimilable = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutIAS]
      Map dispersionData = [employees:employees, paymentMessage:"TRN ss 1"]
    when:
      def result = service.createDispersionFileInterBank(dispersionData, "IAS")
    then:
      result.readLines().size() == 1
			result.readLines()[0] == "${'1'.padLeft(9,'0')}${''.padLeft(16,' ')}99${'EmployeeAccount'.padRight(20,' ')}${'300000'.padLeft(15,'0')}${'NAME EMPLOYEE CLEANED'.padRight(40,' ')}001001"
	}

	void "Should obtain the payers list for payment dispersion"() {
		given:"The paysheet project"
      PaysheetProject paysheetProject = new PaysheetProject(payers:[new PayerPaysheetProject().save(validate:false)]).save(validate:false)
    and:"The PrePaysheet"
      PrePaysheet prePaysheet = new PrePaysheet(paysheetProject:"SomeProject").save(validate:false)
    and:"The paysheet contract"
      PaysheetContract paysheetContract = new PaysheetContract().save(validate:false)
    and:"The paysheet"
			Paysheet paysheet = new Paysheet(paysheetContract:paysheetContract, prePaysheet:prePaysheet).save(validate:false)
		and:
      paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(_, _) >> paysheetProject
		when:
			def result = service.getPayersToPaymentDispersion(paysheet)
	  then:
			result.size() == 1
	}

	void "Should create dispersion file SA for SANTANDER bank"() {
		given:"The dispersion data"
      PaysheetEmployee employeeWithoutSA = createPaysheetEmployee()
      employeeWithoutSA.salaryImss = getValueInBigDecimal("0")
      employeeWithoutSA.socialQuota = getValueInBigDecimal("0")
      employeeWithoutSA.subsidySalary = getValueInBigDecimal("0")
      employeeWithoutSA.incomeTax = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutSA]
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"SANTANDER-LAYOUT", idPaysheet:1]
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
			result.readLines()[0] == "100001E${new Date().format('MMddyyyy')}AccountSA       ${applyDate.format('MMddyyyy')}"
			result.readLines()[1] == "200002${'NUM'.padRight(7,' ')}${'LASTNAMEEMP'.padRight(30,' ')}${'MOTHERLASTNAMEEMP'.padRight(20,' ')}${'NAMEEMP'.padRight(30,' ')}${'EMPLOYEEACCOUNT'.padLeft(16,' ')}${'120000'.padLeft(18,'0')}01"
			result.readLines()[2] == "30000200001${'120000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file IAS for SANTANDER bank"() {
		given:"The dispersion data"
      PaysheetEmployee employeeWithoutIAS = createPaysheetEmployee()
      employeeWithoutIAS.netAssimilable = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutIAS]
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"SANTANDER-LAYOUT", idPaysheet:1]
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
			result.readLines()[0] == "100001E${new Date().format('MMddyyyy')}AccountIAS      ${applyDate.format('MMddyyyy')}"
			result.readLines()[1] == "200002${'NUM'.padRight(7,' ')}${'LASTNAMEEMP'.padRight(30,' ')}${'MOTHERLASTNAMEEMP'.padRight(20,' ')}${'NAMEEMP'.padRight(30,' ')}${'EMPLOYEEACCOUNT'.padLeft(16,' ')}${'300000'.padLeft(18,'0')}01"
			result.readLines()[2] == "30000200001${'300000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file SA for BANAMEX bank"() {
		given:"The dispersion data"
      PaysheetEmployee employeeWithoutSA = createPaysheetEmployee()
      employeeWithoutSA.salaryImss = getValueInBigDecimal("0")
      employeeWithoutSA.socialQuota = getValueInBigDecimal("0")
      employeeWithoutSA.subsidySalary = getValueInBigDecimal("0")
      employeeWithoutSA.incomeTax = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutSA]
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"BANAMEX-LAYOUT", idPaysheet:1]
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
			result.readLines()[0] == "1000000012345${new Date().format('yyMMdd')}0001${'SA COMPANY'.padRight(36,' ')}${'BANAMEXLAYOUT'.padRight(20,' ')}15D01"
			result.readLines()[1] == "21001${'120000'.padLeft(18,'0')}03${'180'.padLeft(13,'0')}${'AccountSA'.padLeft(7,' ')}${'1'.padLeft(6,'0')}"
			result.readLines()[2] == "3000101001${'120000'.padLeft(18,'0')}01${'180'.padLeft(13,'0')}${'EmployeeAccount'.padLeft(7,' ')}${'1NUM'.padRight(16,' ')}${'NameEmp LastNameEmp MotherLastNameEmp'.toUpperCase().padRight(55,' ')}${''.padRight(140,' ')}000000${''.padRight(152,' ')}"
			result.readLines()[3] == "4001${'1'.padLeft(6,'0')}${'120000'.padLeft(18,'0')}000001${'120000'.padLeft(18,'0')}"
	}

	void "Should create dispersion file IAS for BANAMEX bank"() {
		given:"The dispersion data"
      PaysheetEmployee employeeWithoutIAS = createPaysheetEmployee()
      employeeWithoutIAS.netAssimilable = getValueInBigDecimal("0")
      List<PaysheetEmployee> employees = [createPaysheetEmployee(), employeeWithoutIAS]
			BankAccount saBankAccount = new BankAccount(accountNumber:"AccountSA", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			BankAccount iasBankAccount = new BankAccount(accountNumber:"AccountIAS", banco:new Bank(bankingCode:"999").save(validate:false), clientNumber:"12345", branchNumber:"180").save(validate:false)
			Date applyDate = new Date()
			Map dispersionData = [employees:employees, saBankAccount:saBankAccount, iasBankAccount:iasBankAccount, applyDate:applyDate, sequence:"1", saPayer:"SA COMPANY", iasPayer:"IAS COMPANY", paymentMessage:"BANAMEX-LAYOUT", idPaysheet:1]
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
			result.readLines()[0] == "1000000012345${new Date().format('yyMMdd')}0001${'IAS COMPANY'.padRight(36,' ')}${'BANAMEXLAYOUT'.padRight(20,' ')}15D01"
			result.readLines()[1] == "21001${'300000'.padLeft(18,'0')}03${'180'.padLeft(13,'0')}${'AccountIAS'.padLeft(7,' ')}${'1'.padLeft(6,'0')}"
			result.readLines()[2] == "3000101001${'300000'.padLeft(18,'0')}01${'180'.padLeft(13,'0')}${'EmployeeAccount'.padLeft(7,' ')}${'1NUM'.padRight(16,' ')}${'NameEmp LastNameEmp MotherLastNameEmp'.toUpperCase().padRight(55,' ')}${''.padRight(140,' ')}000000${''.padRight(152,' ')}"
			result.readLines()[3] == "4001${'1'.padLeft(6,'0')}${'300000'.padLeft(18,'0')}000001${'300000'.padLeft(18,'0')}"
	}

  @Unroll
  void "Should get status for current result = #theCurrentResult"() {
    given:"The employee"
      PaysheetEmployee employee = theEmployee
    and:"The map result"
      Map result = theCurrentResult
    when:
      def status = service.getStatusForCurrentResult(employee, result)
    then:
      status == expectedStatus
    where:
      theEmployee    |   theCurrentResult    ||   expectedStatus
      null | [schema:PaymentSchema.IMSS, amount:3000, resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.NOT_FOUND
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false)).save(validate:false) | [schema:PaymentSchema.IMSS, amount:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.APPLIED
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false)).save(validate:false) | [schema:PaymentSchema.ASSIMILABLE, amount:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.APPLIED
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false)).save(validate:false) | [schema:PaymentSchema.IMSS, amount:3000, resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.AMOUNT_ERROR
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false)).save(validate:false) | [schema:PaymentSchema.ASSIMILABLE, amount:3000, resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.AMOUNT_ERROR
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false)).save(validate:false) | [schema:PaymentSchema.ASSIMILABLE, amount:3000, resultMessage:"OPERACION FALLIDA"] || DispersionResultFileDetailStatus.REJECTED
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false), status:PaysheetEmployeeStatus.IMSS_PAYED).save(validate:false) | [schema:PaymentSchema.IMSS, amount:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.PREVIOUS_PROCESSED
      new PaysheetEmployee(salaryImss:new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), netAssimilable:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), prePaysheetEmployee:new PrePaysheetEmployee().save(validate:false), status:PaysheetEmployeeStatus.ASSIMILABLE_PAYED).save(validate:false) | [schema:PaymentSchema.ASSIMILABLE, amount:new BigDecimal(2000).setScale(2, RoundingMode.HALF_UP), resultMessage:"OPERACION EXITOSA"] || DispersionResultFileDetailStatus.PREVIOUS_PROCESSED
  }

  void "Should process result dispersion file for BBVA-BANCOMER"() {
    given:"The paysheet"
      Bank bank = new Bank(name:"BBVA").save(validate:false)
      Paysheet paysheet = createPaysheetForTestProcessResultDispersionFile(bank)
    and:"The data result dispersion file"
      File resultDispersionBBVAAllCases = resultDispersionBBVAAllCasesResource.getFile()
      Map dataResultDispersionFile = [resultFile:resultDispersionBBVAAllCases, bank:bank, schema:PaymentSchema.IMSS]
    when:""
      def results = service.processResultDispersionFileForBBVABANCOMER(paysheet, dataResultDispersionFile)
    then:""
      results.resultMessage == ["OPERACION EXITOSA","OPERACION FALLIDA", "OPERACION EXITOSA", "NO EXISTE", "ERROR NO IDENTIFICADO", "ERROR NO IDENTIFICADO"]
      results[5].employee == null
      results.size() == 6
  }

  private Paysheet createPaysheetForTestProcessResultDispersionFile(Bank bank) {
    Paysheet paysheet = new Paysheet().save(validate:false)
    List employees = [
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.PENDING, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201801")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.IMSS_PAYED, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201802")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.ASSIMILABLE_PAYED, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201803")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.PENDING, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201804")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.PENDING, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201805")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.CASH, status:PaysheetEmployeeStatus.PENDING, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201806")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.REJECTED, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201807")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.REJECTED, prePaysheetEmployee: new PrePaysheetEmployee(bank:new Bank(name:"ANOTHER").save(validate:false), account:"2018201808")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.PAYED, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201809")).save(validate:false),
      new PaysheetEmployee(paymentWay:PaymentWay.BANKING, status:PaysheetEmployeeStatus.CANCELED, prePaysheetEmployee: new PrePaysheetEmployee(bank:bank, account:"2018201810")).save(validate:false)
    ] 
    paysheet.employees = employees
    paysheet.save(validate:false)
    paysheet
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
      prePaysheetEmployee: new PrePaysheetEmployee(rfc:"RFC", account:"EmployeeAccount", nameEmployee:"Náme ?Emplóyee Cleañed", clabe:"Clabe interbanking", bank: bank , numberEmployee:"Num", branch:"180").save(validate:false),
      salaryImss: getValueInBigDecimal("1000"),
      socialQuota: getValueInBigDecimal("100"),
      subsidySalary: getValueInBigDecimal("500"),
      incomeTax: getValueInBigDecimal("200"),
      netAssimilable: getValueInBigDecimal("3000")
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
