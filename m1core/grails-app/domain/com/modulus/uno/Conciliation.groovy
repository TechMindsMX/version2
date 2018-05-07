package com.modulus.uno

import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.status.ConciliationStatus

class Conciliation {

  Company company
  User user
  SaleOrder saleOrder
  Payment payment
  MovimientosBancarios bankingTransaction
  BigDecimal amount
  BigDecimal changeType
  ConciliationStatus status = ConciliationStatus.TO_APPLY
	PaymentToPurchase paymentToPurchase
  String comment

  Date dateCreated
  Date lastUpdated

  static constraints = {
    company nullable:false
    amount nullable:false, min:0.01
    user nullable:false
    saleOrder nullable:true
    payment nullable:true
    bankingTransaction nullable:true
		paymentToPurchase nullable:true
    comment nullable:true, blank:true
  }
}
