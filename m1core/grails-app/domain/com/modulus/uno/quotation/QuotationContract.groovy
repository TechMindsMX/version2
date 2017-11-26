package com.modulus.uno.quotation

import com.modulus.uno.BusinessEntity
import com.modulus.uno.Company
import com.modulus.uno.User

class QuotationContract {

  BusinessEntity client
  BigDecimal commission
  Date initDate
  QuotationContractStatus status = QuotationContractStatus.ACTIVE

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company:Company]
  static hasMany = [users:User]

  static constraints = {
    commission min:0.0, max:16.0
    users nullable:true
  }
}
