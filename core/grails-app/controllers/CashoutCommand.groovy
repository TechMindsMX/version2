package com.modulus.uno

class CashoutCommand implements MessageCommand {

  String uuid
  String clabe
  String bankCode
  BigDecimal amount
  BigDecimal fee
  String beneficiary
  String concept
  String feeType
}
