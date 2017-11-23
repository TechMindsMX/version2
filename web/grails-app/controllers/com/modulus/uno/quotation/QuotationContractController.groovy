package com.modulus.uno.quotation
import  com.modulus.uno.BusinessEntityService
import com.modulus.uno.Period
import com.modulus.uno.CollaboratorService
import java.text.SimpleDateFormat

import com.modulus.uno.Company

class QuotationContractController {

    BusinessEntityService businessEntityService
    QuotationContractService quotationContractService
    Map paramsDate
    CollaboratorService collaboratorService

    def index(){
    	Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByCompany(company)
      respond new QuotationContract(), model:[quotationContractList: quotationContractList, company:company]
    }

    def feeIncome(){
      Company company = Company.get(session.company)
      List<Map> feeIncomes = quotationContractService.getQuotationWithCommisionPeriod(params)
      model:[company:company,
            feeIncomes:feeIncomes
            ]
    }

    def generalBalance(){
      paramsDate = params
      Company company = Company.get(session.company)
      def detailGeneralBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(params)
      model:[company:company,
            detailGeneralBalance:detailGeneralBalance
            ]
    }

    def pdfGeneralBalance(){
      Company company = Company.get(session.company)
      def detailGeneralBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(paramsDate)
      def period = quotationContractService.getPeriodForPdf(paramsDate)
      renderPdf(template: "/documentTemplates/quotation/quotationBalanceGeneral", model:[company:company, detailGeneralBalance:detailGeneralBalance, period:period])
    }

    def getBalanceGeneral(){
      paramsDate = params
      def detailGeneralBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(params)
      render view:'generalBalance', model:[detailGeneralBalance:detailGeneralBalance]
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
