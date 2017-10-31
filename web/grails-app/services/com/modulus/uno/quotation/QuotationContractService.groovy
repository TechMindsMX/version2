package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.Company

@Transactional
class QuotationContractService {

    def serviceMethod() {

    }

    @Transactional
    def create(QuotationContractCommand quotationContractCommand, Company company){
      def quotationContract = quotationContractCommand.getQuotationContract(company)
      println quotationContractCommand.dump()
      println "*"*100
      println quotationContract.dump()
      quotationContract.save()

    }
}
