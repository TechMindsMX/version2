package com.modulus.uno

import com.modulus.uno.status.ConciliationStatus
import com.modulus.uno.status.PaymentComplementStatus

class MovimientosBancarios {

  String concept
  String reference
  BigDecimal amount
  Date dateEvent
  MovimientoBancarioType type
  Boolean reconcilable = false
  ConciliationStatus conciliationStatus
  Boolean createPaymentComplement = false
  String paymentComplementUuid
  PaymentComplementStatus paymentComplementStatus
  String paymentWay
  Bank sourceBank
  String sourceAccount

  Date dateCreated
  Date lastUpdated

  static belongsTo = [cuenta:BankAccount]

  static constraints = {
    concept blank:false, size:5..200
    reference blank:true, nullable:true, size:5..200
    amount min:0.0
    conciliationStatus nullable:true
    paymentComplementUuid nullable:true
    paymentComplementStatus nullable:true
    paymentWay nullable:true
    sourceBank nullable:true
    sourceAccount nullable:true
  }

}
