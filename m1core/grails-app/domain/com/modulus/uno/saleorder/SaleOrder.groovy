package com.modulus.uno.saleorder

import java.math.RoundingMode
import grails.converters.JSON

import com.modulus.uno.Company
import com.modulus.uno.Address
import com.modulus.uno.Authorization
import com.modulus.uno.S3Asset
import com.modulus.uno.RejectReason
import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod
import com.modulus.uno.InvoicePurpose
import com.modulus.uno.status.SaleOrderStatus
import com.modulus.uno.status.CreditNoteStatus

class SaleOrder {

  String rfc
  String clientName
  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String folio
  Date dateCreated
  Date lastUpdated

  RejectReason rejectReason
  String comments

  SaleOrderStatus status = SaleOrderStatus.CREADA
  PaymentWay paymentWay = PaymentWay.TRANSFERENCIA_ELECTRONICA

  Date fechaCobro
  String externalId
  Date originalDate
  String pdfTemplate
  String note
  String currency
  BigDecimal changeType = new BigDecimal(0)
  PaymentMethod paymentMethod = PaymentMethod.PUE
  InvoicePurpose invoicePurpose = InvoicePurpose.G01

  static belongsTo = [company:Company]

  static hasMany = [addresses:Address, items:SaleOrderItem,authorizations:Authorization, documents:S3Asset, payments:SaleOrderPayment, creditNotes:CreditNote]

  static constraints = {
    rfc blank:false,size:10..50
    clientName blank:false, size:1..300
    uuid nullable:true
    rejectReason nullable:true
    comments nullable:true
    folio nullable:true
    fechaCobro nullable:true
    externalId nullable:true
    originalDate nullable:true
    note nullable:true, size:1..300
    pdfTemplate nullable:true
    currency nullable:false
    changeType nullable:true
  }

  BigDecimal getTotalIVA(){
    items*.appliedIVA.sum() ?: 0
  }

  BigDecimal getTotalIvaRetention(){
    items*.totalIvaRetention.sum() ?: 0
  }

  BigDecimal getTotal(){
    getSubtotal() + getTotalIVA() - getTotalIvaRetention()
  }

  BigDecimal getSubtotal(){
    items*.amountWithoutTaxes.sum() ?: 0
  }

  BigDecimal getSubtotalWithDiscount() {
    getSubtotal() - getAmountDiscount()
  }

  BigDecimal getTotalDiscount(){
    items*.appliedDiscount.sum() ?: 0
  }

  BigDecimal getAmountPayed(){
    payments*.amount.sum() ?: 0
  }

  BigDecimal getAmountToPay() {
    getTotal() - getAmountPayed() - getTotalCreditNotesApplied()
  }

  BigDecimal getTotalCreditNotesApplied() {
    (creditNotes.findAll { it.status == CreditNoteStatus.APPLIED })*.total.sum()
  }

  String toString(){
    "${id} / ${clientName} / ${currency} / Total:\$ ${total.setScale(2, RoundingMode.HALF_UP)} / Por pagar: \$ ${amountToPay.setScale(2, RoundingMode.HALF_UP)}"
  }

  static marshaller = {
    JSON.registerObjectMarshaller(SaleOrder,1) { m ->
      return [
        id:m.id,
        rfc:m.rfc,
        clientName:m.clientName,
        uuid:m.uuid,
        folio:m.folio,
        rejectReason:m.rejectReason,
        status:m.status,
        company:m.company,
        addresses:m.addresses,
        items:m.items,
        authorizations: m.authorizations,
        documents:m.documents
      ]
    }
  }

}
