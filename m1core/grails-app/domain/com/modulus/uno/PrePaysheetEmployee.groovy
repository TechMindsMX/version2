package com.modulus.uno

class PrePaysheetEmployee {
  String rfc
  String curp
  String numberEmployee
  String nameEmployee
  Bank bank
  String clabe
  String account
  String cardNumber
  BigDecimal netPayment
  String note

  static belongsTo = [prePaysheet:PrePaysheet]

  static constraints = {
    bank nullable:true
    clabe nullable:true
    account nullable:true
    cardNumber nullable:true
    note nullable:true, blank:true
  }
}
