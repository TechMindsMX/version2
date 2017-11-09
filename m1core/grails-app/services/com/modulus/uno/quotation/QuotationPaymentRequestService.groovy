package com.modulus.uno.quotation

import grails.transaction.Transactional

@Transactional
class QuotationPaymentRequestService {


  QuotationPaymentRequest create(QuotationPaymentRequest quotationPaymentRequest){
    quotationPaymentRequest.save()
  }
}
