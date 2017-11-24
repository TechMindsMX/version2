package com.modulus.uno.quotation
import com.modulus.uno.Company

class QuotationPaymentRequestController {


  QuotationPaymentRequestService quotationPaymentRequestService

    def index() {
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      [quotationContractList: quotationContractList, company:company]
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
      render view: 'index', model:[quotationContract:quotationContract, quotationPaymentRequestList: quotationPaymentRequestList]
    }

    def create(){
      Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      [company:company,
      quotationContractList:quotationContractList,

    }

    def save(QuotationPaymentRequestCommand quotationPaymentRequestCommand, session.company){
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      Company company = Company.get(session.company)
      def quotationRequest = QuotationRequest.get(params.quotation.toInteger())
      QuotationPaymentRequest quotationPaymentRequest = quotationPaymentRequestCommand.getQuotationPaymentRequest()
      if(quotationPaymentRequest.amount > quotationRequest.total){
        //throw new QuotationException("El saldo disponible es menor al monto en la solicitud de pagos")
        render view:'create', model:[company:company,
                                     quotationContractList:quotationContractList]
        return "El monto fue mayor al saldo disponible"
      }
      quotationRequest.total = quotationRequest.total - quotationPaymentRequest.amount
      quotationPaymentRequestService.create(quotationPaymentRequest)
      redirect(action: 'show', id: quotationPaymentRequest.id)
    }

    def update(QuotationPaymentRequestCommand quotationPaymentRequestCommand){
      QuotationPaymentRequest quotationPaymentRequestUpdate = quotationPaymentRequestCommand.getQuotationPaymentRequest()
      QuotationPaymentRequest quotationPaymentRequest = QuotationPaymentRequest.get(params.id.toLong())
      quotationPaymentRequest.amount = quotationPaymentRequestUpdate.amount
      quotationPaymentRequest.note = quotationPaymentRequestUpdate.note
      quotationPaymentRequest.paymentMethod = quotationPaymentRequestUpdate.paymentMethod
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
