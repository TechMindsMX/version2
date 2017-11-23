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
    Map getBalance(QuotationContract quotationContract){
       List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationRequestStatus.PROCESSED)
       List<QuotationPaymentRequest> quotationPaymentRequestlistPayed = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.PAYED)
       List<QuotationPaymentRequest> quotationPaymentRequestlistSend = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.SEND)
       BigDecimal income = quotationRequest.collect{ it.amount }.sum() ?: 0
       BigDecimal transit = quotationPaymentRequestlistSend.collect{ it.amount }.sum() ?: 0
       BigDecimal expenses = quotationPaymentRequestlistPayed.collect{ it.amount }.sum() ?: 0
       BigDecimal available = income - transit - expenses
       BigDecimal total = available + transit
       quotationRequest

      [quotationContract:quotationContract,
      income:income,
      transit:transit,
      expenses:expenses,
      available:available,
      total:total
      ]
    }

    List<QuotationPaymentRequest> getQuotationPaymentRequestList(QuotationContract quotationContract, Date initDate, Date lastDate){
      QuotationPaymentRequest.findAllByQuotationContractAndDateCreatedBetween(quotationContract, initDate, lastDate)
    }
}
