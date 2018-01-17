package com.modulus.uno.quotation

import com.modulus.uno.PaymentWay

class QuotationPaymentRequest {

  BigDecimal amount
  String note
  PaymentWay paymentMethod
  QuotationPaymentRequestStatus status = QuotationPaymentRequestStatus.CREATED

  Date dateCreated
  Date lastUpdated

    static belongsTo = [quotationContract: QuotationContract]

    static constraints = {
      note nullable:true
    }
}
