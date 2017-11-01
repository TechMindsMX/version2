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
      quotationContract.save()

    }

    @Transactional
    def update(id, commission){
      QuotationContract quotationContract = QuotationContract.get(id.toInteger())
      quotationContract.commission = commission
      quotationContract.save()
      println "En el servicio"
    }
}
