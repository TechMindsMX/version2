package com.modulus.uno.quotation

import grails.transaction.Transactional

@Transactional
class QuotationRequestService {

    def serviceMethod() {

    }

    QuotationRequest create(QuotationRequest quotationRequest){
      quotationRequest.save()

    }

    QuotationRequest update(QuotationRequest quotationRequest){
      quotationRequest.save()

    }
}
