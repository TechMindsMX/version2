package com.modulus.uno

import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.status.CommissionTransactionStatus

class CommissionTransaction {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  CommissionType type
  BigDecimal amount
  CommissionTransactionStatus status = CommissionTransactionStatus.PENDING
  Company company
  Transaction transaction
  SaleOrder invoice

  Date dateCreated
  Date lastUpdated

  static constraints = {
    type nullable:false
    amount nullable:false, min:0.01
    company nullable:false
    transaction nullable:true
    invoice nullable:true
  }

}
