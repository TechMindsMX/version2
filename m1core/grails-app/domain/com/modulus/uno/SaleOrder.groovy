package com.modulus.uno

import java.math.RoundingMode
import grails.converters.JSON

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
  PaymentMethod paymentMethod = PaymentMethod.TRANSFERENCIA_ELECTRONICA

  Date fechaCobro
  String externalId
  Date originalDate
  String pdfTemplate
  String note

  static belongsTo = [company:Company]

  static hasMany = [addresses:Address, items:SaleOrderItem,authorizations:Authorization, documents:S3Asset]

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
  }

  BigDecimal getTotalIVA(){
    items*.appliedIVA.sum() ?: 0
  }

  BigDecimal getTotalIEPS(){
    items*.appliedIEPS.sum() ?: 0
  }

  BigDecimal getTotal(){
    getSubtotal() + getTotalIVA() + getTotalIEPS()
  }

  def getSubtotal(){
    items*.amountWithoutTaxes.sum() ?: 0
  }

  def getAmountDiscount() {
    getSubtotal()*(discount/100)
  }

  def getSubtotalWithDiscount() {
    getSubtotal() - getAmountDiscount()
  }

  BigDecimal getTotalDiscount(){
    items*.appliedDiscount.sum() ?: 0
  }

  String toString(){
    "${clientName} / \$ ${total.setScale(2, RoundingMode.HALF_UP)}"
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
