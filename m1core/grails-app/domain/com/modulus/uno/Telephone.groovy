package com.modulus.uno

class Telephone {
  String number
  String extension
  TelephoneType type

  static belongsTo = [Profile, Company, ContactInformation]

  static constraints = {
    number blank:false,matches:/^[0-9]*$/,size:10..10
    extension nullable:true,blank:true,matches:/^[0-9]*$/,size:1..10
  }

  String toString() {
    "$number - ${extension ?: ''}  $type"
  }

}
