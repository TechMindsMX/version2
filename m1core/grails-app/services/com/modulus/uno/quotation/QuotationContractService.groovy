package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.Company

class QuotationContractService {

    @Transactional
    def create(QuotationContract quotationContract){
      quotationContract.save()
      log.info "QuotationContract: ${quotationContract.dump()}"
      quotationContract
    }

    @Transactional
    def update(QuotationContractCommand quotationContractCommand, Integer id){
      QuotationContract quotationContract = QuotationContract.get(id)
      quotationContract.commission = quotationContractCommand.getQuotationContract().commission
      quotationContract.initDate = quotationContractCommand.getQuotationContract().initDate
      quotationContract.save()
    }

    @Transactional
    def paymentPayed(QuotationPaymentRequest quotationPaymentRequest){
      def type
      Map typeProcess =[
                       'SEND': {respond -> type ="Enviand...."},
                       'PAYED':{respond -> type= "Pagando..."}
                       ]

      typeProcess['SEND']
    }

    @Transactional
    Map getBalance(QuotationContract quotationContract, BigDecimal saldoAnterior){
       List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationRequestStatus.PROCESSED)
       List<QuotationPaymentRequest> quotationPaymentRequestlistPayed = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.PAYED)
       List<QuotationPaymentRequest> quotationPaymentRequestlistSend = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.SEND)
       BigDecimal income = quotationRequest.collect{ it.amount }.sum() ?: 0
       BigDecimal transit = quotationPaymentRequestlistSend.collect{ it.amount }.sum() ?: 0
       BigDecimal expenses = quotationPaymentRequestlistPayed.collect{ it.amount }.sum() ?: 0
       BigDecimal available = income - transit - expenses
       BigDecimal total = available + transit
       def mergeConcept = mergeList(quotationRequest,quotationPaymentRequestlistPayed, saldoAnterior)
       println "..."*100
       mergeConcept.quotationConceptList = mergeConcept.quotationConceptList.sort{it.date}
       mergeConcept.quotationConceptList.each{
        println it.dump()

       }

      [quotationContract:quotationContract,
      income:income,
      transit:transit,
      expenses:expenses,
      available:available,
      total:total,
      mergeConcept:mergeConcept
      ]
    }

    List<QuotationPaymentRequest> getQuotationPaymentRequestList(QuotationContract quotationContract, Date initDate, Date lastDate){
      QuotationPaymentRequest.findAllByQuotationContractAndDateCreatedBetween(quotationContract, initDate, lastDate)
    }

    @Transactional
    Map caculateData(QuotationContract quotationContract, Date initDate){
      def quotationRequest = QuotationRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, QuotationRequestStatus.PROCESSED, initDate)*.amount.sum() ?: 0
      def quotationPaymentRequest = QuotationPaymentRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, [QuotationPaymentRequestStatus.SEND, QuotationPaymentRequestStatus.PAYED], initDate)*.amount.sum() ?: 0
      def saldoAnterior = quotationRequest - quotationPaymentRequest
      [saldoAnterior:saldoAnterior,
        quotationRequest:quotationRequest,
        quotationPaymentRequest:quotationPaymentRequest]
    }

    def mergeList(List<QuotationRequest> quotationRequestList, List<QuotationPaymentRequest> quotationPaymentRequestlistPayed, BigDecimal saldoAnterior){
      def mergeLists = quotationRequestList + quotationPaymentRequestlistPayed
      BigDecimal saldoBeforeOneList = 0 + saldoAnterior
      List<QuotationConcept> quotationConceptList = []
      quotationRequestList.each{ request ->
        saldoBeforeOneList = saldoBeforeOneList + request.amount
        QuotationConcept quotationConcept = new QuotationConcept()
        quotationConcept.concept = "Deposito"
        quotationConcept.date = request.dateCreated
        quotationConcept.payment = request.amount
        quotationConcept.saldo = saldoBeforeOneList
        quotationConceptList << quotationConcept
      }

      quotationPaymentRequestlistPayed.each{ paymentRequest ->
         saldoBeforeOneList = saldoBeforeOneList - paymentRequest.amount
         QuotationConcept quotationConcept = new QuotationConcept()
         quotationConcept.concept = "Pago"
         quotationConcept.date = paymentRequest.dateCreated
         quotationConcept.charge = paymentRequest.amount
         quotationConcept.saldo = saldoBeforeOneList

        quotationConceptList << quotationConcept
      }

      [quotationConceptList:quotationConceptList]
    }
}
