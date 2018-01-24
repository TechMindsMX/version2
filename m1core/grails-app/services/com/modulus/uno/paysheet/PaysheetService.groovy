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
  PaysheetProjectService paysheetProjectService
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
		validatePaymentsOfPaysheet(paysheet)
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

	def validatePaymentsOfPaysheet(Paysheet paysheet) {
		List<PaysheetEmployee> wrongEmployees = getEmployeesWithWrongPayment(paysheet)
		if (wrongEmployees) {
			rejectPaysheetAndPrePaysheetForWrongPayments(paysheet, wrongEmployees)
		} else {
    	prePaysheetService.changeStatusToProcessed(paysheet.prePaysheet)
		}
	
	}

	List<PaysheetEmployee> getEmployeesWithWrongPayment(Paysheet paysheet) {
		List<PaysheetEmployee> wrongEmployees = []
		paysheet.employees.each { employee ->
			BigDecimal difference = employee.totalSalaryEmployee - employee.prePaysheetEmployee.netPayment
			if (difference.abs() > 1) {
				wrongEmployees.add(employee)
			}
		}
		wrongEmployees
	}

	Paysheet rejectPaysheetAndPrePaysheetForWrongPayments(Paysheet paysheet, List<PaysheetEmployee> wrongEmployees) {
		paysheet.status = PaysheetStatus.REJECTED
		String wrongRfcs = (wrongEmployees*.prePaysheetEmployee.rfc).join(",")
		paysheet.rejectReason = "LOS SIGUIENTES EMPLEADOS RESULTARON CON UN NETO A PAGAR DISTINTO AL INDICADO EN LA PRENÓMINA: ${wrongRfcs}"
		paysheet.save()
    prePaysheetService.reject(paysheet.prePaysheet)
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
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'INCIDENCIAS PERCEP', 'INCIDENCIAS DEDUC', 'TOTAL IMSS', 'ASIMILABLE BRUTO', 'ISR ASIMILABLE', 'ASIMILABLE NETO', 'SUBTOTAL', 'CARGA SOCIAL EMPRESA', 'ISN', 'COSTO NOMINAL', 'COMISION', 'TOTAL NÓMINA', 'IVA', 'TOTAL A FACTURAR']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'totalIncidencesImssPerceptions', 'totalIncidencesImssDeductions', 'imssSalaryNet', 'crudeAssimilable', 'incomeTaxAssimilable', 'netAssimilable', 'totalSalaryEmployee', 'socialQuotaEmployer', 'paysheetTax', 'paysheetCost', 'commission', 'paysheetTotal', 'paysheetIva', 'totalToInvoice']
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
    prePaysheetService.reject(paysheet.prePaysheet)
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
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'INCIDENCIAS PERCEP', 'INCIDENCIAS DEDUC', 'TOTAL IMSS']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'totalIncidencesImssPerceptions', 'totalIncidencesImssDeductions', 'imssSalaryNet']
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
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'ASIMILABLE BRUTO', 'ISR ASIMILABLE', 'ASIMILABLE NETO']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'crudeAssimilable', 'incomeTaxAssimilable', 'netAssimilable']
    employees.data = paysheet.employees.sort {it.prePaysheetEmployee.nameEmployee}
    employees
  }

	@Transactional
  def generateDispersionFilesFromPaysheet(Paysheet paysheet, Map dispersionData) {
  	Locale.setDefault(new Locale("es","MX"))
		deleteCurrentDispersionFilesFromPaysheet(paysheet)
    dispersionData = complementDispersionData(dispersionData)
		generateDispersionFiles(paysheet, dispersionData)
		/*generateDispersionFileSameBank(paysheet, dispersionData)
		generateDispersionFileInterBank(paysheet, dispersionData)*/
  }

	def deleteCurrentDispersionFilesFromPaysheet(Paysheet paysheet){
		def listFiles = paysheet.dispersionFiles.collect { it }
		listFiles.each {
			paysheet.removeFromDispersionFiles(it)
		}
		paysheet.save()
	}

  Map complementDispersionData(Map dispersionData) {
		List idsBanks = Arrays.asList(dispersionData.bank)
		List idsSaBankAccounts = dispersionData.saBankAccount ? Arrays.asList(dispersionData.saBankAccount) : []
		List idsIasBankAccounts = dispersionData.iasBankAccount ? Arrays.asList(dispersionData.iasBankAccount) : []
    List<Bank> banks = Bank.findAllByIdInList(idsBanks)
    List<BankAccount> saBankAccounts = idsSaBankAccounts ? BankAccount.findAllByIdInList(idsSaBankAccounts) : []
    List<BankAccount> iasBankAccounts = idsIasBankAccounts ? BankAccount.findAllByIdInList(idsIasBankAccounts) : []
    dispersionData.banks = banks
    dispersionData.saBankAccounts = saBankAccounts
    dispersionData.iasBankAccounts = iasBankAccounts
		dispersionData.applyDate = dispersionData.applyDate ? Date.parse("dd/MM/yyyy", dispersionData.applyDate) : null
    dispersionData
  }

	def generateDispersionFiles(Paysheet paysheet, Map dispersionData){
		List dispersionFiles = []
		dispersionData.banks.eachWithIndex { bank, index ->
      if (bank.name!="STP") {
        Map dispersionDataForBank = prepareDispersionDataForBank(paysheet, bank, dispersionData)
        dispersionDataForBank.saBankAccount = dispersionData.saBankAccounts.find { ba -> ba.banco == bank }
        dispersionDataForBank.iasBankAccount = dispersionData.iasBankAccounts.find { ba -> ba.banco == bank }
        dispersionDataForBank = getPayersForPaysheetAndBank(paysheet, dispersionDataForBank)
        List files = createDispersionFilesForDispersionData(dispersionDataForBank)
        List s3Files = uploadDispersionFilesToS3(files)
        dispersionFiles.addAll(s3Files)
		    addingDispersionFilesToPaysheet(paysheet, dispersionFiles)
      } else {
        generateDispersionFileInterBank(paysheet, dispersionData)
      }
		}

		log.info "Files dispersion files generated"
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

	Map prepareDispersionDataForBank(Paysheet paysheet, Bank bank, Map dispersionData){
		List<PaysheetEmployee> employees = getPaysheetEmployeesForBank(paysheet.employees, bank)
		[employees:employees, paymentMessage:dispersionData.paymentMessage, applyDate:dispersionData.applyDate, idPaysheet:paysheet.id, sequence:dispersionData.sequence]
	}

  List<PaysheetEmployee> getPaysheetEmployeesForBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank==bank && employee.paymentWay == PaymentWay.BANKING) {
        employee
      }
    }.grep()
  }

  Map getPayersForPaysheetAndBank(Paysheet paysheet, Map dispersionDataForBank) {
    List payers = getPayersToPaymentDispersion(paysheet)
    payers.each { payer ->
      if (payer.company.banksAccounts.contains(dispersionDataForBank.saBankAccount)) {
        dispersionDataForBank.saPayer = payer.company.bussinessName
      } else if (payer.company.banksAccounts.contains(dispersionDataForBank.iasBankAccount)) {
        dispersionDataForBank.iasPayer = payer.company.bussinessName
      }
    }
    dispersionDataForBank
  }

	List createDispersionFilesForDispersionData(Map dispersionDataForBank){
		List dispersionFiles = []

		String methodCreatorTxtFileDispersionSA = getMethodCreatorOfTxtDispersionFile(dispersionDataForBank.saBankAccount.banco.name)
		String methodCreatorTxtFileDispersionIAS = getMethodCreatorOfTxtDispersionFile(dispersionDataForBank.iasBankAccount.banco.name)

    File dispersionFileSAForBank = "${methodCreatorTxtFileDispersionSA}"(dispersionDataForBank, "SA")
    File dispersionFileIASForBank = "${methodCreatorTxtFileDispersionIAS}"(dispersionDataForBank, "IAS")
		
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

		String salary = schema == "SA" ? "imssSalaryNet" : "netAssimilable"
		String account = schema == "SA" ? "saBankAccount" : "iasBankAccount"
		String sourceAccount = dispersionDataForBank."${account}".accountNumber.padLeft(18,'0')
		String currency = "MXN"
		String message = "${schema.padLeft(3,'S')}-${clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(26,' ')}"

    dispersionDataForBank.employees.each { employee ->
      if (employee."${salary}" > 0) {
        String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')

        String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).padLeft(16,'0')
        file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
      }
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForBBVABANCOMER(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} BBVA for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_BBVA",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "netAssimilable"
		String account = schema == "SA" ? "saBankAccount" : "iasBankAccount"
    String rfc = "".padLeft(16," ")
    String type = "99"
    String bank = "001"
    String branch = "001"

    dispersionDataForBank.employees.eachWithIndex { employee, index ->
      if (employee."${salary}" > 0) {
        String counter = "${index+1}".padLeft(9,"0")
        String destinyAccount = employee.prePaysheetEmployee.account.padRight(20,' ')
        String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).replace(".","").padLeft(15,'0')
        String adjustName = employee.prePaysheetEmployee.nameEmployee.length() > 40 ? employee.prePaysheetEmployee.nameEmployee.substring(0,40) : employee.prePaysheetEmployee.nameEmployee
        String name = clearSpecialCharsFromString(adjustName).padRight(40," ")
        file.append("${counter}${rfc}${type}${destinyAccount}${amount}${name}${bank}${branch}\n")
      }
    }

    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForSANTANDER(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} SANTANDER for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_SANTANDER",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "netAssimilable"
		String account = schema == "SA" ? "saBankAccount" : "iasBankAccount"
    String sourceAccount = dispersionDataForBank."${account}".accountNumber.padRight(11,'  ')
		//HEADER
		String header = "100001E${new Date().format('MMddyyyy')}${sourceAccount}     ${dispersionDataForBank.applyDate.format('MMddyyyy')}"
		file.append("${header}\n")

		//DETAIL
		BigDecimal total = new BigDecimal(0)
    Integer countEmployees = 0
    dispersionDataForBank.employees.eachWithIndex { employee, index ->
      if (employee."${salary}" > 0) {
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
        countEmployees++
      }
    }

		//FOOTER
		String footer = "3${(countEmployees+1).toString().padLeft(5,'0')}${countEmployees.toString().padLeft(5,'0')}${(new DecimalFormat('##0.00').format(total)).replace(".","").padLeft(18,'0')}"
		file.append("${footer}\n")
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForBANAMEX(Map dispersionDataForBank, String schema) {
    log.info "Payment dispersion same bank ${schema} BANAMEX for employees: ${dispersionDataForBank.employees}"
    File file = File.createTempFile("dispersion_${schema}_BANAMEX",".txt")
		
		String account = schema == "SA" ? "saBankAccount" : "iasBankAccount"
    BankAccount chargeBankAccount = dispersionDataForBank."${account}"
		if (!chargeBankAccount.clientNumber){
			log.info "La cuenta no tiene registrado el número de cliente"
			file.append("La cuenta Banamex ${chargeBankAccount.accountNumber} no tiene número de cliente registrado")
		} else {
      log.info "Dispersion data for bank: ${dispersionDataForBank}"
      String salary = schema == "SA" ? "imssSalaryNet" : "netAssimilable"
      
      def employeesNoZeroSalary = dispersionDataForBank.employees.findAll { employee -> employee."${salary}">0 }
      if (!employeesNoZeroSalary) {
        log.info "El salario de los empleados para el esquema ${schema} está en ceros"
        file.append("El salario de los empleados para el esquema ${schema} está en ceros")
      } else {
        String namePayer = schema == "SA" ? dispersionDataForBank.saPayer : dispersionDataForBank.iasPayer
        String nameCompany = namePayer.length() > 36 ? namePayer.substring(0,36) : namePayer
        String sourceAccount = dispersionDataForBank."${account}".accountNumber.padLeft(7,"0")
        String message = clearSpecialCharsFromString(dispersionDataForBank.paymentMessage).padRight(20," ")
        String lineControl = "1${chargeBankAccount.clientNumber.padLeft(12,'0')}${dispersionDataForBank.applyDate.format('yyMMdd')}${dispersionDataForBank.sequence.padLeft(4,'0')}${clearSpecialCharsFromString(nameCompany).padRight(36,'  ')}${message}15D01"
        file.append("${lineControl}\n")
        BigDecimal totalDispersion = employeesNoZeroSalary*."${salary}".sum().setScale(2, RoundingMode.HALF_UP)
        String lineGlobal = "21001${((totalDispersion*100).intValue()).toString().padLeft(18,'0')}03${chargeBankAccount.branchNumber.padLeft(13,'0')}${chargeBankAccount.accountNumber.padLeft(7,'0')}${employeesNoZeroSalary.size().toString().padLeft(6,'0')}"
        file.append("${lineGlobal}\n")
        employeesNoZeroSalary.eachWithIndex { employee, index ->
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
        String lineTotals = "4001${employeesNoZeroSalary.size().toString().padLeft(6,'0')}${(totalDispersion*100).intValue().toString().padLeft(18,'0')}000001${(totalDispersion*100).intValue().toString().padLeft(18,'0')}"
        file.append("${lineTotals}\n")
      }
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
    List<PaysheetEmployee> employees = getPaysheetEmployeesForInterBank(paysheet.employees, dispersionData.banks)
		if (employees) {
			Map dispersionDataInterBank = [employees: employees, paymentMessage:dispersionData.paymentMessage]
			File dispersionFileSAInterBank = createDispersionFileInterBank(dispersionDataInterBank, "SA")
			File dispersionFileIASInterBank = createDispersionFileInterBank(dispersionDataInterBank, "IAS")
			List dispersionFiles = [dispersionFileSAInterBank, dispersionFileIASInterBank]
			List s3Files = uploadDispersionFilesToS3(dispersionFiles)
			addingDispersionFilesToPaysheet(paysheet, s3Files)
		}
	}

  List<PaysheetEmployee> getPaysheetEmployeesForInterBank(def allEmployees, List banks) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank && !banks.contains(employee.prePaysheetEmployee.bank) && employee.paymentWay == PaymentWay.BANKING) {
        employee
      }
    }.grep()
  }

  File createDispersionFileInterBank(Map dispersionData, String schema) {
    log.info "Payment dispersion ${schema} interbank for employees: ${dispersionData.employees}"
    File file = File.createTempFile("dispersion_${schema}_InterBank",".txt")

		String salary = schema == "SA" ? "imssSalaryNet" : "netAssimilable"
		String sourceAccount = "M1Account".padLeft(18,'0')
    String rfc = "".padLeft(16," ")
    String type = "99"
    String bank = "001"
    String branch = "001"

    dispersionData.employees.eachWithIndex { employee, index ->
      if (employee."${salary}" > 0) {
        log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
        String counter = "${index+1}".padLeft(9,"0")
        String destinyAccount = employee.prePaysheetEmployee.account.padRight(20,' ')
        String amount = (new DecimalFormat('##0.00').format(employee."${salary}")).replace(".","").padLeft(15,'0')
        String adjustName = employee.prePaysheetEmployee.nameEmployee.length() > 40 ? employee.prePaysheetEmployee.nameEmployee.substring(0,40) : employee.prePaysheetEmployee.nameEmployee
        String name = clearSpecialCharsFromString(adjustName).padRight(40," ")
        file.append("${counter}${rfc}${type}${destinyAccount}${amount}${name}${bank}${branch}\n")
      }
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
    employees.headers = ['RFC','CURP','NOMBRE','NO. EMPL.','CÓD. BANCO','BANCO','CLABE', 'CUENTA', 'TARJETA', 'SALARIO IMSS', 'CARGA SOCIAL TRABAJADOR', 'SUBSIDIO', 'ISR', 'TOTAL IMSS', 'ASIMILABLE BRUTO', 'ISR ASIMILABLE', 'ASIMILABLE NETO', 'SUBTOTAL', 'CARGA SOCIAL EMPRESA', 'ISN', 'COSTO NOMINAL', 'COMISION', 'TOTAL NÓMINA', 'IVA', 'TOTAL A FACTURAR']
    employees.properties = ['prePaysheetEmployee.rfc', 'prePaysheetEmployee.curp', 'prePaysheetEmployee.nameEmployee', 'prePaysheetEmployee.numberEmployee', 'prePaysheetEmployee.bank.bankingCode', 'prePaysheetEmployee.bank.name', 'prePaysheetEmployee.clabe', 'prePaysheetEmployee.account', 'prePaysheetEmployee.cardNumber', 'salaryImss', 'socialQuota', 'subsidySalary', 'incomeTax', 'imssSalaryNet', 'crudeAssimilable', 'incomeTaxAssimilable', 'netAssimilable', 'totalSalaryEmployee', 'socialQuotaEmployer', 'paysheetTax', 'paysheetCost', 'commission', 'paysheetTotal', 'paysheetIva', 'totalToInvoice']
    employees.data = paysheet.employees.findAll { emp -> !emp.prePaysheetEmployee.bank }.sort { it.prePaysheetEmployee.nameEmployee }
    employees
  }

	List prepareDispersionSummary(Paysheet paysheet){
		List summary = []
		List payers = getPayersToPaymentDispersion(paysheet)
		def banks = getBanksFromPayers(payers)
		banks.each { bank ->
			Map summaryBank = [:]
			summaryBank.bank = bank
			summaryBank.saPayers = getPayersForBankAndSchema(payers, bank, PaymentSchema.IMSS)
			summaryBank.iasPayers = getPayersForBankAndSchema(payers, bank, PaymentSchema.ASSIMILABLE)
			summaryBank.allPayers = payers
			summaryBank.totalSA = paysheet.employees.findAll{ e-> if(e.prePaysheetEmployee.bank==bank && e.paymentWay==PaymentWay.BANKING){ return e} }*.imssSalaryNet.sum()
			summaryBank.totalIAS = paysheet.employees.findAll{ e-> if(e.prePaysheetEmployee.bank==bank && e.paymentWay==PaymentWay.BANKING){ return e} }*.netAssimilable.sum()
			summaryBank.type = "SameBank"
      if (summaryBank.totalSA > 0 || summaryBank.totalIAS >0)
			  summary.add(summaryBank)
		}
		summary = addInterBankSummary(summary, paysheet, payers)
		summary
	}

  def getPayersToPaymentDispersion(Paysheet paysheet) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheet.paysheetContract, paysheet.prePaysheet.paysheetProject)
    paysheetProject.payers.toList()
  }

  def getPayersForBankAndSchema(List payers, Bank bank, PaymentSchema schema) {
    def schemaBankPayers = payers.collect { payer ->
      if (payer.paymentSchema == schema && payer.company.banksAccounts.findAll { it.banco == bank }) {
        payer
      }
    }.grep()
    getDataPayersFromPayers(schemaBankPayers, bank)
  }

  def getDataPayersFromPayers(List payers, Bank bank) {
    List dataPayers = []
    payers.each { payer ->
      def banksAccounts = bank ? payer.company.banksAccounts.findAll {it.banco == bank} : payer.company.banksAccounts
      banksAccounts.each { bankAccount ->
        Map dataPayer = [:]
        dataPayer.payer = payer.company.bussinessName
        dataPayer.bankAccountId = bankAccount.id
        dataPayer.description = "${payer.company.bussinessName} - ${bankAccount}"
        dataPayers.add(dataPayer)
      }
    }
    dataPayers 
  }

	def addInterBankSummary(List summary, Paysheet paysheet, List payers){
    def banksPayers = getBanksFromPayers(payers)
    def employeesInterBank = paysheet.employees.findAll{ e-> if(!banksPayers.contains(e.prePaysheetEmployee.bank) && e.paymentWay==PaymentWay.BANKING){ return e} }
    if (employeesInterBank) {
      Map summaryInterBank = [:]
      summaryInterBank.bank = Bank.findByName("STP")
      summaryInterBank.saPayers = getDataPayersFromPayers(payers.findAll { it.paymentSchema == PaymentSchema.IMSS }, null)
      summaryInterBank.iasPayers = getDataPayersFromPayers(payers.findAll { it.paymentSchema == PaymentSchema.ASSIMILABLE }, null)
      summaryInterBank.allPayers = payers
      summaryInterBank.totalSA = employeesInterBank*.imssSalaryNet.sum()
      summaryInterBank.totalIAS = employeesInterBank*.netAssimilable.sum()
      summaryInterBank.type = "InterBank"
      if (summaryInterBank.totalSA > 0 || summaryInterBank.totalIAS >0)
        summary.add(summaryInterBank)
    }
		summary
	}

  def getBanksFromPayers(List payers) {
    def banksPayers = [] as Set
    payers.each { payer ->
      payer.company.banksAccounts.each { bankAccount ->
        banksPayers.add(bankAccount.banco)
      }
    }
    banksPayers.sort{it.name}
  }

}
