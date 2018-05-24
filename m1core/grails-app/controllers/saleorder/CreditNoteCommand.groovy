package com.modulus.uno.saleorder

import grails.validation.Validateable
import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod
import com.modulus.uno.InvoicePurpose

class CreditNoteCommand implements Validateable {

  String paymentWay
  String paymentMethod
  String saleOrderId

  CreditNote createCreditNote() {
    PaymentWay paymentWay = PaymentWay.find { it.toString() == this.paymentWay }
    PaymentMethod paymentMethod = PaymentMethod.find { it.toString() == this.paymentMethod }
    SaleOrder saleOrder = SaleOrder.get(this.saleOrderId)

    new CreditNote (
      paymentWay:paymentWay,
      paymentMethod:paymentMethod,
      saleOrder:saleOrder
    )
  }

}
