package com.modulus.uno.quotation
import  com.modulus.uno.BusinessEntityService
import com.modulus.uno.Period
import com.modulus.uno.CollaboratorService
import com.modulus.uno.User
import com.modulus.uno.Corporate
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
      def period = quotationContractService.getPeriodForPdf(params)
      List<Map> feeIncomes = quotationContractService.getQuotationWithCommisionPeriod(params)
      model:[company:company,
            feeIncomes:feeIncomes,
            period:period
            ]
    }

    def generalBalance(){
      paramsDate = params
      Company company = Company.get(session.company)
      def period = quotationContractService.getPeriodForPdf(params)
      def detailGeneralBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(params)
      model:[company:company,
            detailGeneralBalance:detailGeneralBalance,
            period:period
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
      def period = quotationContractService.getPeriodForPdf(params)
      def detailGeneralBalance = quotationContractService.getQuotationBalanceGeneralConceptForPeriod(params)
      render view:'generalBalance', model:[detailGeneralBalance:detailGeneralBalance,period:period]
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
       redirect(action: 'index', id: params.id)
    }

    def show(QuotationContract quotationContract) {
      Company company = Company.get(session.company)
      List<User> users = quotationContractService.getListUsersForCorpotate(quotationContract, company)
      def availableUsers = users - quotationContract.users
      [quotationContract:quotationContract, company:company, users:users, availableUsers:availableUsers]
    }

    def chooseClientForBalance(){
      Company company = Company.get(session.company)
      List<QuotationContract> quotationContractList =  QuotationContract.findAllByCompany(company)
      render view:"balance", model:[quotationContractList:quotationContractList]
    }

    def balance(QuotationContract quotationContract){
      def period = quotationContractService.getPeriodForPdf(params)
      Map balance = quotationContractService.getBalance(quotationContract, params)
      render view: 'balance', model:[balance:balance, period:period]

    }
    
    def addUsers(UserListCommand userListCommand){
      QuotationContract quotationContract = QuotationContract.get(params.quotationId.toInteger())
      quotationContractService.addUsersToQuotationContract(userListCommand.checkBe, quotationContract)
      redirect action: 'show', id:params.quotationId
    }

    def deleteUserFromQuotationContract(User user){
      QuotationContract quotationContract = QuotationContract.get(params.quotationId.toInteger())
      quotationContractService.removeOneUserOfQuotationContract(quotationContract, user)
      redirect action: 'show', id:params.quotationId
    }

}
