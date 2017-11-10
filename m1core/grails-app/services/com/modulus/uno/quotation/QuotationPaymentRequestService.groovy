package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.quotation.QuotationRequest

@Transactional
class QuotationPaymentRequestService {

  QuotationRequestService quotationRequestService

  QuotationPaymentRequest create(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest update(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest process(QuotationPaymentRequest quotationPaymentRequest){
    //QuotationContract quotationContract= quotationPaymentRequest.quotationContract
    List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatus(quotationPaymentRequest.quotationContract, QuotationRequestStatus.PROCESSED)
    List<QuotationPaymentRequest> quotationPaymentRequestlist = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationPaymentRequest.quotationContract, QuotationPaymentRequestStatus.PAYED)
    println "-----"
    println quotationPaymentRequestlist.dump() 
    println quotationRequest.dump()
    def amount = 0
    quotationRequest.each{
      amount = amount + it.amount 
    }

    println amount
    quotationRequest
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
    def amount = quotationRequest.amount 
  }

}
