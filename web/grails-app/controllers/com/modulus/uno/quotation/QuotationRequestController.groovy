package com.modulus.uno.quotation

import com.modulus.uno.Company

class QuotationRequestController {

    QuotationRequestService quotationRequestService

    def index() {
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
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
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
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
      respond quotationRequest, model:[billers:quotationRequestService.getBillerCompanies(session.company.toLong())]
    }

    def edit(QuotationRequest quotationRequest){

      [quotationRequest:quotationRequest]
    }

    def update(QuotationRequestCommand quotationRequestCommand){
        QuotationRequest quotationRequestUpdate = quotationRequestCommand.getQuotationRequest()
        QuotationRequest quotationRequest = QuotationRequest.get(params.id.toInteger())
        quotationRequest.description = quotationRequestUpdate.description
        quotationRequest.commission = quotationRequestCommand.getCommission(quotationRequestCommand.commission)
        quotationRequest.total = quotationRequestUpdate.total
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

    def requestProcessed(QuotationRequestCommand quotationRequestCommand){
      QuotationRequest quotationRequestUpdate = quotationRequestCommand.getQuotationRequest()
      QuotationRequest quotationRequest= QuotationRequest.get(params.id.toInteger())
      quotationRequest.satConcept = SatConcept.values().find(){it.toString() == params.satConcept }
      quotationRequest.commission = quotationRequestCommand.getCommission(params.commission)
      quotationRequest.biller = Company.get(quotationRequestCommand.biller.toLong())
      quotationRequestService.requestProcessed(quotationRequest)
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
