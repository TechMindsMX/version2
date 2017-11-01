package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import java.math.RoundingMode

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
      paysheetContract:prePaysheet.paysheetContract
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
    def bankAccounts = paysheet.paysheetContract.company.banksAccounts.collect { ba ->
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
		List idsChargeBankAccounts = Arrays.asList(dispersionData.dispersionAccount)
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
		Map dispersionDataForBank = [employees: employees, chargeBankAccount:chargeBankAccount, paymentMessage:dispersionData.paymentMessage, applyDate:dispersionData.applyDate, idPaysheet:paysheet.id, sequence:dispersionData.sequence, nameCompany:dispersionData.nameCompany]
	}

  List<PaysheetEmployee> getPaysheetEmployeesForBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank==bank && employee.paymentWay == PaymentWay.BANKING) {
        employee
      }
    }.grep()
  }

	List createDispersionFilesForDispersionData(Map dispersionDataForBank){
		List dispersionFiles = []

		String methodCreatorTxtFileDispersion = getMethodCreatorOfTxtDispersionFile(dispersionDataForBank.chargeBankAccount.banco.name)

    File dispersionFileSAForBank = "${methodCreatorTxtFileDispersion}"(dispersionDataForBank, "SA")
    File dispersionFileIASForBank = "${methodCreatorTxtFileDispersion}"(dispersionDataForBank, "IAS")
		
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

	String getMethodCreatorOfTxtDispersionFile(String bankName) {
		bankName = bankName.replace(" ","")
		String methodCreatorTxtFileDispersion = "createTxtDispersionFileDefault"
		if (this.metaClass.respondsTo(this, "createTxtDispersionFileFor${bankName}")) {
			methodCreatorTxtFileDispersion = "createTxtDispersionFileFor${bankName}"
		}
		methodCreatorTxtFileDispersion
	}

	//TODO: Definir el layout a usar por default, actualmente es igual al de BBVA
  File createTxtDispersionFileDefault(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} Default for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_Default",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "salaryAssimilable"
		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "${schema.padLeft(3,'S')}-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')

      String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForBBVABANCOMER(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_BBVA",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "salaryAssimilable"
		String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "${schema.padLeft(3,'S')}-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).padLeft(16,'0')
			file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForSANTANDER(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} SANTANDER for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_SANTANDER",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "salaryAssimilable"
    String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padRight(11,'  ')
		//HEADER
		String header = "100001E${new Date().format('MMddyyyy')}${sourceAccount}     ${dispersionDataForBank.applyDate.format('MMddyyyy')}"
		file.append("${header}\n")

		//DETAIL
		BigDecimal total = new BigDecimal(0)
    dispersionDataForBank.employees.eachWithIndex { employee, index ->
			String counter = "2${(index+2).toString().padLeft(5,'0')}"
			String employeeNumberCleaned = clearSpecialCharsFromString(employee.prePaysheetEmployee.numberEmployee ?: "")
			String employeeNumber = employeeNumberCleaned ? (employeeNumberCleaned.length() > 7 ? employeeNumberCleaned.substring(0,7) : employeeNumberCleaned.padRight(7,' ')) : " ".padRight(7, " ") 
			
			BusinessEntity businessEntityEmployee = BusinessEntity.findByRfc(employee.prePaysheetEmployee.rfc)

			String lastName = clearSpecialCharsFromString((businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_PATERNO }.value : " ")).padRight(30," ")
			String motherLastName = clearSpecialCharsFromString((businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.APELLIDO_MATERNO }.value : " ")).padRight(20," ")
			String name = clearSpecialCharsFromString((businessEntityEmployee ? businessEntityEmployee.names.find { it.type == NameType.NOMBRE }.value : " ")).padRight(30," ")
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(16,' ')
      String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).replace(".","").padLeft(18,'0')
      String concept = "01"
			file.append("${counter}${employeeNumber}${lastName}${motherLastName}${name}${destinyAccount}${amount}${concept}\n".toUpperCase())
			total += employee."${salary}"
    }

		//FOOTER
		String footer = "3${(dispersionDataForBank.employees.size()+1).toString().padLeft(5,'0')}${dispersionDataForBank.employees.size().toString().padLeft(5,'0')}${(new DecimalFormat('##0.00').format(total)).replace(".","").padLeft(18,'0')}"
		file.append("${footer}\n")
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForBANAMEX(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} BANAMEX for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_BANAMEX",".txt")
		
		if (!dispersionDataForBank.chargeBankAccount.clientNumber){
			log.info "La cuenta no tiene registrado el número de cliente"
			file.append("La cuenta Banamex ${dispersionDataForBank.chargeBankAccount.accountNumber} no tiene número de cliente registrado")
		} else {

		log.info "Dispersion data for bank: ${dispersionDataForBank}"
		String salary = schema == "SA" ? "imssSalaryNet" : "salaryAssimilable"
    String sourceAccount = dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(7,"0")
		String message = clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(20," ")
		String lineControl = "1${dispersionDataForBank.chargeBankAccount.clientNumber.padLeft(12,'0')}${dispersionDataForBank.applyDate.format('yyMMdd')}${dispersionDataForBank.sequence.padLeft(4,'0')}${clearSpecialCharsFromString(dispersionDataForBank.nameCompany).padRight(36,'  ')}${message}15D01"
		file.append("${lineControl}\n")
		BigDecimal totalDispersion = dispersionDataForBank.employees*."${salary}".sum().setScale(2, RoundingMode.HALF_UP)
		String lineGlobal = "21001${((totalDispersion*100).intValue()).toString().padLeft(18,'0')}03${dispersionDataForBank.chargeBankAccount.branchNumber.padLeft(13,'0')}${dispersionDataForBank.chargeBankAccount.accountNumber.padLeft(7,'0')}${dispersionDataForBank.employees.size().toString().padLeft(6,'0')}"
		file.append("${lineGlobal}\n")
		dispersionDataForBank.employees.eachWithIndex { employee, index ->
			String amount = (employee."${salary}".setScale(2, RoundingMode.HALF_UP)*100).intValue().toString().padLeft(18,"0")
      String destinyBranchAccount = employee.prePaysheetEmployee.clabe.substring(3,6).padLeft(13,"0")
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(7," ")
			String employeeNumberCleaned = clearSpecialCharsFromString(employee.prePaysheetEmployee.numberEmployee ?: "")
			String reference = "${dispersionDataForBank.idPaysheet}${employeeNumberCleaned ?: index}".padRight(16," ")
			BusinessEntity businessEntityEmployee = BusinessEntity.findByRfc(employee.prePaysheetEmployee.rfc)
			String fullName = clearSpecialCharsFromString(businessEntityEmployee.toString().length()>55 ? businessEntityEmployee.toString().substring(0,55) : businessEntityEmployee.toString()).padRight(55, " ")
			String ending = "${''.padRight(140,' ')}000000${''.padRight(152,' ')}"
			String lineEmployee = "3000101001${amount}01${destinyBranchAccount}${destinyAccount}${reference}${fullName}${ending}"
			file.append("${lineEmployee}\n")
		}
		String lineTotals = "4001${dispersionDataForBank.employees.size().toString().padLeft(6,'0')}${(totalDispersion*100).intValue().toString().padLeft(18,'0')}000001${(totalDispersion*100).intValue().toString().padLeft(18,'0')}"
		file.append("${lineTotals}\n")

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
    List<PaysheetEmployee> employees = getPaysheetEmployeesForInterBank(paysheet.employees, dispersionData.chargeBankAccountsList)
		if (employees) {
			Map dispersionDataInterBank = [employees: employees, paymentMessage:dispersionData.paymentMessage]
			File dispersionFileSAInterBank = createDispersionFileInterBank(dispersionDataInterBank, "SA")
			File dispersionFileIASInterBank = createDispersionFileInterBank(dispersionDataInterBank, "IAS")
			List dispersionFiles = [dispersionFileSAInterBank, dispersionFileIASInterBank]
			List s3Files = uploadDispersionFilesToS3(dispersionFiles)
			addingDispersionFilesToPaysheet(paysheet, s3Files)
		}
	}

  List<PaysheetEmployee> getPaysheetEmployeesForInterBank(def allEmployees, List chargeBankAccountsList) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank && !chargeBankAccountsList.find { it.banco==employee.prePaysheetEmployee.bank } && employee.paymentWay == PaymentWay.BANKING) {
        employee
      }
    }.grep()
  }

  File createDispersionFileInterBank(Map dispersionData, String schema) {
    log.info "Payment dispersion ${schema} interbank for employees: ${dispersionData.employees}"
    File file = File.createTempFile("dispersion_${schema}_InterBank",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "salaryAssimilable"
		String sourceAccount = "M1Account".padLeft(18,'0')
		String currency = "MXN"
		String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
		String reference = new Date().format("ddMMyy").padLeft(7,'0')
		String typeAccount = "40"
		String disp = "H"      

    dispersionData.employees.each { employee ->
      log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.clabe.padLeft(18,'0')
      String cleanedName = clearSpecialCharsFromString(employee.prePaysheetEmployee.nameEmployee)
      String nameEmployee = cleanedName.length()>30 ? cleanedName.substring(0,30) : cleanedName.padRight(30,' ')
      String bankingCode = employee.prePaysheetEmployee.bank.bankingCode
			String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).padLeft(16,'0')
     	file.append("${destinyAccount}${sourceAccount}${currency}${amount}${nameEmployee}${typeAccount}${bankingCode}${message}${reference}${disp}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  def exportPaysheetToXlsCash(Paysheet paysheet) {
    Map employees = getEmployeesOnCashToExport(paysheet)
    new WebXlsxExporter().with {
      fillRow(["PROYECTO:", paysheet.prePaysheet.paysheetProject, "NÓMINA EFECTIVO/CHEQUE"],0)
      fillRow(["PERIODO DE PAGO:", paysheet.prePaysheet.paymentPeriod, "DEL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.initPeriod), "AL:", new SimpleDateFormat("dd-MM-yyyy").format(paysheet.prePaysheet.endPeriod)],1)
      fillRow(["RESIDENTE:", paysheet.prePaysheet.accountExecutive,"TOTAL:", paysheet.total], 2)
      fillRow(employees.headers, 4)
      add(employees.data, employees.properties, 5)
    }
  }

  Map getEmployeesOnCashToExport(Paysheet paysheet) {
    Map employees = [:]
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'TOTAL IMSS', 'ASIMILABLE', 'SUBTOTAL', 'CARGA SOCIAL EMPRESA', 'ISN', 'COSTO NOMINAL', 'COMISION', 'TOTAL NÓMINA', 'IVA', 'TOTAL A FACTURAR']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'imssSalaryNet', 'salaryAssimilable', 'totalSalaryEmployee', 'socialQuotaEmployer', 'paysheetTax', 'paysheetCost', 'commission', 'paysheetTotal', 'paysheetIva', 'totalToInvoice']
    employees.data = paysheet.employees.findAll { emp -> !emp.prePaysheetEmployee.bank }.sort { it.prePaysheetEmployee.nameEmployee }
    employees
  }

	List prepareDispersionSummary(Paysheet paysheet){
		List summary = []
		List bankAccounts = getBanksAccountsToPaymentDispersion(paysheet)
		def banks = getListBanksFromBankAccountsToPaymentDispersion(bankAccounts)
		banks.each { bank ->
			Map summaryBank = [:]
			summaryBank.bank = bank
			summaryBank.accounts = bankAccounts.collect { ba -> if (ba.banco == bank) { ba } }.grep()
			summaryBank.totalSA = paysheet.employees.findAll{ e-> if(e.prePaysheetEmployee.bank==bank){ return e} }*.imssSalaryNet.sum()
			summaryBank.totalIAS = paysheet.employees.findAll{ e-> if(e.prePaysheetEmployee.bank==bank){ return e} }*.salaryAssimilable.sum()
			summaryBank.type = "SameBank"
			summary.add(summaryBank)
		}
		//inter bank data
		summary = addInterBankSummary(summary, paysheet, banks)
		summary
	}

	def getListBanksFromBankAccountsToPaymentDispersion(List bankAccounts){
		def banks = [] as Set
		bankAccounts.each { ba ->
			banks.add(ba.banco)
		}
		banks
	}

	def addInterBankSummary(List summary, Paysheet paysheet, def banks){
		Map summaryInterBank = [:]
		summaryInterBank.bank = Bank.findByName("STP")
		summaryInterBank.accounts = paysheet.company.accounts.first()
		summaryInterBank.totalSA = paysheet.employees.findAll{ e-> if(!banks.contains(e.prePaysheetEmployee.bank)){ return e} }*.imssSalaryNet.sum()
		summaryInterBank.totalIAS = paysheet.employees.findAll{ e-> if(!banks.contains(e.prePaysheetEmployee.bank)){ return e} }*.salaryAssimilable.sum()
		summaryInterBank.type = "InterBank"
		summary.add(summaryInterBank)
		summary
	}

}
