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


    void "Merge list quotationPaymentRquest and Quotation rquest"(){
      given:"Give list quotation payment request "
        List<QuotationRequest> quotationRequestList = []
        QuotationRequest request1 = new QuotationRequest(quotationContract:quotationContract, status:QuotationRequestStatus.PROCESSED, amount:1000).save(validate:false)
        QuotationRequest request2 = new QuotationRequest(quotationContract:quotationContract, status:QuotationRequestStatus.PROCESSED, amount:2000).save(validate:false)
        quotationRequestList << request1
        quotationRequestList << request2

      and:"give list og quotation request in status payed"
        List<QuotationPaymentRequest> quotationPaymentRequestList = []
        QuotationPaymentRequest quotationPaymentRequest1 = new QuotationPaymentRequest(dateCreated: new Date(), status: QuotationPaymentRequestStatus.PAYED, amount:100).save(validate:false)
        QuotationPaymentRequest quotationPaymentRequest2 = new QuotationPaymentRequest(dateCreated: new Date(), status: QuotationPaymentRequestStatus.PAYED, amount:300).save(validate:false)
        quotationPaymentRequestList << quotationPaymentRequest1
        quotationPaymentRequestList << quotationPaymentRequest2

      when:"Merge two list"
        def merge = service.mergeList(quotationRequestList, quotationPaymentRequestList)
      then:
        merge
    }

    void "Calculate balance before"(){
      given:"Give quotation contract"
        QuotationContract quotationContract = new QuotationContract().save(validate:false)
      and:"Two request quotation"
        QuotationRequest request1 = new QuotationRequest(quotationContract:quotationContract, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:4000).save(validate:false)
        QuotationRequest request2 = new QuotationRequest(quotationContract:quotationContract, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:2000).save(validate:false)
      and:"give payment rquest quotation"
        QuotationPaymentRequest quotationPaymentRequest1 = new QuotationPaymentRequest(quotationContract:quotationContract ,dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:100).save(validate:false)
        QuotationPaymentRequest quotationPaymentRequest2 = new QuotationPaymentRequest(quotationContract:quotationContract, dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:300).save(validate:false)
      when:
        BigDecimal beforeBalance= service.getPreviousBalance(quotationContract, new Date()+90)
      then:
        beforeBalance == 6000
    }

    void "Create list of quotation balance general"(){
      given:"List of quotation contract with request and payments"
        def quotationContractList = getQuotationContractList()
      when:"was created quotation balance general concep"
        def listBalanceGeneral = service.quotationBalanceGeneralConcept(quotationContractList) 
      then:
        listBalanceGeneral
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

    List<QuotationContract> getQuotationContractList(){
      List<QuotationContract> quotationContractList = []
      QuotationContract quotationContract = new QuotationContract().save(validate:false)
      QuotationContract quotationContract2 = new QuotationContract().save(validate:false)
      QuotationRequest request1 = new QuotationRequest(quotationContract:quotationContract, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:4000).save(validate:false)
      QuotationRequest request2 = new QuotationRequest(quotationContract:quotationContract, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:2000).save(validate:false)
      QuotationRequest request3 = new QuotationRequest(quotationContract:quotationContract2, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:2000).save(validate:false)
      QuotationRequest request4 = new QuotationRequest(quotationContract:quotationContract2, dateCreated:new Date()-10, status:QuotationRequestStatus.PROCESSED, amount:3000).save(validate:false)
      QuotationPaymentRequest quotationPaymentRequest1 = new QuotationPaymentRequest(quotationContract:quotationContract ,dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:100).save(validate:false)
      QuotationPaymentRequest quotationPaymentRequest2 = new QuotationPaymentRequest(quotationContract:quotationContract, dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:300).save(validate:false)
      QuotationPaymentRequest quotationPaymentRequest3 = new QuotationPaymentRequest(quotationContract:quotationContract2 ,dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:700).save(validate:false)
      QuotationPaymentRequest quotationPaymentRequest4 = new QuotationPaymentRequest(quotationContract:quotationContract2, dateCreated: new Date()-5, status: QuotationPaymentRequestStatus.PAYED, amount:600).save(validate:false)
      quotationContractList << quotationContract
      quotationContractList << quotationContract2
      quotationContractList
    }




}
