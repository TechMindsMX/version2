package com.modulus.uno.quotation

import com.modulus.uno.SaleOrder
import com.modulus.uno.Company
import com.modulus.uno.Product

class QuotationRequest {

  BigDecimal commission
  String description
  QuotationRequestStatus status = QuotationRequestStatus.CREATED
  Company biller
  SaleOrder saleOrder
  Product product
  BigDecimal subtotal
  BigDecimal total
  BigDecimal iva

  Date dateCreated
  Date lastUpdated

    static belongsTo = [quotationContract: QuotationContract]

    static constraints = {
      biller nullable:true
      saleOrder nullable:true
      product nullable:true
      subtotal min:0.0
      total min:0.0
      iva min:0.0
    }
}
