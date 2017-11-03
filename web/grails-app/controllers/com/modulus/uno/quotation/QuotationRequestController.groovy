package com.modulus.uno.quotation

import com.modulus.uno.Company

class QuotationRequestController {

    QuotationRequestService quotationRequestService

    def index() { }


    def create(){
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)

      [company:company,
      quotationContractList:quotationContractList]
    }

    def save(QuotationRequestCommand quotationRequestCommand ){
      def quotationRequest = quotationRequestCommand.getQuotationRequest()
      quotationRequestService.create(quotationRequest)
      redirect(action: 'show', id: quotationRequest.id)
    }

    def show(QuotationRequest quotationRequest){

      [quotationRequest: quotationRequest]
    }

    def edit(QuotationRequest quotationRequest){

      [quotationRequest:quotationRequest]
    }

    def update(QuotationRequestCommand quotationRequestCommand){
        QuotationRequest quotationRequestUpdate = quotationRequestCommand.getQuotationRequest()
        QuotationRequest quotationRequest = QuotationRequest.get(params.id.toInteger())
        quotationRequest.description = quotationRequestUpdate.description
        quotationRequest.commission = quotationRequestCommand.getCommission(quotationRequestCommand.commission)
        quotationRequest.amount = quotationRequestUpdate.amount
        quotationRequestService.update(quotationRequest)

      redirect(action: 'show', id: quotationRequest.id)
    }
}
