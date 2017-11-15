package com.modulus.uno

import grails.converters.JSON

class Profile {
  String name
  String lastName
  String motherLastName
  String email
  String rfc
  String curp
  String trademark
  String fullName
  Date birthDate

  Gender gender
  Nationality nationality

  static belongsTo = [User]

  static hasMany = [
  telephones : Telephone,
  documents : S3Asset
  ]

  static transients = ['fullName']

  static constraints = {
    name blank:false,size:1..100
    lastName blank:false,size:1..100
    motherLastName blank:false,size:1..100
    email blank:false,email:true,size:6..200
    birthDate nullable:true
    gender nullable:true
    nationality nullable:true
    trademark nullable:true,blank:false,size:1..100
    rfc nullable:true,blank:false,size:10..50,matches:/^[A-Z]{4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/
    curp nullable:true,blank:false,size:18..18,matches:/^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$/
  }

  String getFullName(){
    "${name} ${lastName} ${motherLastName}"
  }

  String info() {
    "$rfc - $curp - $email"
  }

  static marshaller = {
    JSON.registerObjectMarshaller(Profile, 1) { m ->
      return [
      id: m.id,
      name: m.name,
      lastName: m.lastName,
      motherLastName: m.motherLastName,
      email: m.email,
      rfc: m.rfc,
      curp: m.curp,
      trademark: m.trademark,
      fullName: m.fullName,
      birthDate: m.birthDate,
      gender: m.gender,
      nationality: m.nationality
      ]
    }
  }

}
