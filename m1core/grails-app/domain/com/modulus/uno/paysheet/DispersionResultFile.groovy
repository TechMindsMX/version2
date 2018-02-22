package com.modulus.uno.paysheet

import com.modulus.uno.S3Asset
import com.modulus.uno.Bank

class DispersionResultFile {

  Bank bank
  PaymentSchema schema
  S3Asset file

  static belongsTo = [paysheet:Paysheet]
  static hasMany = [details:DispersionResultFileDetail]

  Date dateCreated
  Date lastUpdated

}
