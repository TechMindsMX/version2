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

    def generalBalance(){
      Company company = Company.get(session.company)
      def detailGeneralBalance = [
                                  [client:"Brandon", totalRequest:10, totalPayments:5],
                                  [client:"Temoc", totalRequest:20, totalPayments:10],
                                  [client:"Luis", totalRequest:30, totalPayments:15]
                                  ]

      model:[company:company,
            detailGeneralBalance:detailGeneralBalance
            ]
    }

    def getBalanceGeneral(){

      def deatilBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(params)
      render view:'generalBalance', model:[deatilBalance:deatilBalance]
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

    def chooseClientForBalance(){
      Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList =  QuotationContract.findAllByCompany(company)
      render view:"balance", model:[quotationContractList:quotationContractList]
    }

    def balance(QuotationContract quotationContract){
      Map balance = quotationContractService.getBalance(quotationContract, params)
      render view: 'balance', model:[balance:balance]

    }



}
