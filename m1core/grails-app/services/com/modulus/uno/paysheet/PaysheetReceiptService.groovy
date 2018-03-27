package com.modulus.uno.paysheet

import com.modulus.uno.invoice.paysheet.*
import com.modulus.uno.invoice.*

class PaysheetReceiptService {

  PaysheetReceipt createPaysheetReceiptFromPaysheetEmployeeForSchema(PaysheetEmployee paysheetEmployee, String schema) {
    PaysheetReceipt paysheetReceipt = new PaysheetReceipt(
      datosDeFacturacion: createInvoiceDataFromPaysheetEmployee(paysheetEmployee),
      emisor: createEmitterFromPaysheetEmployee(paysheetEmployee)
    )
  }

  DatosDeFacturacion createInvoiceDataFromPaysheetEmployee(PaysheetEmployee paysheetEmployee) {
    new DatosDeFacturacion(
      folio: paysheetEmployee.paysheet.paysheetContract.nextFolio,
      serie: paysheetEmployee.paysheet.paysheetContract.serie
    )
  }

}
