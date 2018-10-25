package com.modulus.uno.invoice.paymentComplement

class Payment {
  String paymentDate
  String paymentWay
  String currency
  String amount
  String sourceBankRfc
  String sourceAccount
  String destinationBankRfc
  String destinationAccount

  List<RelatedDocument> relatedDocuments
}
