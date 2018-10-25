package com.modulus.uno

class Bank {

  String bankingCode
  String name
  String rfc

  static constraints = {
    bankingCode blank:false
    name blank:false
    rfc nullable:true
  }

  String toString(){
    name
  }
}
