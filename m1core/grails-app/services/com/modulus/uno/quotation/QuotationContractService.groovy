package com.modulus.uno.quotation

import grails.transaction.Transactional

@Transactional
class QuotationContractService {

    def serviceMethod() {

    }

    def save(QuotationContract quotationContract){
      quotationContract.save()
    }
}
