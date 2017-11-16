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
      log.info "command: ${quotationContractCommand.dump()}"
      Company company = Company.get(session.company)
      def clients = businessEntityService.findBusinessEntityByKeyword("","CLIENT" , company)

      if (quotationContractCommand.hasErrors()) {
        render view:"create", model:[quotationContract:quotationContractCommand, company:company, clients:clients]
        return
      }

      QuotationContract quotationContract = quotationContractCommand.getQuotationContract(company)

      quotationContractService.create(quotationContract)

      if (quotationContract.hasErrors()){
        render view:"create", model:[quotationContract:quotationContract, company:company, clients:clients]
        return
      }

      redirect action: 'show', id:quotationContract.id
    }

    def edit(QuotationContract quotationContract) {
      [quotationContract:quotationContract]
    }

    def update(QuotationContractCommand quotationContractCommand) {
     Integer id = params.id.toInteger()
      quotationContractService.update(quotationContractCommand, id)
       redirect(action: 'edit', id: params.id)
    }

    def show(QuotationContract quotationContract) {
      Company company = Company.get(session.company)
      [quotationContract:quotationContract, company:company]
    }

    def balance(QuotationContract quotationContract){
      Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList =  QuotationContract.findAllByCompany(company)
      Map balance = quotationContractService.getBalance(quotationContract, 1)
      List<QuotationPaymentRequest> quotationPaymentRequestList = quotationContractService.getQuotationPaymentRequestList(quotationContract, new Date(), new Date())

      [balance:balance,
      quotationPaymentRequestList: quotationPaymentRequestList,
      quotationContractList: quotationContractList
      ]
    }

    def getQuotationPaymentRequest(){
      QuotationContract quotationContract = QuotationContract.get(params.id.toLong())
      Date firstDate = Date.parse( 'dd/MM/yyyy', params.initDate)
      Date lastDate = Date.parse('dd/MM/yyyy', params.lastDate)
      def saldoAnterior = quotationContractService.caculateData(quotationContract, firstDate)
      Map balance = quotationContractService.getBalance(quotationContract, saldoAnterior.saldoAnterior)
      List<QuotationPaymentRequest> quotationPaymentRequestList = quotationContractService.getQuotationPaymentRequestList(quotationContract, firstDate, lastDate)

      render view: 'balance', model:[balance:balance, quotationPaymentRequestList:quotationPaymentRequestList, saldoAnterior:saldoAnterior]
    }



}
