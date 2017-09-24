package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import com.modulus.uno.Bank
import com.modulus.uno.BankAccount
import com.modulus.uno.S3AssetService

class PaysheetService {

  PaysheetEmployeeService paysheetEmployeeService
  PrePaysheetService prePaysheetService
  S3AssetService s3AssetService
  def grailsApplication

  @Transactional
  Paysheet createPaysheetFromPrePaysheet(PrePaysheet prePaysheet) {
    Paysheet paysheet = new Paysheet(
      prePaysheet:prePaysheet,
      company:prePaysheet.company
    )
    paysheet.save()
    loadEmployeesToPaysheetFromPrePaysheet(paysheet, prePaysheet)
    prePaysheetService.changeStatusToProcessed(prePaysheet)
    paysheet
  }

  Paysheet loadEmployeesToPaysheetFromPrePaysheet(Paysheet paysheet, PrePaysheet prePaysheet) {
    prePaysheet.employees.each { prePaysheetEmployee ->
      PaysheetEmployee paysheetEmployee = paysheetEmployeeService.createPaysheetEmployeeFromPrePaysheetEmployee(paysheet, prePaysheetEmployee)
      paysheet.addToEmployees(paysheetEmployee)
    }
    paysheet.save()
    paysheet
  }

  @Transactional
  Paysheet sendToAuthorize(Paysheet paysheet) {
    paysheet.status = PaysheetStatus.TO_AUTHORIZE
    paysheet.save()
    //TODO: enviar notificación a usuario autorizador de nómina
    paysheet
  }

  def exportPaysheetToXls(Paysheet paysheet) {
    Map employees = getEmployeesToExport(paysheet)
    new WebXlsxExporter().with {
      fillRow(["PROYECTO:", paysheet.prePaysheet.paysheetProject],0)
      fillRow(["PERIODO DE PAGO:", paysheet.prePaysheet.paymentPeriod, "DEL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.initPeriod), "AL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.endPeriod)],1)
      fillRow(["RESIDENTE:", paysheet.prePaysheet.accountExecutive,"TOTAL:", paysheet.total], 2)
      fillRow(employees.headers, 4)
      add(employees.data, employees.properties, 5)
    }
  }

  Map getEmployeesToExport(Paysheet paysheet) {
    Map employees = [:]
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'TOTAL IMSS', 'ASIMILABLE', 'SUBTOTAL', 'CARGA SOCIAL EMPRESA', 'ISN', 'COSTO NOMINAL', 'COMISION', 'TOTAL NÓMINA', 'IVA', 'TOTAL A FACTURAR']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'imssSalaryNet', 'salaryAssimilable', 'totalSalaryEmployee', 'socialQuotaEmployer', 'paysheetTax', 'paysheetCost', 'commission', 'paysheetTotal', 'paysheetIva', 'totalToInvoice']
    employees.data = paysheet.employees.sort {it.prePaysheetEmployee.nameEmployee}
    employees
  }

  @Transactional
  Paysheet authorize(Paysheet paysheet) {
    paysheet.status = PaysheetStatus.AUTHORIZED
    paysheet.save()
    //TODO: enviar notificación
    paysheet
  }

  @Transactional
  Paysheet reject(Paysheet paysheet) {
    paysheet.status = PaysheetStatus.REJECTED
    paysheet.save()
    //TODO: enviar notificación
    paysheet
  }

  def exportPaysheetToXlsImss(Paysheet paysheet) {
    Map employees = getEmployeesToExportImss(paysheet)
    new WebXlsxExporter().with {
      fillRow(["PROYECTO:", paysheet.prePaysheet.paysheetProject, "NÓMINA IMSS"],0)
      fillRow(["PERIODO DE PAGO:", paysheet.prePaysheet.paymentPeriod, "DEL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.initPeriod), "AL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.endPeriod)],1)
      fillRow(["RESIDENTE:", paysheet.prePaysheet.accountExecutive,"TOTAL:", paysheet.total], 2)
      fillRow(employees.headers, 4)
      add(employees.data, employees.properties, 5)
    }
  }

  Map getEmployeesToExportImss(Paysheet paysheet) {
    Map employees = [:]
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'TOTAL IMSS']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'imssSalaryNet']
    employees.data = paysheet.employees.sort {it.prePaysheetEmployee.nameEmployee}
    employees
  }

  def exportPaysheetToXlsAssimilable(Paysheet paysheet) {
    Map employees = getEmployeesToExportAssimilable(paysheet)
    new WebXlsxExporter().with {
      fillRow(["PROYECTO:", paysheet.prePaysheet.paysheetProject, "NÓMINA ASIMILABLES"],0)
      fillRow(["PERIODO DE PAGO:", paysheet.prePaysheet.paymentPeriod, "DEL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.initPeriod), "AL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.endPeriod)],1)
      fillRow(["RESIDENTE:", paysheet.prePaysheet.accountExecutive,"TOTAL:", paysheet.total], 2)
      fillRow(employees.headers, 4)
      add(employees.data, employees.properties, 5)
    }
  }

  Map getEmployeesToExportAssimilable(Paysheet paysheet) {
    Map employees = [:]
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'ASIMILABLE']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryAssimilable']
    employees.data = paysheet.employees.sort {it.prePaysheetEmployee.nameEmployee}
    employees
  }

  def getBanksAccountsToPaymentDispersion(Paysheet paysheet) {
		def distinctBanksEmployees = [] as Set
		paysheet.employees.each { emp ->
			distinctBanksEmployees.add(emp.prePaysheetEmployee.bank)
		}
    def bankAccounts = paysheet.company.banksAccounts.collect { ba ->
			if (distinctBanksEmployees.contains(ba.banco)) { return ba }
		}.grep() 
		bankAccounts
  }

	@Transactional
  def generateDispersionFilesFromPaysheet(Paysheet paysheet, Map dispersionData) {
		deleteCurrentDispersionFilesFromPaysheet(paysheet)
    dispersionData = complementDispersionData(dispersionData)
		generateDispersionFileSameBank(paysheet, dispersionData)
		//generateDispersionFileInterBank(paysheet, dispersionData)
  }

	def deleteCurrentDispersionFilesFromPaysheet(Paysheet paysheet){
		def listFiles = paysheet.dispersionFiles.collect { it }
		listFiles.each {
			paysheet.removeFromDispersionFiles(it)
		}
		paysheet.save()
	}

  Map complementDispersionData(Map dispersionData) {
		List idsChargeBankAccounts = Arrays.asList(dispersionData.chargeBankAccountsIds)
    List<BankAccount> chargeBankAccountsList = BankAccount.findAllByIdInList(idsChargeBankAccounts)
    dispersionData.chargeBankAccountsList = chargeBankAccountsList
    dispersionData
  }

	def generateDispersionFileSameBank(Paysheet paysheet, Map dispersionData){
		List dispersionFiles = []
		dispersionData.chargeBankAccountsList.each { chargeBankAccount ->
			Map dispersionDataForBank = prepareDispersionDataForBank(paysheet, chargeBankAccount, dispersionData.paymentMessage)
			List files = createDispersionFilesForDispersionData(dispersionDataForBank)
			List s3Files = uploadDispersionFilesToS3(files)
			dispersionFiles.addAll(s3Files)
		}

		addingDispersionFilesToPaysheet(paysheet, dispersionFiles)
		log.info "Files dispersion same bank generated"
	}

	Map prepareDispersionDataForBank(Paysheet paysheet, BankAccount chargeBankAccount, String paymentMessage){
		List<PaysheetEmployee> employees = getPaysheetEmployeesForBank(paysheet.employees, chargeBankAccount.banco)
		Map dispersionDataForBank = [employees: employees, chargeBankAccount:chargeBankAccount, paymentMessage:paymentMessage]
	}

  List<PaysheetEmployee> getPaysheetEmployeesForBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank==bank) {
        employee
      }
    }.grep()
  }

	List createDispersionFilesForDispersionData(Map dispersionDataForBank){
		List dispersionFiles = []

		String methodCreatorSATxtFileDispersion = getMethodCreatorOfSATxtDispersionFile(dispersionDataForBank.chargeBankAccount.banco.name)
		String methodCreatorIASTxtFileDispersion = getMethodCreatorOfIASTxtDispersionFile(dispersionDataForBank.chargeBankAccount.banco.name)

    File dispersionFileSAForBank = "${methodCreatorSATxtFileDispersion}"(dispersionDataForBank)
    File dispersionFileIASForBank = "${methodCreatorIASTxtFileDispersion}"(dispersionDataForBank)
		
		dispersionFiles.add(dispersionFileSAForBank)
		dispersionFiles.add(dispersionFileIASForBank)
		dispersionFiles
	}

	List uploadDispersionFilesToS3(List files){
		List s3Files = []
		files.each { file ->
			def s3DispersionFile = s3AssetService.createFileToUpload(file, "${file.name.replaceAll('[0-9]','')}")
			s3Files.add(s3DispersionFile)
		}
		s3Files
	}

	String getMethodCreatorOfSATxtDispersionFile(String bankName) {
		bankName = bankName.replace(" ","")
		String methodCreatorSATxtFileDispersion = "createTxtDispersionFileSADefault"
		if (this.metaClass.respondsTo(this, "createTxtDispersionFileSAFor${bankName}")) {
			methodCreatorSATxtFileDispersion = "createTxtDispersionFileSAFor${bankName}"
		}
		methodCreatorSATxtFileDispersion
	}

	String getMethodCreatorOfIASTxtDispersionFile(String bankName) {
		bankName = bankName.replace(" ","")
		String methodCreatorIASTxtFileDispersion = "createTxtDispersionFileIASDefault"
		if (this.metaClass.respondsTo(this, "createTxtDispersionFileIASFor${bankName}")) {
			methodCreatorIASTxtFileDispersion = "createTxtDispersionFileIASFor${bankName}"
		}
		methodCreatorIASTxtFileDispersion
	}

	//TODO: Definir el layout a usar por default, actualmente es igual al de BBVA
  File createTxtDispersionFileSADefault(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank SA Default for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionSADefault",".txt")
    dispersionDataForBank.employees.each { employee ->
      log.info "Payment dispersion same bank SA Default record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String message = "SSA-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"
      String amount = (new DecimalFormat('##0.00').format(employee.imssSalaryNet)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

	//TODO: Definir el layout a usar por default, actualmente es igual al de BBVA
  File createTxtDispersionFileIASDefault(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank IAS Default for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionIASDefault",".txt")
    dispersionDataForBank.employees.each { employee ->
      log.info "Payment dispersion same bank IAS Default record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String message = "IAS-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"
      String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileSAForBBVABANCOMER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank SA BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionSABBVA",".txt")
    dispersionDataForBank.employees.each { employee ->
      log.info "Payment dispersion same bank SA BBVA record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String message = "SSA-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"
      String amount = (new DecimalFormat('##0.00').format(employee.imssSalaryNet)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileIASForBBVABANCOMER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank IAS BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionIASBBVA",".txt")
    dispersionDataForBank.employees.each { employee ->
      log.info "Payment dispersion same bank IAS BBVA record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String message = "IAS-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"
      String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  String clearSpecialCharsFromString(String text) {
    text.toUpperCase().replace("Ñ","N").replace("Á","A").replace("É","E").replace("Í","I").replace("Ó","O").replace("Ú","U").replace("Ü","U").replaceAll("[^a-zA-Z0-9 ]","")
  }

	def addingDispersionFilesToPaysheet(Paysheet paysheet, List s3Files){
		s3Files.each { file ->
			paysheet.addToDispersionFiles(file)
		}
		paysheet.save()
	}

//TODO: Los interbancarios, se harán con M1
	def generateDispersionFileInterBank(Paysheet paysheet, Map dispersionData){
    List<PaysheetEmployee> employees = getPaysheetEmployeesForInterBank(paysheet.employees, dispersionData.chargeBankAccount.banco)
    File dispersionFileInterBank = createTxtDispersionFileForInterBank(employees, dispersionData)
	}

  File createTxtDispersionFileForInterBank(List<PaysheetEmployee> employees, Map dispersionData) {
    log.info "Payment dispersion iinterbank for employees: ${employees}"
    File file = File.createTempFile("txtDispersion",".txt")
    employees.each { employee ->
      log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.clabe.padLeft(18,'0')
      String sourceAccount = dispersionData.chargeBankAccount.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String cleanedName = clearSpecialCharsFromString(employee.prePaysheetEmployee.nameEmployee)
      String nameEmployee = cleanedName.length()>30 ? cleanedName.substring(0,30) : cleanedName.padRight(30,' ')
      String typeAccount = "40"
      String bankingCode = employee.prePaysheetEmployee.bank.bankingCode
      String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
      String reference = new Date().format("ddMMyy").padLeft(7,'0')
      String disp = "H"      
			["imssSalaryNet", "salaryAssimilable"].each { salary ->
      	String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).padLeft(16,'0')
      	file.append("${destinyAccount}${sourceAccount}${currency}${amount}${nameEmployee}${typeAccount}${bankingCode}${message}${reference}${disp}\n")
			}
    }
    log.info "File created: ${file.text}"
    file
  }

  List<PaysheetEmployee> getPaysheetEmployeesForInterBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank!=bank) {
        employee
      }
    }.grep()
  }

}
