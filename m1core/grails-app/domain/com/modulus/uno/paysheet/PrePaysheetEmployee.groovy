package com.modulus.uno.paysheet

import com.modulus.uno.Bank

class PrePaysheetEmployee {
  String rfc
  String curp
  String numberEmployee
  String nameEmployee
  Bank bank
  String clabe
  String branch
  String account
  String cardNumber
  BigDecimal netPayment
  String note

  static belongsTo = [prePaysheet:PrePaysheet]
  static hasMany = [incidences:PrePaysheetEmployeeIncidence]

  static constraints = {
    bank nullable:true
    clabe nullable:true
    account nullable:true
    cardNumber nullable:true
    note nullable:true, blank:true
    branch nullable:true
  }
}
