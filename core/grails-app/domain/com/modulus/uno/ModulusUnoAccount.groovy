package com.modulus.uno

class ModulusUnoAccount {

  String account
  String balance
  String integraUuid
  String stpClabe
  String timoneUuid
  String aliasStp

  Date dateCreated
  Date lastUpdated

  static belongsTo = [company: Company]

  static constraints = {
    account nullable:false
    balance nullable:false
    integraUuid nullable:false
    stpClabe nullable:false
    timoneUuid nullable:false
    aliasStp nullable:true
  }
}
