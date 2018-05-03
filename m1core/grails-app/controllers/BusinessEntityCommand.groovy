package com.modulus.uno

import grails.validation.Validateable

class BusinessEntityCommand implements Validateable {

  String rfc
  String curp
  String website
  String name
  String lastName
  String motherLastName
  String businessName
  String number
  String email

  BusinessEntityType type
  LeadType clientProviderType

  static constraints = {
    website nullable:true,blank:false,size:5..50,url:true
    number nullable:true
    email nullable:true, blank:true, email:true, size:6..200
    curp(nullable:true, blank:true,size:10..50,validator: { val, obj ->
      if(obj.clientProviderType == LeadType.EMPLEADO && obj.type == BusinessEntityType.FISICA && !(val ==~ /^[A-Z]{4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]+)$/) ) {
        false
      } else {
        true
      }
    })
    rfc(blank:false,size:10..50, validator: { val, obj ->
      if(obj.type == BusinessEntityType.FISICA && !(val.toUpperCase() ==~ /^[A-Z]{4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/) ) {
        return false
      } else if (obj.type == BusinessEntityType.MORAL && !(val.toUpperCase() ==~ /^[A-Z]{3}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/) ) {
        return false
      }
    })
    clientProviderType(nullable:false)
    name(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.type == BusinessEntityType.FISICA) {
        return false
      }
    })
    lastName(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.type == BusinessEntityType.FISICA) {
        return false
      }
    })
    motherLastName(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.type == BusinessEntityType.FISICA) {
        return false
      }
    })
    businessName(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.type == BusinessEntityType.MORAL) {
        return false
      } else {
        true
      }
    })
  }
}
