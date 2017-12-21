package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.quotation.QuotationRequest

@Transactional
class QuotationPaymentRequestService {

  QuotationRequestService quotationRequestService
  QuotationContractService quotationContractService

  QuotationPaymentRequest create(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest update(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest process(QuotationPaymentRequest quotationPaymentRequest){
    //QuotationContract quotationContract= quotationPaymentRequest.quotationContract

    quotationPaymentRequest.status = QuotationPaymentRequestStatus.PAYED
    quotationPaymentRequest.save()

  }

  QuotationPaymentRequest send(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.status = QuotationPaymentRequestStatus.SEND
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest delete(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.delete()
  }

  def getAmountFromQuotationRequest(){
    def amount = quotationRequest.total
  }

  Map calaculateSummary(String quotation){
    QuotationContract quotationContract = QuotationContract.get(quotation.toInteger())
    quotationContractService.calculateSummaryForBalance(quotationContract)
  }

}
