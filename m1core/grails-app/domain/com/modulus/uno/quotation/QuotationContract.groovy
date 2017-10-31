package com.modulus.uno.quotation

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company

class QuotationContract {

  BusinessEntity client
  BigDecimal commission
  Date initDate
  QuotationContractStatus status = QuotationContractStatus.ACTIVE

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company:Company]

  static constraints = {
    commission min:0.0, max:16.0
  }
}
