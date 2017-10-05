package com.modulus.uno

import grails.converters.JSON

class BankAccount {

  String accountNumber
  String branchNumber
  String clabe
  String cardNumber
  boolean concentradora = false
	String clientNumber

  static hasMany = [accountStatements:AccountStatementBankAccount]
  static belongsTo = [banco:Bank]

  static constraints = {
    accountNumber minSize:11,maxSize:11,blank:false
    branchNumber blank:false
    clabe blank:false,minSize:18,maxSize:18,validator:{ val ->
      if(!(val[val.size()-1].toInteger() == getControlDigit(val[0..(val.size()-2)])))
        return ['wrongClabe']
    }
    cardNumber nullable:true, blank:true
		clientNumber nullable:true
  }

  private static Integer getControlDigit(def clabe){
    def rules = [0:3,1:7,2:1]
    def factors = []
    def products = []
    clabe.size().times{ i ->
      factors << rules[i%3]
    }
    factors.eachWithIndex{ factor, i ->
      products << (clabe[i].toInteger()*factor)%10
    }

    new Integer(10-(products.sum()%10))
  }

  String toString() {
    "${banco} - ${clabe}"
  }

  static marshaller = {
    JSON.registerObjectMarshaller(BankAccount, 1) { m ->
      return [
      id: m.id,
      accountNumber: m.accountNumber,
      branchNumber: m.branchNumber,
      clabe: m.clabe,
      banco: m.banco
      ]
    }
  }

}
