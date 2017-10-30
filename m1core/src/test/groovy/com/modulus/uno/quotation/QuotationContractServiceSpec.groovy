package com.modulus.uno.quotation

import grails.test.mixin.TestFor
import spock.lang.Specification
import com.modulus.uno.BusinessEntity

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuotationContractService)
class QuotationContractServiceSpec extends Specification {




    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }

    void "Save quotation"(){
      given:"A quotation"
        QuotationContract quotationContrac = new QuotationContract(
                                                  client: BusinessEntity.get(1),
                                                  commision: 10,
                                                  initDate: new Date()
                                                  )

      when:"shouuld save"
        service.save(quotationContrac)
      then:
        quotationContrac
    }



}
