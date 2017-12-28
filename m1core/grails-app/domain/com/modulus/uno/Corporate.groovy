package com.modulus.uno

class Corporate {

  String nameCorporate
  String corporateUrl
  CorporateStatus status = CorporateStatus.ENABLED

  static  hasMany =[companies:Company,users: User]

  static constraints = {
    nameCorporate nullable:false, unique:true
    corporateUrl nullable:false, unique:true
  }
}
