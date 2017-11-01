package com.modulus.uno.quotation
import  com.modulus.uno.BusinessEntityService

import com.modulus.uno.Company

class QuotationContractController {

    BusinessEntityService businessEntityService
    QuotationContractService quotationContractService

    def index(){
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      respond new QuotationContract(), model:[quotationContractList: quotationContractList, company:company]
    }

    def create(){
    	Company company = Company.get(session.company)

      def clients = businessEntityService.findBusinessEntityByKeyword("","CLIENT" , company)
    	respond new QuotationContract(), model:[company:company,
                                              clients:clients
                                              ]
    }

    def save(QuotationContractCommand quotationContractCommand){
    	Company company = Company.get(session.company)
      quotationContractService.create(quotationContractCommand, company)
       redirect action: 'create'
    }

    def edit(String id){
      QuotationContract quotationContract = QuotationContract.get(id.toInteger())

      [quotationContract:quotationContract]
    }

    def update(QuotationContractCommand quotationContractCommand){
      quotationContractService.update(params.id, quotationContractCommand.getCommission())
       redirect(action: 'edit', id: params.id)
    }

    def show(String id){
      Company company = Company.get(session.company)
      QuotationContract quotationContract = QuotationContract.get(id.toInteger())
      [quotationContract:quotationContract, company:company]
    }



}
