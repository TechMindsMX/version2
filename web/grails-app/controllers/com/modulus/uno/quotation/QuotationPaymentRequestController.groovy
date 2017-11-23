package com.modulus.uno.quotation
import com.modulus.uno.Company

class QuotationPaymentRequestController {


  QuotationPaymentRequestService quotationPaymentRequestService

    def index() {
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      respond new QuotationContract(), model:[quotationContractList: quotationContractList, company:company]
    }

    def consult() {
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
        if(!quotationPaymentRequestList){
           Company company = Company.get(session.company)
           List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
           render view: 'index', model:[quotationContractList: quotationContractList]
           return
        }
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

    def update(QuotationPaymentRequestCommand quotationPaymentRequestCommand){
      QuotationPaymentRequest quotationPaymentRequestUpdate = quotationPaymentRequestCommand.getQuotationPaymentRequest()
      QuotationPaymentRequest quotationPaymentRequest = QuotationPaymentRequest.get(params.id.toLong())
      quotationPaymentRequest.amount = quotationPaymentRequestUpdate.amount
      quotationPaymentRequest.note = quotationPaymentRequestUpdate.note
      quotationPaymentRequest.paymentWay = quotationPaymentRequestUpdate.paymentWay
      quotationPaymentRequestService.update(quotationPaymentRequest)
      redirect(action:'show', id:quotationPaymentRequest.id)
    }

    def edit(QuotationPaymentRequest quotationPaymentRequest){
      respond quotationPaymentRequest
    }

    def send(QuotationPaymentRequest quotationPaymentRequest){
      quotationPaymentRequestService.send(quotationPaymentRequest)
      redirect(action:'index')
    }

    def delete(QuotationPaymentRequest quotationPaymentRequest){
      quotationPaymentRequestService.delete(quotationPaymentRequest)
      redirect(action:'index')
    }

    def process(QuotationPaymentRequest quotationPaymentRequest){
      quotationPaymentRequestService.process(quotationPaymentRequest)
      redirect(action:'index')
    }

}
