package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat
import java.text.DecimalFormat
import com.modulus.uno.Bank
import com.modulus.uno.BankAccount

class PaysheetService {

  PaysheetEmployeeService paysheetEmployeeService
  PrePaysheetService prePaysheetService
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

  def getBanksAccountsToPay(Paysheet paysheet) {
    paysheet.company.banksAccounts.findAll { bA -> bA.banco.bankingCode.endsWith(grailsApplication.config.paysheet.paymentBankingCode) }
  }

  File generateDispersionFilesFromPaysheet(Paysheet paysheet, Map dispersionData) {
    dispersionData = complementDispersionData(dispersionData)
		generateDispersionFileSameBank(paysheet, dispersionData)
		generateDispersionFileInterBank(paysheet, dispersionData)
  }

  Map complementDispersionData(Map dispersionData) {
    BankAccount chargeBankAccount = BankAccount.get(dispersionData.chargeBankAccountId)
    dispersionData.chargeBankAccount = chargeBankAccount
    dispersionData.salary = dispersionData.paymentSchema == 'IMSS' ? "imssSalaryNet" : "salaryAssimilable"
    dispersionData
  }

	def generateDispersionFileSameBank(Paysheet paysheet, Map dispersionData){
    List<PaysheetEmployee> employees = getPaysheetEmployeesForSameBank(paysheet.employees, dispersionData.chargeBankAccount.banco)
    File dispersionFileSameBank = createTxtDispersionFileForSameBank(employees, dispersionData)
		//TODO: upload file to s3
	}

	def generateDispersionFileInterBank(Paysheet paysheet, Map dispersionData){
    List<PaysheetEmployee> employees = getPaysheetEmployeesForInterBank(paysheet.employees, dispersionData.chargeBankAccount.banco)
    File dispersionFileInterBank = createTxtDispersionFileForInterBank(employees, dispersionData)
		//TODO: upload file to s3
	}

  File createTxtDispersionFileForSameBank(List<PaysheetEmployee> employees, Map dispersionData) {
    log.info "Payment dispersion same bank for employees: ${employees}"
    File file = File.createTempFile("txtDispersion",".txt")
    employees.each { employee ->
      log.info "Payment dispersion same bank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.account.padLeft(18,'0')
      String sourceAccount = dispersionData.chargeBankAccountNumber.accountNumber.padLeft(18,'0')
      String currency = "MXN"
      String amount = (new DecimalFormat('##0.00').format(employee."${dispersionData.salary}")).padLeft(16,'0')
      String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
      file.append("${destinyAccount}${sourceAccount}${currency}${amount}${message}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  File createTxtDispersionFileForInterBank(List<PaysheetEmployee> employees, Map dispersionData) {
    log.info "Payment dispersion ${dispersionData.paymentSchema} interbank for employees: ${employees}"
    File file = File.createTempFile("txtDispersion",".txt")
    employees.each { employee ->
      log.info "Payment dispersion ${dispersionData.paymentSchema} interbank record for employee: ${employee?.dump()}"
      String destinyAccount = employee.prePaysheetEmployee.clabe.padLeft(18,'0')
      String sourceAccount = dispersionData.chargeAccountNumber.padLeft(18,'0')
      String currency = "MXN"
      String amount = (new DecimalFormat('##0.00').format(employee."${dispersionData.salary}")).padLeft(16,'0')
      String cleanedName = clearSpecialCharsFromString(employee.prePaysheetEmployee.nameEmployee)
      String nameEmployee = cleanedName.length()>30 ? cleanedName.substring(0,30) : cleanedName.padRight(30,' ')
      String typeAccount = "40"
      String bankingCode = employee.prePaysheetEmployee.bank.bankingCode
      String message = clearSpecialCharsFromString(dispersionData.paymentMessage).padRight(30,' ')
      String reference = new Date().format("ddMMyy").padLeft(7,'0')
      String disp = "H"      
      file.append("${destinyAccount}${sourceAccount}${currency}${amount}${nameEmployee}${typeAccount}${bankingCode}${message}${reference}${disp}\n")
    }
    log.info "File created: ${file.text}"
    file
  }

  String clearSpecialCharsFromString(String text) {
    text.toUpperCase().replace("Ñ","N").replace("Á","A").replace("É","E").replace("Í","I").replace("Ó","O").replace("Ú","U").replace("Ü","U").replaceAll("[^a-zA-Z0-9 ]","")
  }

  List<PaysheetEmployee> getPaysheetEmployeesForSameBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank==bank) {
        employee
      }
    }.grep()
  }

  List<PaysheetEmployee> getPaysheetEmployeesForInterBank(def allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank!=bank) {
        employee
      }
    }.grep()
  }

}
