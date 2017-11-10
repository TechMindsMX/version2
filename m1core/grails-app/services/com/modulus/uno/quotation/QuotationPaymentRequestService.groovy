package com.modulus.uno.quotation

import grails.transaction.Transactional

@Transactional
class QuotationPaymentRequestService {


  QuotationPaymentRequest create(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest update(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }

  QuotationPaymentRequest process(QuotationPaymentRequest quotationPaymentRequest){
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

}
