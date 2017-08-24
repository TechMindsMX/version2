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

  File generateImssPaymentsDispersionFileForCompanyBankWithChargeAccount(Paysheet paysheet, BankAccount chargeBankAccount) {
    //obtener la lista de empleados con cuenta bancaria en el mismo banco al que usa la empresa para pagos
    Bank bank = Bank.findByBankingCodeLike("%${grailsApplication.config.paysheet.paymentBankingCode}")
    List<PaysheetEmployee> employees = getPaysheetEmployeesWithBankAccountInBank(paysheet.employees, bank)
    //generar el archivo txt usando la lista
  }

  List<PaysheetEmployee> getPaysheetEmployeesWithBankAccountInBank(List<PaysheetEmployee> allEmployees, Bank bank) {
    allEmployees.collect { employee ->
      if (employee.prePaysheetEmployee.bank==bank) {
        employee
      }
    }    
  }

  File createTxtImssDispersionFileForSameCompanyBank(List<PaysheetEmployee> employees, BankAccount chargeBankAccount) {
    File file = File.createTempFile("txtDispersion",".txt")
    employees.each { employee ->
      String destinyAccount = "${employee.prePaysheetEmployee.account.padLeft(18,'0')}"
      String sourceAccount = "${chargeBankAccount.accountNumber.padLeft(18,'0')}"
      String currency = "MXN"
      String amount = "${(new DecimalFormat('##0.00').format(employee.totalSalaryEmployee)).padLeft(16,'0')}"
      String paymentMessage = "PAGO IMSS".padRight(30,' ')
      file.write("${destinyAccount}${sourceAccount}${currency}${amount}${paymentMessage}\n")
    }
    log.info "File created: ${file.text}"
    file
  }


  /* ---- Layout dispersión cuentas bancomer
  lCap.u1_Cabono = Format(Hoja3.Cells(lFila, 1).Value, "000000000000000000")
  lCap.u2_Ccargo = Format(Hoja3.Cells(lFila, 2).Value, "000000000000000000")
  lCap.u3_Divisa = "MXP"
  lCap.u4_Importe = Format(Hoja3.Cells(lFila, 3).Value, "0000000000000.00")
  lCap.u5_Mpago = UCase(RemoveTrash(Hoja3.Cells(lFila, 4).Value)) --> Motivo de pago
  lCap.u6_CRLF = vbCrLf -->Salto de línea
  */

  /* ---- Layout dispersión transferencias interbancarias
  lCap.u1_Cabono = Format(Hoja4.Cells(lFila, 1).Value, "000000000000000000")
  lCap.u2_Ccargo = Format(Hoja4.Cells(lFila, 2).Value, "000000000000000000")
  lCap.u3_Divisa = "MXP"
  lCap.u4_Importe = Format(Hoja4.Cells(lFila, 3).Value, "0000000000000.00")
  lCap.u5_Titular = UCase(RemoveTrash(Hoja4.Cells(lFila, 4).Value))
  lCap.u6_Tcuenta = "40"
  lCap.u7_Nbanco = Mid(Hoja4.Cells(lFila, 1).Value, 1, 3)
  lCap.u8_Mpago = UCase(RemoveTrash(Hoja4.Cells(lFila, 5).Value))
  lCap.u9_Refnum = Format(Hoja4.Cells(lFila, 6).Value, "0000000")
  lCap.u10_Disp = UCase(RemoveTrash(Hoja4.Cells(lFila, 7).Value))
  lCap.u11_CRLF = vbCrLf
  */

  // |ARVIZU ESTRADA MARTIN         |
  // |BERNABE CAMPOS JOEL           |
  // 072180004686814140000000000105737036MXP0000000001022.60ARVIZU ESTRADA MARTIN         40072FAP BOSQ AZTEX INTER S21      0020617H
}
