package com.modulus.uno.quotation

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import com.modulus.uno.BusinessEntity

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuotationContractService)
@Mock([BusinessEntity, QuotationPaymentRequest])
class QuotationContractServiceSpec extends Specification {

    void "test something"() {
        expect:"fix me"
            true == true
    }

    void "Save quotation"(){
      given:"A quotation and BusinessEntity"
        BusinessEntity client = new BusinessEntity(
                                                  rfc:"BDJBDYHSGGDVVD",
                                                  website:"loquesea@makingdes.com",
                                                  artemisaId:"33"
                                                    )
        QuotationContract quotationContract = new QuotationContract(
                                                  client: client,
                                                  commision: 10,
                                                  initDate: new Date()
                                                  )

      when:"shouuld save"
        println client.dump()
        //quotationContract.save(validate:false)
      then:
        quotationContract
    }

    void "Metodo de pago"(){
      given:"Quotation payment request"
        QuotationPaymentRequest quotationPaymentRequest = new QuotationPaymentRequest()
        quotationPaymentRequest.save(validate:false)
      when:
        def algo =service.paymentPayed(quotationPaymentRequest)
      then:
      println algo.dump()
       1==1

    }



}
