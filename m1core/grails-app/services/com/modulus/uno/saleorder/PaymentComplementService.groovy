package com.modulus.uno.saleorder

import com.modulus.uno.MovimientosBancarios
import com.modulus.uno.PaymentWay
import com.modulus.uno.Company
import com.modulus.uno.saleorder.PaymentComplementCommand
import com.modulus.uno.invoice.*
import com.modulus.uno.Address
import com.modulus.uno.AddressType
import com.modulus.uno.RestService
import com.modulus.uno.RestException

import grails.util.Environment

class PaymentComplementService {

  RestService restService

  String generatePaymentComplementForConciliatedBankingTransaction(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    //Create command for payment complement
    PaymentComplementCommand paymentComplementCommand = createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    //Send command to M1-API-Facturacion
    //Receive uuid and return it
    "hardcode uuid"
  }
 
  PaymentComplementCommand createPaymentComplementCommand(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
   PaymentComplementCommand paymentComplementCommand = new PaymentComplementCommand(
    id: dataPaymentComplement.company.id,
    emisor: createEmisor(dataPaymentComplement.company),
    receptor: createReceptor(dataPaymentComplement.conciliations.first().saleOrder),
    emitter: dataPaymentComplement.company.rfc,
    datosDeFacturacion:  new DatosDeFacturacion(),
    payment: createDataPayment(bankingTransaction, dataPaymentComplement)
   )
   paymentComplementCommand
  }

  Contribuyente createEmisor(Company company) {
    Address address = company.addresses.find { addr -> addr.addressType == AddressType.FISCAL }
    new Contribuyente(
      datosFiscales: new DatosFiscales(
        razonSocial: company.bussinessName,
        rfc: (Environment.current == Environment.PRODUCTION) ? company.rfc : "AAA010101AAA",
        regimen: new RegimenFiscal(clave:company.taxRegime.key, descripcion:company.taxRegime.description)
        codigoPostal: address.zipCode
      )
    ) 
  }

  Contribuyente createReceptor(SaleOrder saleOrder) {
    new Contribuyente(
      datosFiscales: new DatosFiscales(
        razonSocial: saleOrder.clientName,
        rfc: (Environment.current == Environment.PRODUCTION) ? saleOrder.rfc : "LAN7008173R5",
        usoCFDI:"P01"
      )
    ) 
  }

  Payment createDataPayment(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    PaymentWay paymentWay = PaymentWay.values().find { it.toString() == dataPaymentComplement.paymentWay }
    Bank bank = Bank.get(dataPaymentComplement.bankId)
    new Payment(
      paymentDate:,
      paymentWay: paymentWay.key,
      currency:"MXN",
      amount: bankingTransaction.amount.setScale(2, RoundingMode.HALF_UP).toString(),
      sourceBankRfc:bank.rfc,
      sourceAccount:dataPaymentComplement.sourceAccount,
      destinationBankRfc:bankingTransaction.cuenta.banco.rfc,
      destinationAccount:bankingTransaction.cuenta.clabe ?: bankingTransaction.cuenta.accountNumber,
      relatedDocuments:createRelatedDocuments()
    )
  }
}
