
package com.modulus.uno.credit

import spock.lang.Specification
import java.lang.Void as Should

import grails.test.mixin.Mock
import grails.test.mixin.TestFor


@TestFor(CreditService)
@Mock([Company, Credit])
class CreditServiceSpec extends Specification {

  Should "give all companies from the corporate"() {
    given: "a corporate"
      def corporate = new Corporate()
    when: ""
    then: ""
  }
}