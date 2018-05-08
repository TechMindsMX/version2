package com.modulus.uno.saleorder

import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod
import com.modulus.uno.InvoicePurpose
import com.modulus.uno.status.CreditNoteStatus

class CreditNote {

  CreditNoteStatus status = CreditNoteStatus.CREATED
  PaymentWay paymentWay = PaymentWay.TRANSFERENCIA_ELECTRONICA
  PaymentMethod paymentMethod = PaymentMethod.PUE
  InvoicePurpose invoicePurpose = InvoicePurpose.G02
  String folio

  Date dateCreated
  Date lastUpdated
  
  static belongsTo = [saleOrder:SaleOrder]
  static hasMany = [items:CreditNoteItem]

  static constraints = {
    folio nullable:true
  }

}
