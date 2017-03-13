package com.modulus.uno

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(DepositOrderService)
@Mock([DepositOrder,User,Profile,Company,ModulusUnoAccount])
class DepositOrderServiceSpec extends Specification {


}
