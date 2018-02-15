package com.modulus.uno.paysheet

import grails.transaction.Transactional
import com.modulus.uno.Bank
import com.modulus.uno.BankAccount
import com.modulus.uno.BusinessEntity
import com.modulus.uno.NameType
import com.modulus.uno.S3AssetService

import java.text.DecimalFormat
import java.math.RoundingMode

class PaysheetDispersionFilesService {
  
  PaysheetProjectService paysheetProjectService
  S3AssetService s3AssetService

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

  def getPayersToPaymentDispersion(Paysheet paysheet) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheet.paysheetContract, paysheet.prePaysheet.paysheetProject)
    paysheetProject.payers.toList()
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

	String getMethodCreatorOfTxtDispersionFile(String bankName) {
		bankName = bankName.replace(" ","")
		String methodCreatorTxtFileDispersion = "createTxtDispersionFileDefault"
		if (this.metaClass.respondsTo(this, "createTxtDispersionFileFor${bankName}")) {
			methodCreatorTxtFileDispersion = "createTxtDispersionFileFor${bankName}"
		}
		methodCreatorTxtFileDispersion
	}

	List uploadDispersionFilesToS3(List files){
		List s3Files = []
		files.each { file ->
			def s3DispersionFile = s3AssetService.createFileToUpload(file, "${file.name.replaceAll('[0-9]','')}")
			s3Files.add(s3DispersionFile)
		}
		s3Files
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
		int consecutive = 0

    dispersionData.employees.eachWithIndex { employee, index ->
      if (employee."${salary}" > 0) {
				log.info "Payment dispersion interbank record for employee: ${employee?.dump()}"
				consecutive++
				String counter = "${consecutive}".padLeft(9,"0")
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

  @Transactional
	def addingDispersionFilesToPaysheet(Paysheet paysheet, List s3Files){
		s3Files.each { file ->
			paysheet.addToDispersionFiles(file)
		}
		paysheet.save()
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
		int consecutive = 0

    dispersionDataForBank.employees.eachWithIndex { employee, index ->
      if (employee."${salary}" > 0) {
				consecutive++
				String counter = "${consecutive}".padLeft(9,"0")
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

  def uploadResultDispersionFile(Paysheet paysheet, def resultFile, Bank bank) {
    def asset = s3AssetService.createTempFilesOfMultipartsFiles(resultFile,"PaysheetResultDispersionFile")
    DispersionResultFile dispersionResultFile = new DispersionResultFile(
      bank:bank,
      file:asset,
      paysheet:paysheet
    )
    dispersionResultFile.save() 
  }
}
