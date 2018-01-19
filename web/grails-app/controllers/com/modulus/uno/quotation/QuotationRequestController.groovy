package com.modulus.uno.quotation

import com.modulus.uno.Company
import com.modulus.uno.Product

class QuotationRequestController {

    QuotationRequestService quotationRequestService
    QuotationContractService quotationContractService

    def index() {
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = quotationContractService.getListOfClientsFromTheCurrentUser(company)
      respond new QuotationContract(), model:[quotationContractList:quotationContractList,
       company:company
      ]
    }

    def chooseClient(QuotationContract quotationContract){
      def quotationRequestList = QuotationRequest.findAllByQuotationContract(quotationContract)
      render view:'index', model:[quotationRequestList:quotationRequestList, quotationContract:quotationContract]
    }


    def create(){
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = quotationContractService.getListOfClientsFromTheCurrentUser(company)
      BigDecimal ivaRate = quotationRequestService.getIvaCurrent()

      [company:company,
      quotationContractList:quotationContractList,
      ivaRate:ivaRate]
    }

    def save(QuotationRequestCommand quotationRequestCommand ){
      def quotationRequest = quotationRequestCommand.getQuotationRequest()
      quotationRequestService.create(quotationRequest)
      redirect(action: 'show', id: quotationRequest.id)
    }

    def show(QuotationRequest quotationRequest){
      List<Product> products = Product.getAll()
      respond quotationRequest, model:[billers:quotationRequestService.getBillerCompanies(session.company.toLong()), products:products]
    }

    def edit(QuotationRequest quotationRequest){
      BigDecimal ivaRate = quotationRequestService.getIvaCurrent()
      [quotationRequest:quotationRequest,
      ivaRate:ivaRate]
    }

    def update(QuotationRequestCommand quotationRequestCommand){
        QuotationRequest quotationRequestUpdate = quotationRequestCommand.getQuotationRequest()
        QuotationRequest quotationRequest = QuotationRequest.get(params.id.toInteger())
        quotationRequest.description = quotationRequestUpdate.description
        quotationRequest.commission = quotationRequestCommand.getCommission(quotationRequestCommand.commission)
        quotationRequest.total = quotationRequestUpdate.total
        quotationRequest.subtotal = quotationRequestUpdate.subtotal
        quotationRequest.iva = quotationRequestUpdate.iva
        quotationRequestService.update(quotationRequest)
      redirect(action: 'show', id: quotationRequest.id)
    }

    def delete(QuotationRequest quotationRequest){
      quotationRequestService.delete(quotationRequest)
      redirect(action: 'index')
    }

    def processed(){
      List<QuotationRequest> quotationRequestList = QuotationRequest.findAllByStatus(QuotationRequestStatus.PROCESSED)

      [quotationRequestList:quotationRequestList]
    }

    def processRequest(QuotationRequest quotationRequest){
      quotationRequest.commission = new QuotationRequestCommand().getCommission(params.commission)
      log.info "Quotation Request to updating: ${quotationRequest.dump()}"
      quotationRequestService.processRequest(quotationRequest)
      redirect(action: 'index')
    }

    def send(){
      List<QuotationRequest> quotationRequestList = QuotationRequest.findAllByStatus(QuotationRequestStatus.SEND)

      [quotationRequestList:quotationRequestList]
    }

    def sendQuotation(QuotationRequest quotationRequest){
      quotationRequestService.sendQuotation(quotationRequest)
      redirect(action: 'index')
    }

}
