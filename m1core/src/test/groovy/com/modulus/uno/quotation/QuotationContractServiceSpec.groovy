package com.modulus.uno.quotation

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import com.modulus.uno.BusinessEntity

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(QuotationContractService)
@Mock([BusinessEntity, QuotationPaymentRequest, QuotationContract, QuotationRequest])
class QuotationContractServiceSpec extends Specification {



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
        quotationContract.save(validate:false)
      then:
        quotationContract
    }

    void "Metodo de pago"(){
      given:"Quotation payment request"
        QuotationPaymentRequest quotationPaymentRequest = new QuotationPaymentRequest()
        quotationPaymentRequest.save(validate:false)
      when:
        def payment =service.paymentPayed(quotationPaymentRequest)
      then:
        payment

    }

    void "Calculate summary for balance"(){
      given:"Give QuotationContract"
        QuotationContract quotationContract = new QuotationContract().save(validate:false)
      and:"The requests"
        QuotationRequest request1 = new QuotationRequest(quotationContract:quotationContract, status:QuotationRequestStatus.PROCESSED, amount:1000).save(validate:false)
        QuotationRequest request2 = new QuotationRequest(quotationContract:quotationContract, status:QuotationRequestStatus.PROCESSED, amount:2000).save(validate:false)
        QuotationRequest request3 = new QuotationRequest(quotationContract:quotationContract, status:QuotationRequestStatus.SEND, amount:2000).save(validate:false)
      when:""
        Map summary = service.calculateSummaryForBalance(quotationContract)
      then:""
        summary.income == new BigDecimal(3000)
        summary.transit == new BigDecimal(0)
    }

    QuotationContract getQuotationContract(){
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
    }




}
