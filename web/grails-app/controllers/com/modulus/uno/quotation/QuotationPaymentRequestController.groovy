package com.modulus.uno.quotation
import com.modulus.uno.Company

class QuotationPaymentRequestController {


  QuotationPaymentRequestService quotationPaymentRequestService

    def index() {
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      respond new QuotationContract(), model:[quotationContractList: quotationContractList, company:company]
    }

    def show(QuotationPaymentRequest quotationPaymentRequest) {

      respond quotationPaymentRequest
    }

    def selectPaymentRequest(String quotation){
      QuotationContract quotationContract = QuotationContract.get(quotation.toLong())
      def quotationPaymentRequestList = QuotationPaymentRequest.findAllByQuotationContract(quotationContract)
      render view: 'index', model:[quotationPaymentRequestList: quotationPaymentRequestList]
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
