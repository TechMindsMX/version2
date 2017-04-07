package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.TestFor

@TestFor(Telephone)
class TelephoneSpec extends Specification {

  @Unroll
  void """When we have a telephone with number: #number, type: #type, extension: #extension we expect result: #result"""() {
  given: "A telephone"
    def telephone = new Telephone()
  when: "We assign values"
    telephone.number = number
    telephone.extension = extension
    telephone.type = type
  then: "We validate result"
   result == telephone.validate()
  where: "We have next values"
  number        | type                   | extension  || result
  '1234567890'  | TelephoneType.CELULAR  | '123'      || true
  '1234567890'  | TelephoneType.TRABAJO  | '123'      || true
  '1234567890'  | TelephoneType.CASA     | '123'      || true
  '1234567890'  | TelephoneType.CELULAR  | '1'        || true
  '1234567890'  | TelephoneType.TRABAJO  | null       || true
  '12345678901' | TelephoneType.TRABAJO  | '123'      || false
  '123456789'   | TelephoneType.TRABAJO  | '123'      || false
  'ABCDEFGHIJ'  | TelephoneType.TRABAJO  | '123'      || false
  'ABCDE12345'  | TelephoneType.TRABAJO  | '123'      || false
  'abcde12345'  | TelephoneType.TRABAJO  | '123'      || false
  ''            | TelephoneType.TRABAJO  | '123'      || false
  null          | TelephoneType.TRABAJO  | '123'      || false
  '1234567890'  | TelephoneType.CELULAR  | ''         || true
  }

}
