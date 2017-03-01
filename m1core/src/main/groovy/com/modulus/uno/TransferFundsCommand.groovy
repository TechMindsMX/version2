package com.modulus.uno

class TransferFundsCommand implements MessageCommand {
  String uuidOrigin
  String uuidDestination
  BigDecimal amount
  BigDecimal fee
  String typeFee
}
