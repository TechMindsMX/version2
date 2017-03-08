package com.modulus.uno

class FeesReceiptModulusunoCommand implements MessageCommand {
  String uuid
  String clabe
  String bankCode
  BigDecimal amount
  BigDecimal iva
  BigDecimal isr
  String beneficiary
  String emailBeneficiary
  String concept
  String payerName
  String payerClabe
}
