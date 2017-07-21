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

  static belongsTo = [prePaySheet:PrePaysheet]

}
