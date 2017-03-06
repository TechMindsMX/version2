package com.modulus.uno

class CompanyRejectedLog {

  Long companyId
  String typeClass
  String reason
  Boolean status
  Long assetId

  Date dateCreated

  static constraints = {
    companyId nullable:false
    typeClass nullable:false
    reason nullable:false
    status nullable:false
    assetId nullable:true
  }

  String toString() {
    "$reason"
  }
}
