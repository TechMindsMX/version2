package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat

import com.modulus.uno.Bank
import com.modulus.uno.BankAccount
import com.modulus.uno.BusinessEntity

class PaysheetService {

  PaysheetEmployeeService paysheetEmployeeService
  PrePaysheetService prePaysheetService
  def grailsApplication
  PaysheetDispersionFilesService paysheetDispersionFilesService

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
		paysheetDispersionFilesService.generateDispersionFiles(paysheet, dispersionData)
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
		List payers = paysheetDispersionFilesService.getPayersToPaymentDispersion(paysheet)
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

  def getDispersionBanksFromPaysheet(Paysheet paysheet) {
 		List summary = prepareDispersionSummary(paysheet) 
    summary.bank
  }

  @Transactional
  def processResultDispersionFileToPaysheet(Paysheet paysheet, def params) {
    paysheetDispersionFilesService.processResultDispersionFile(paysheet, params)
  }
}
