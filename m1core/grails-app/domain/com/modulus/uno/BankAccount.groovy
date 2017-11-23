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
    accountNumber maxSize:11, nullable:true, blank:true, validator: { val, obj ->
      if(!val && !obj.clabe && !obj.cardNumber) {
        return ['missing Account Number']
      }
    }
    branchNumber blank:false
    clabe minSize:18, maxSize:18, nullable:true, blank:true, validator:{ val, obj ->
      if(val && !(val[val.size()-1].toInteger() == getControlDigit(val[0..(val.size()-2)]))){
        return ['wrongClabe']
      }
      if(!val && !obj.accountNumber && !obj.cardNumber) {
        return ['missing Clabe']
      }
    }
    cardNumber nullable:true, blank:true, validator: { val, obj ->
      if(!val && !obj.accountNumber && !obj.clabe) {
        return ['missing card number']
      }
    }
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
    String number = (clabe ? clabe : (accountNumber ? "${branchNumber}/${accountNumber}" : cardNumber))
    "${banco} - ${number}"
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
