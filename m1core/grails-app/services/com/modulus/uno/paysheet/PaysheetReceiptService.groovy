package com.modulus.uno.paysheet

import com.modulus.uno.invoice.paysheetReceipt.*
import com.modulus.uno.invoice.*

import com.modulus.uno.DataImssEmployee
import com.modulus.uno.EmployeeLink

import com.modulus.uno.DataImssEmployeeService

class PaysheetReceiptService {

  PaysheetProjectService paysheetProjectService
  DataImssEmployeeService dataImssEmployeeService

  PaysheetReceipt createPaysheetReceiptFromPaysheetEmployeeForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceipt paysheetReceipt = new PaysheetReceipt(
      datosDeFacturacion: createInvoiceDataFromPaysheetEmployee(paysheetEmployee),
      emisor: createEmitterFromPaysheetEmployeeAndSchema(paysheetEmployee, schema),
      receptor: createReceiverFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
    )
  }

  DatosDeFacturacion createInvoiceDataFromPaysheetEmployee(PaysheetEmployee paysheetEmployee) {
    new DatosDeFacturacion(
      folio: paysheetEmployee.paysheet.paysheetContract.nextFolio,
      serie: paysheetEmployee.paysheet.paysheetContract.serie
    )
  }

  Contribuyente createEmitterFromPaysheetEmployee(paysheetEmployee, PaymentSchema schema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    PayerPaysheetProject payer = paysheetProject.payers.find { payer -> payer.paymentSchema == schema }
    new Contribuyente (
      registroPatronal: paysheetEmployee.paysheet.paysheetContract.employerRegistration,
      datosFiscales: new DatosFiscales (
        razonSocial: payer.company.bussinessName,
        rfc: payer.company.rfc,
        codigoPostal: payer.company.addresses.find { address -> address.addressType == AddressType.FISCAL }.zipCode
      )
    )
  }

  Empleado createReceiverFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    new Empleado(
      rfc: paysheetEmployee.prePaysheetEmployee.rfc,
      nombre: paysheetEmployee.prePaysheetEmployee.nameEmployee,
      curp: paysheetEmployee.prePaysheetEmployee.curp,
      datosBancarios: new DatosBancarios(banco:paysheetEmployee.prePaysheetEmployee.bank.bankingCode.substring(2,5), cuenta:paysheetEmployee.prePaysheetEmployee.account),
      datosLaborales: createJobDataFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
    )
  }

  DatosLaborales createJobDataFromPaysheetEmployeeAndSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetProject paysheetProject = paysheetProjectService.getPaysheetProjectByPaysheetContractAndName(paysheetEmployee.paysheet.paysheetContract, paysheetEmployee.paysheet.prePaysheet.paysheetProject)
    EmployeeLink employeeLink = EmployeeLink.findByCompanyAndEmployeeRef(paysheetEmployee.paysheet.paysheetContract.company, paysheetEmployee.prePaysheetEmployee.rfc)
    DataImssEmployee dataImssEmployee = DataImssEmployee.findByEmployee(employeeLink)
    new DatosLaborales (
      entidad: paysheetProject.federalEntity,
      noEmpleado: paysheetEmployee.prePaysheetEmployee.numberEmployee,
      tipoContrato: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.contractType.key : ContractType.WORK_WITHOUT_RELATION.key,
      periodoPago: dataImssEmployee.paymentPeriod.key,
      tipoRegimen: paymentSchema == PaymentSchema.IMSS ? dataImssEmployee.regimeType.key : RegimeType.FEES_ASSIMILATED.key,
      tipoJornada: dataImssEmployee.workDayType.key,
      datosImss: new DatosImss (
        antiguedad: dataImssEmployeeService.calculateLaborOldInSATFormat(dataImssEmployee),
        departamento: dataImssEmployee.department,
        fechaAlta: dataImssEmployee.registrationDate.format("yyyy-MM-dd"),
        nss: dataImssEmployee.nss,
        puesto: dataImssEmployee.job,
        riesgo: dataImssEmployee.jobRisk.key,
        salarioBaseCotizacion: paymentSchema == PaymentSchema.IMSS ? paysheetEmployee.breakdownPayment.baseQuotation : new BigDecimal(0),
        salarioDiarioIntegrado: paymentSchema == PaymentSchema.IMSS ? paysheetEmployee.breakdownPayment.integratedDailySalary : new BigDecimal(0)
      )
    )
  }
}
