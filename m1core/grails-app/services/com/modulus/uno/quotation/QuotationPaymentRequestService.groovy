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

  Map process(QuotationPaymentRequest quotationPaymentRequest){
    //QuotationContract quotationContract= quotationPaymentRequest.quotationContract
    List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatus(quotationPaymentRequest.quotationContract, QuotationRequestStatus.PROCESSED)
    List<QuotationPaymentRequest> quotationPaymentRequestlistPayed = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationPaymentRequest.quotationContract, QuotationPaymentRequestStatus.PAYED)
    List<QuotationPaymentRequest> quotationPaymentRequestlistSend = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationPaymentRequest.quotationContract, QuotationPaymentRequestStatus.SEND)
    BigDecimal income = quotationRequest.collect{ it.amount }.sum()
    BigDecimal transit = quotationPaymentRequestlistSend.collect{ it.amount }.sum()
    BigDecimal expenses = quotationPaymentRequestlistPayed.collect{ it.amount }.sum()
    BigDecimal available = income - transit - expenses
    BigDecimal total = available + transit
    quotationRequest
    quotationPaymentRequest.status = QuotationPaymentRequestStatus.PAYED
    quotationPaymentRequest.save()
    [quotationPaymentRequest: quotationPaymentRequest,
      available:available, transit:transit, total:total]
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
