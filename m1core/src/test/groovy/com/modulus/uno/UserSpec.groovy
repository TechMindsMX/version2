package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock

@Mock([User,Profile])
class UserSpec extends Specification {

  @Unroll
  void """When we have a user with email: #email, emailCheck: #emailCheck, name: #name, lastName: #lastName, motherLastName: #motherLastName, username: #username, password: #password, passwordCheck: #passwordCheck we expect result: #result"""(){
  given:"An user"
    UserCommand user = new UserCommand()
  when:"We assign values to command"
    user.email = email
    user.emailCheck = emailCheck
    user.name = name
    user.motherLastName = motherLastName
    user.lastName = lastName
    user.username = username
    user.password = password
    user.passwordCheck = passwordCheck
    user.terms = terms
    user.legal = false
  then:"We validate command"
    result == user.validate()
  where:"We have next cases"
  email              | emailCheck         | name | lastName | motherLastName | username | password             | passwordCheck        | terms  || result
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | true   || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1=_-¿?¡!@#\$%^&*" | "aA1=_-¿?¡!@#\$%^&*" | true   || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "M"            | "josdem" | "aA12345678"         | "aA12345678"         | true   || true
  null               | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  ""                 | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem"           | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "jos@gmail"        | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | null | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | ""   | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | null     | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | ""       | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | null           | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | ""             | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | null     | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | ""       | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | null                 | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | ""                   | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josde"  | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@gmail.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "AA1234567!"         | "AA1234567!"         | true   || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | false  || false
  }

  @Unroll
  void """When we have a user with email: #email, emailCheck: #emailCheck, name: #name, lastName: #lastName, motherLastName: #motherLastName, username: #username, password: #password, passwordCheck: #passwordCheck, rfc: #rfc, trademark: #trademark, birthDate: #birthDate, curp: #curp, nationality: #nationality, gender: #gender we expect result: #result"""(){
  given:"An user"
    UserCommand user = new UserCommand()
  when:"We assign values to command"
    user.email = email
    user.emailCheck = emailCheck
    user.name = name
    user.motherLastName = motherLastName
    user.lastName = lastName
    user.username = username
    user.password = password
    user.passwordCheck = passwordCheck
    user.rfc = rfc
    user.trademark = trademark
    user.curp = curp
    user.birthDate = birthDate
    user.nationality = nationality
    user.gender = gender
    user.terms = terms
    user.number = number
    user.extension = extension
    user.telephoneType = telephoneType
    user.legal = true
  then:"We validate command"
    result == user.validate()
  where:"We have next cases"
  email              | emailCheck         | name | lastName | motherLastName | username | password             | passwordCheck        | rfc              | trademark   | curp                 | birthDate  | nationality          | gender           | terms  | number       | extension  | telephoneType          || result
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1=_-¿?¡!@#\$%^&*" | "aA1=_-¿?¡!@#\$%^&*" | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "M"            | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  null               | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  ""                 | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem"           | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "jos@gmail"        | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | null | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | ""   | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | null     | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | ""       | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | null           | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | ""             | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | null     | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | ""       | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | null                 | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | ""                   | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josde"  | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@gmail.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA12345678"         | "aA12345678"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "AA1234567!"         | "AA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001AB'   | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | '123'            | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | ''               | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | null             | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | ''          | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | null        | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || true
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB012'| new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB0'  | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR'               | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | ''                   | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | null                 | new Date() | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | null       | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | null                 | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | null             | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() | Nationality.MEXICANA | Gender.MASCULINO | false  | '0123456789' | '123'      | TelephoneType.CASA     || false
  "josdem@email.com" | "josdem@email.com" | "J"  | "DLC"    | "Morales"      | "josdem" | "aA1234567!"         | "aA1234567!"         | 'MOCS801001ABC'  | 'trademark' | 'GACR800607HDFSRB01' | new Date() + 30 | Nationality.MEXICANA | Gender.MASCULINO | true   | '0123456789' | '123'      | TelephoneType.CASA     || false
  }

}
