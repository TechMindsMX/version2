package com.modulus.uno

class MovimientosBancarios {

  String concept
  String reference
  BigDecimal amount
  Date dateEvent
  MovimientoBancarioType type


  Date dateCreated
  Date lastUpdated

  static belongsTo = [cuenta:BankAccount]

  static constraints = {
    concept blank:false, size:5..200
    reference blank:true, nullable:true, size:5..200
    amount min:0.0

  }


}
