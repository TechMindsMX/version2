package com.modulus.uno

import spock.lang.Specification
import spock.lang.Unroll
import grails.test.mixin.Mock
import grails.test.mixin.TestFor

@TestFor(Corporate)
@Mock([Company,User,Commission])
class CorporateSpec extends Specification {

  @Unroll
  void """ create a corporate and validate """() {
    given:
      def corporate = new Corporate()
    when:
      corporate.nameCorporate = corporateName
      corporate.corporateUrl = urlCorporate
    then:
      result == corporate.validate()
    where:
      corporateName | urlCorporate || result
      "says"        | "says-inc"   || true
      "makingdevs"  | "makingdevs" || true
      null          | "algo"       || false
      "algo"        | null         || false
      null          | null         || false
  }


}
