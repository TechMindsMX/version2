package com.modulus.uno.saleorder

import com.modulus.uno.MovimientosBancarios
import com.modulus.uno.PaymentWay
import com.modulus.uno.Company
import com.modulus.uno.Conciliation
import com.modulus.uno.saleorder.PaymentComplementCommand
import com.modulus.uno.invoice.*
import com.modulus.uno.invoice.paymentComplement.*
import com.modulus.uno.Address
import com.modulus.uno.Bank
import com.modulus.uno.AddressType
import com.modulus.uno.RestService
import com.modulus.uno.RestException

import grails.util.Environment
import java.math.RoundingMode
import groovy.json.JsonSlurper

class PaymentComplementService {

  RestService restService
  def grailsApplication

  String generatePaymentComplementForConciliatedBankingTransaction(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    //Create command for payment complement
    PaymentComplementCommand paymentComplementCommand = createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    //Send command to M1-API-Facturacion
    def resultStamp = restService.sendFacturaCommandWithAuth(paymentComplementCommand, grailsApplication.config.modulus.paymentComplementCreate)
    if (!resultStamp) {
      throw new RestException("No se pudo generar el complemento de pago") 
    }
    log.info "Result stamp: ${resultStamp.text}"
    resultStamp.text
  }

  def generatePdfForPaymentComplementWithUuid(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    PaymentComplementCommand paymentComplementCommand = createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    paymentComplementCommand.datosDeFacturacion.uuid = bankingTransaction.paymentComplementUuid
    def result = restService.sendFacturaCommandWithAuth(paymentComplementCommand, grailsApplication.config.modulus.paymentComplementGeneratePdf)
    log.info "Result generating pdf: ${result}"
    if (!result) {
      throw new RestException("No se pudo generar el PDF del complemento de pago")
    }
    result   
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
        regimen: new RegimenFiscal(clave:company.taxRegime.key, descripcion:company.taxRegime.description),
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
    Bank bank = Bank.get(dataPaymentComplement.bankId)
    new Payment(
      paymentDate: bankingTransaction.dateEvent.format("yyyy-MM-dd'T00:00:00'"),
      paymentWay: dataPaymentComplement.paymentWay,
      currency:"MXN",
      amount: bankingTransaction.amount.setScale(2, RoundingMode.HALF_UP).toString(),
      sourceBankRfc:bank.rfc,
      sourceAccount:dataPaymentComplement.sourceAccount,
      destinationBankRfc:bankingTransaction.cuenta.banco.rfc,
      destinationAccount:bankingTransaction.cuenta.clabe ?: bankingTransaction.cuenta.accountNumber,
      relatedDocuments:createRelatedDocuments(dataPaymentComplement.conciliations)
    )
  }

  List<RelatedDocument> createRelatedDocuments(List<Conciliation> conciliations) {
    List<RelatedDocument> relatedDocuments = []
    conciliations.each { conciliation ->
      RelatedDocument relatedDocument = new RelatedDocument(
        uuid: conciliation.saleOrder.folio.length() > 36 ? conciliation.saleOrder.folio.substring(0,36) : conciliation.saleOrder.folio,
        serie: conciliation.saleOrder.invoiceSerie ?: "",
        folio: conciliation.saleOrder.invoiceFolio ?: "",
        currency: conciliation.saleOrder.currency,
        changeType: conciliation.saleOrder.changeType.setScale(2, RoundingMode.HALF_UP).toString(),
        paymentMethod: conciliation.saleOrder.paymentMethod.name(),
        partialNumber: conciliation.saleOrder.payments.size(),
        beforeAmount: (conciliation.saleOrder.amountToPay + conciliation.amount).setScale(2, RoundingMode.HALF_UP).toString(),
        payedAmount: conciliation.amount.setScale(2, RoundingMode.HALF_UP).toString(),
        newAmount: conciliation.saleOrder.amountToPay.setScale(2, RoundingMode.HALF_UP).toString()
      )
      relatedDocuments.add(relatedDocument)
    }
    relatedDocuments
  }

  def generatePdfForPaymentComplementFromBankingTransaction(MovimientosBancarios bankingTransaction, Map dataPaymentComplement) {
    PaymentComplementCommand paymentComplementCommand = createPaymentComplementCommand(bankingTransaction, dataPaymentComplement)
    paymentComplementCommand.datosDeFacturacion.uuid = bankingTransaction.paymentComplementUuid
    def result = restService.sendFacturaCommandWithAuth(paymentComplementCommand, grailsApplication.config.modulus.pdfPaymentComplementCreate)
    if (!result) {
      throw new RestException("No se pudo generar el PDF del complemento de pago")
    }
    result
  }

}
