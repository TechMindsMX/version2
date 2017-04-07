package com.modulus.uno

class FeesReceipt {

  String uuid = UUID.randomUUID().toString().replace('-','')[0..15]
  String collaboratorName
  BigDecimal amount = 0.00
  BigDecimal iva = 0.00
  BigDecimal isr = 0.00
  BigDecimal ivaWithHolding = 0.00
  BankAccount bankAccount

  FeesReceiptStatus status = FeesReceiptStatus.CREADA
  RejectReason rejectReason
  String comments

  static belongsTo = [company:Company]
  static hasMany = [documents:S3Asset, authorizations: Authorization]

  static constraints = {
    amount min:0.1,max:250000000.00
    iva min:0.0,max:250000000.00
    isr min:0.0,max:250000000.00
    ivaWithHolding min:0.0,max:250000000.00
    rejectReason nullable:true
    comments nullable:true
    bankAccount nullable:true
    collaboratorName nullable:true
  }

  void setAmount(BigDecimal amount){
    this.amount = amount
    this.iva = amount * 0.16
    this.ivaWithHolding = iva * 2 / 3
    this.isr = amount * 0.1
  }

  BigDecimal getNetAmount() {
    this.amount + this.iva - this.ivaWithHolding - this.isr
  }

}
