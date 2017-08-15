package com.modulus.uno.paysheet

import grails.transaction.Transactional
import pl.touk.excel.export.WebXlsxExporter
import java.text.SimpleDateFormat

class PaysheetService {

  PaysheetEmployeeService paysheetEmployeeService
  PrePaysheetService prePaysheetService

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

}
