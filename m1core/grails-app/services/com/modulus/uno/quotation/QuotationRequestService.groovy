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

    def delete(QuotationRequest quotationRequest){
      quotationRequest.delete()
    }

    QuotationRequest requestProcessed(QuotationRequest quotationRequest){
      quotationRequest.status = QuotationRequestStatus.PROCESSED
      quotationRequest.save()
    }

    QuotationRequest sendQuotation(QuotationRequest quotationRequest){
      quotationRequest.status = QuotationRequestStatus.SEND
      quotationRequest.save()
    }
}
