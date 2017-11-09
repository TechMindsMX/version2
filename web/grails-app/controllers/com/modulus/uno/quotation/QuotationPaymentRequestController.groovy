package com.modulus.uno.quotation
import com.modulus.uno.Company

class QuotationPaymentRequestController {


  QuotationPaymentRequestService quotationPaymentRequestService

    def index() { }

    def show(QuotationPaymentRequest quotationPaymentRequest) {

      respond quotationPaymentRequest
    }

    def create(){
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)

      [company:company,
      quotationContractList:quotationContractList]

    }

    def save(QuotationPaymentRequestCommand quotationPaymentRequestCommand){
      QuotationPaymentRequest quotationPaymentRequest = quotationPaymentRequestCommand.getQuotationPaymentRequest()
      quotationPaymentRequestService.create(quotationPaymentRequest)
      redirect(action: 'show', id: quotationPaymentRequest.id)
    }
}
