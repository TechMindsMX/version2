package com.modulus.uno.quotation

import com.modulus.uno.SaleOrder
import com.modulus.uno.Company

class QuotationRequest {

  BigDecimal commission
  String description
  BigDecimal amount
  QuotationRequestStatus status = QuotationRequestStatus.CREATED
  Company biller
  SaleOrder saleOrder
  SatConcept satConcept
  BigDecimal subtotal
  BigDecimal total
  BigDecimal iva

  Date dateCreated
  Date lastUpdated

    static belongsTo = [quotationContract: QuotationContract]

    static constraints = {
      biller nullable:true
      saleOrder nullable:true
      satConcept nullable:true
      amount min:0.0
    }
}
