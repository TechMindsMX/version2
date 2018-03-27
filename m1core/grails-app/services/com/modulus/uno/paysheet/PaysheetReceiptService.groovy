package com.modulus.uno.paysheet

import com.modulus.uno.invoice.paysheet.*
import com.modulus.uno.invoice.*

class PaysheetReceiptService {

  PaysheetReceipt createPaysheetReceiptFromPaysheetEmployeeForSchema(PaysheetEmployee paysheetEmployee, PaymentSchema schema) {
    PaysheetReceipt paysheetReceipt = new PaysheetReceipt(
      datosDeFacturacion: createInvoiceDataFromPaysheetEmployee(paysheetEmployee),
      emisor: createEmitterFromPaysheetEmployeeAndSchema(paysheetEmployee, schema)
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

}
