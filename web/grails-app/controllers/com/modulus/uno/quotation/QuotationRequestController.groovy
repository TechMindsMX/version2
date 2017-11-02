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
      redirect(action: 'create')
    }
}
