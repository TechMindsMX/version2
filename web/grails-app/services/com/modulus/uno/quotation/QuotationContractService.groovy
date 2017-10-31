package com.modulus.uno.quotation

import grails.transaction.Transactional

@Transactional
class QuotationContractService {

    def serviceMethod() {

    }

    @Transactional
    def create(QuotationContractCommand quotationContractCommand){
      def quotationContract = quotationContractCommand.getQuotationContract()
      println "*"*100
      println quotationContract.dump()
      quotationContract.save()

    }
}
