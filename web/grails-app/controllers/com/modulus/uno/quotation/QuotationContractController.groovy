package com.modulus.uno.quotation
import  com.modulus.uno.BusinessEntityService

import com.modulus.uno.Company

class QuotationContractController {

    BusinessEntityService businessEntityService
    QuotationContractService quotationContractService

    def index(){
      List<QuotationContract> quotationContractList = QuotationContract.list()
      quotationContractList.each{
            println it.client
      }

      [quotationContractList: quotationContractList]
    }

    def show(){
    	Company company = Company.get(session.company)

      def clients = businessEntityService.findBusinessEntityByKeyword("","CLIENT" , company)
    	respond new QuotationContract(), model:[company:company,
                                              clients:clients
                                              ]
    }

    def save(QuotationContractCommand quotationContractCommand){
    	Company company = Company.get(session.company)
      quotationContractService.create(quotationContractCommand, company)
       redirect action: 'show'
    }



}
