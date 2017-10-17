package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat
import java.text.DecimalFormat

import com.modulus.uno.Bank
import com.modulus.uno.BankAccount
import com.modulus.uno.S3AssetService
import com.modulus.uno.BusinessEntity
import com.modulus.uno.NameType

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
  	Locale.setDefault(new Locale("es","MX"));
		deleteCurrentDispersionFilesFromPaysheet(paysheet)
    dispersionData = complementDispersionData(dispersionData)
		generateDispersionFileSameBank(paysheet, dispersionData)
		generateDispersionFileInterBank(paysheet, dispersionData)
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
		dispersionData.applyDate = dispersionData.applyDate ? Date.parse("dd/MM/yyyy", dispersionData.applyDate) : null
    dispersionData
  }

	def generateDispersionFileSameBank(Paysheet paysheet, Map dispersionData){
		List dispersionFiles = []
		dispersionData.chargeBankAccountsList.each { chargeBankAccount ->
			Map dispersionDataForBank = prepareDispersionDataForBank(paysheet, chargeBankAccount, dispersionData)
			List files = createDispersionFilesForDispersionData(dispersionDataForBank)
			List s3Files = uploadDispersionFilesToS3(files)
			dispersionFiles.addAll(s3Files)
		}

		addingDispersionFilesToPaysheet(paysheet, dispersionFiles)
		log.info "Files dispersion same bank generated"
	}

	Map prepareDispersionDataForBank(Paysheet paysheet, BankAccount chargeBankAccount, Map dispersionData){
		List<PaysheetEmployee> employees = getPaysheetEmployeesForBank(paysheet.employees, chargeBankAccount.banco)
		Map dispersionDataForBank = [employees: employees, chargeBankAccount:chargeBankAccount, paymentMessage:dispersionData.paymentMessage, applyDate:dispersionData.applyDate]
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
		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "SSA-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
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
		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "IAS-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileSAForBBVABANCOMER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank SA BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionSABBVA",".txt")
		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "SSA-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String amount = (new DecimalFormat('##0.00').format(employee.imssSalaryNet)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileIASForBBVABANCOMER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank IAS BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionIASBBVA",".txt")

		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "IAS-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')

      String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileSAForSANTANDER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank SA SANTANDER for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionSASANTANDER",".txt")
    String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padRight(11,'  ')
		//HEADER
		String header = "100001E${new Date().format('MMddyyyy')}${sourceAccount}     ${dispersionDataForBank.applyDate.format('MMddyyyy')}"
		file.append("${header}\n")

		//DETAIL
		BigDecimal total = new BigDecimal(0)
    dispersionDataForBank.employees.eachWithIndex { employee, index ->
			String counter = "2${(index+2).toString().padLeft(5,'0')}"
			String employeeNumber = employee.prePaysheetEmployee.numberEmployee ? (employee.prePaysheetEmployee.numberEmployee.length() > 7 ? employee.prePaysheetEmployee.numberEmployee.substring(0,7) : employee.prePaysheetEmployee.numberEmployee.padRight(7,' ')) : " ".padRight(7, " ") 
			
			BusinessEntity businessEntityEmployee = BusinessEntity.findByRfc(employee.prePaysheetEmployee.rfc)

			String lastName = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_PATERNO }.value : " ").padRight(30," ")
			String motherLastName = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_MATERNO }.value : " ").padRight(20," ")
			String name = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.NOMBRE }.value : " ").padRight(30," ")
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(16,' ')
      String amount = (new DecimalFormat('##0.00').format(employee.imssSalaryNet)).replace(".","").padLeft(18,'0')
      String concept = "01"
			file.append("${counter}${employeeNumber}${lastName}${motherLastName}${name}${destinyAccount}${amount}${concept}\n".toUpperCase())
			total += employee.imssSalaryNet
    }

		//FOOTER
		String footer = "3${(dispersionDataForBank.employees.size()+1).toString().padLeft(5,'0')}${dispersionDataForBank.employees.size().toString().padLeft(5,'0')}${(new DecimalFormat('##0.00').format(total)).replace(".","").padLeft(18,'0')}"
		file.append("${footer}\n")
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileIASForSANTANDER(Map dispersionDataForBank) {
    log.info "Payment dispersion same bank IAS SANTANDER for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("txtDispersionIASSANTANDER",".txt")
    String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padRight(11,'  ')
		//HEADER
		String header = "100001E${new Date().format('MMddyyyy')}${sourceAccount}     ${dispersionDataForBank.applyDate.format('MMddyyyy')}"
		file.append("${header}\n")

		//DETAIL
		BigDecimal total = new BigDecimal(0)
    dispersionDataForBank.employees.eachWithIndex { employee, index ->
			String counter = "2${(index+2).toString().padLeft(5,'0')}"
			String employeeNumber = employee.prePaysheetEmployee.numberEmployee ? (employee.prePaysheetEmployee.numberEmployee.length() > 7 ? employee.prePaysheetEmployee.numberEmployee.substring(0,7) : employee.prePaysheetEmployee.numberEmployee.padRight(7,' ')) : " ".padRight(7, " ") 
			
			BusinessEntity businessEntityEmployee = BusinessEntity.findByRfc(employee.prePaysheetEmployee.rfc)

			String lastName = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_PATERNO }.value : " ").padRight(30," ")
			String motherLastName = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_MATERNO }.value : " ").padRight(20," ")
			String name = (businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.NOMBRE }.value : " ").padRight(30," ")
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(16,' ')
      String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).replace(".","").padLeft(18,'0')
      String concept = "01"
			file.append("${counter}${employeeNumber}${lastName}${motherLastName}${name}${destinyAccount}${amount}${concept}\n".toUpperCase())
			total += employee.salaryAssimilable
    }

		//FOOTER
		String footer = "3${(dispersionDataForBank.employees.size()+1).toString().padLeft(5,'0')}${dispersionDataForBank.employees.size().toString().padLeft(5,'0')}${(new DecimalFormat('##0.00').format(total)).replace(".","").padLeft(18,'0')}"
		file.append("${footer}\n")
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
    List<PaysheetEmployee> employees = getPaysheetEmployeesForInterBank(paysheet.employees, dispersionData.chargeBankAccountsList)
		if (employees) {
			Map dispersionDataInterBank = [employees: employees, paymentMessage:dispersionData.paymentMessage]
			File dispersionFileSAInterBank = createDispersionFileSAInterBank(dispersionDataInterBank)
			File dispersionFileIASInterBank = createDispersionFileIASInterBank(dispersionDataInterBank)
			List dispersionFiles = [dispersionFileSAInterBank, dispersionFileIASInterBank]
			List s3Files = uploadDispersionFilesToS3(dispersionFiles)
			addingDispersionFilesToPaysheet(paysheet, s3Files)
		}
	}

  List<PaysheetEmployee> getPaysheetEmployeesForInterBank(def allEmployees, List chargeBankAccountsList) {
    allEmployees.collect { employee ->
      if (!chargeBankAccountsList.find { it.banco==employee.prePaysheetEmployee.bank }) {
        employee
      }
    }.grep()
  }

  File createDispersionFileSAInterBank(Map dispersionData) {
    log.info "Payment dispersion SA interbank for employees: ${dispersionData.employees}"
    File file = File.createTempFile("txtDispersionSAInterBank",".txt")
    dispersionData.employees.each { employee ->
      log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.clabe.padLeft(18,'0')
      String sourceAccount = "M1Account".padLeft(18,'0')
      String currency = "MXN"
      String cleanedName = clearSpecialCharsFromString(employee.prePaysheetEmployee.nameEmployee)
      String nameEmployee = cleanedName.length()>30 ? cleanedName.substring(0,30) : cleanedName.padRight(30,' ')
      String typeAccount = "40"
      String bankingCode = employee.prePaysheetEmployee.bank.bankingCode
      String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
      String reference = new Date().format("ddMMyy").padLeft(7,'0')
      String disp = "H"      
			String amount = (new DecimalFormat('##0.00').format(employee.imssSalaryNet)).padLeft(16,'0')
     	file.append("${destinyAccount}${sourceAccount}${currency}${amount}${nameEmployee}${typeAccount}${bankingCode}${message}${reference}${disp}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  File createDispersionFileIASInterBank(Map dispersionData) {
    log.info "Payment dispersion IAS interbank for employees: ${dispersionData.employees}"
    File file = File.createTempFile("txtDispersionIASInterBank",".txt")
    dispersionData.employees.each { employee ->
      log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.clabe.padLeft(18,'0')
      String sourceAccount = "M1Account".padLeft(18,'0')
      String currency = "MXN"
      String cleanedName = clearSpecialCharsFromString(employee.prePaysheetEmployee.nameEmployee)
      String nameEmployee = cleanedName.length()>30 ? cleanedName.substring(0,30) : cleanedName.padRight(30,' ')
      String typeAccount = "40"
      String bankingCode = employee.prePaysheetEmployee.bank.bankingCode
      String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
      String reference = new Date().format("ddMMyy").padLeft(7,'0')
      String disp = "H"      
			String amount = (new DecimalFormat('##0.00').format(employee.salaryAssimilable)).padLeft(16,'0')
     	file.append("${destinyAccount}${sourceAccount}${currency}${amount}${nameEmployee}${typeAccount}${bankingCode}${message}${reference}${disp}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

}
