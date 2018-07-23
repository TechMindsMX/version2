package com.modulus.uno.quotation

import com.modulus.uno.saleorder.SaleOrder
import com.modulus.uno.Company
import com.modulus.uno.Product
import com.modulus.uno.InvoicePurpose
import com.modulus.uno.PaymentWay
import com.modulus.uno.PaymentMethod

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
  PaymentWay paymentWay
  PaymentMethod paymentMethod
  InvoicePurpose invoicePurpose

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
    paymentWay nullable:true
    paymentMethod nullable:true
    invoicePurpose nullable:true
  }
}
