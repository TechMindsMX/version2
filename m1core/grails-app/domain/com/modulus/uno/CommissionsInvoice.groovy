package com.modulus.uno

import java.math.RoundingMode

class CommissionsInvoice {

  Company receiver
  CommissionsInvoiceStatus status = CommissionsInvoiceStatus.CREATED
  String folioSat

  static hasMany = [commissions:CommissionTransaction, payments:CommissionsInvoicePayment]

  Date dateCreated
  Date lastUpdated

  static constraints = {
    folioSat nullable:true
  }

  BigDecimal getSubtotal() {
    commissions ? commissions*.amount.sum() : new BigDecimal(0)
  }

  BigDecimal getAmountIva() {
    getSubtotal() * 0.16
  }

  BigDecimal getTotal() {
    getSubtotal() + getAmountIva()
  }

  BigDecimal getTotalPayed() {
    payments*.amount.sum() ?: new BigDecimal(0)
  }

  BigDecimal getAmountToPay() {
    getTotal() - getTotalPayed()
  }

  String toString() {
    "${id} / ${receiver.bussinessName} / Total: \$${total.setScale(2, RoundingMode.HALF_UP)} / Por pagar: \$${amountToPay.setScale(2, RoundingMode.HALF_UP)}"
  }
}
