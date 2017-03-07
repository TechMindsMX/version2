package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(EmailerClientService)
@Mock([User])
class EmailerClientServiceSpec extends Specification {

  def setup() {
  }

}
