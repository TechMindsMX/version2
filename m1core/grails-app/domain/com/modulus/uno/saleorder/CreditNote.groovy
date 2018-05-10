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

}
