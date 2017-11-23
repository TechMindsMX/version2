package com.modulus.uno

import grails.validation.Validateable

class UserCommand implements Validateable {
  String username
  String password
  String email
  String name
  String lastName
  String motherLastName
  String rfc
  String curp
  String trademark
  String number
  String extension
  String telephone
  Boolean legal
  Boolean terms

  Date birthDate

  Gender gender
  Nationality nationality
  TelephoneType telephoneType

  String emailCheck
  String passwordCheck

  static constraints = {
    name blank:false,size:1..100
    lastName blank:false,size:1..100
    motherLastName blank:false,size:1..100
    username blank:false,size:6..50
    email blank:false,email:true,size:6..200
    trademark nullable:true,blank:false,size:1..100
    telephone nullable:true
    legal nullable:true
    extension nullable:true,blank:false

    username(blank:false,validator:{val, obj ->
      if(User.findByUsername(val)){
        return false
      }
    })
    terms(validator:{val, obj ->
      if(!val && !obj.terms)
        return false
    })
    number(nullable:true,blank:false,matches:/^[0-9]*$/,size:10..10,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    telephoneType(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    birthDate(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.legal || val >= new Date()) {
        return false
      }
    })
    gender(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    nationality(nullable:true,blank:false,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    rfc(nullable:true,blank:false,size:10..50,matches:/^[A-Z]{4}([0-9]{2})(1[0-2]|0[1-9])([0-3][0-9])([A-Z0-9]{3})$/,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    curp(nullable:true,blank:false,size:18..18,matches:/^[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}$/,validator:{val, obj ->
      if(!val && obj.legal) {
        return false
      }
    })
    password(blank:false,size:10..50,matches:/^(?=.*\d)(?=.*[=_\-¿?¡!@#\$%^&*]+)?(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/,validator:{val, obj ->
      if(!val.equalsIgnoreCase(obj.passwordCheck)) {
        return false
      }
    })
    email(blank:false,email:true,size:6..200,validator:{val, obj ->
      if(!val.equalsIgnoreCase(obj.emailCheck)) {
        return false
      }
    })
  }

  Profile getProfile(){
    def profile = new Profile (
      name:this.name,
      lastName:this.lastName,
      motherLastName:this.motherLastName,
      email:this.email,
      rfc:this.rfc,
      curp:this.curp,
      trademark:this.trademark,
      birthDate:this.birthDate,
      gender:this.gender,
      nationality:this.nationality
    )
    profile
  }

  Telephone getTelephone(){
    def telephone
    if(this.number){
      telephone = new Telephone(
        number:this.number,
        extension:this.extension,
        type:this.telephoneType
      )
    }
    telephone
  }

}
