package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.Company
import com.modulus.uno.Period
import com.modulus.uno.User
import com.modulus.uno.CorporateService
import com.modulus.uno.CollaboratorService

class QuotationContractService {

  CollaboratorService collaboratorService
  CorporateService corporateService

    @Transactional
    def create(QuotationContract quotationContract){
      quotationContract.save()
      log.info "QuotationContract: ${quotationContract.dump()}"
      quotationContract
    }

    @Transactional
    def update(QuotationContractCommand quotationContractCommand, Integer id){
      QuotationContract quotationContract = QuotationContract.get(id)
      quotationContract.commission = quotationContractCommand.getQuotationContract().commission
      quotationContract.initDate = quotationContractCommand.getQuotationContract().initDate
      quotationContract.save()
    }

    @Transactional
    def paymentPayed(QuotationPaymentRequest quotationPaymentRequest){
      def type
      Map typeProcess =[
                       'SEND': {respond -> type ="Enviand...."},
                       'PAYED':{respond -> type= "Pagando..."}
                       ]

      typeProcess['SEND']
    }

    Map getBalance(QuotationContract quotationContract, Map params){
      Period period = getPeriodForBalance(params)
      makeBalanceForContractAndPeriod(quotationContract, period)
    }

    List<QuotationBalanceGeneralConcept> getQuotationBalanceGeneralConceptForPeriod(Map params){
      Period period = getPeriodForBalance(params)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByStatusAndDateCreatedBetween(QuotationContractStatus.ACTIVE, period.init, period.end)
      List<QuotationBalanceGeneralConcept> quotationBalanceGeneralConceptList = quotationBalanceGeneralConcept(quotationContractList)
    }

    List<Map> getQuotationWithCommisionPeriod(Map params){
      Period period = getPeriodForBalance(params)
      List<QuotationContract> quotationContractList = QuotationContract.findAllByStatusAndDateCreatedBetween(QuotationContractStatus.ACTIVE, period.init, period.end)
      List<Map> quotationWithCommissionList = getQuotationWithCommision(quotationContractList)
    }

    Period getPeriodForPdf(Map params){
      Period period = getPeriodForBalance(params)
    }

    private Period getPeriodForBalance(Map params) {
      Period period
      if (params?.initDate && params?.lastDate) {
        period = collaboratorService.createPeriod(params.initDate, params.lastDate)
      } else {
        period = collaboratorService.getCurrentMonthPeriod()
      }
    }

    private Map makeBalanceForContractAndPeriod(QuotationContract quotationContract, Period period) {
      List<QuotationConcept> quotationConcepList = getConceptListForBalance(quotationContract, period)
      Map summaryBalance = calculateSummaryForBalance(quotationContract)
      [quotationContract:quotationContract,
        summary:summaryBalance,
        period:period,
        conceptList:quotationConcepList
      ]
    }

    List<QuotationConcept> getConceptListForBalance(QuotationContract quotationContract, Period period) {
      List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatusAndDateCreatedBetween(quotationContract, QuotationRequestStatus.PROCESSED, period.init, period.end)
      List<QuotationPaymentRequest> quotationPaymentRequestlistPayed = QuotationPaymentRequest.findAllByQuotationContractAndStatusAndDateCreatedBetween(quotationContract, QuotationPaymentRequestStatus.PAYED, period.init, period.end)
      List<QuotationConcept> conceptList = mergeList(quotationRequest,quotationPaymentRequestlistPayed)
      calculateBalancesForConcepts(conceptList, quotationContract, period)
    }

    Map calculateSummaryForBalance(QuotationContract quotationContract) {
      List<QuotationRequest> quotationRequest = QuotationRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationRequestStatus.PROCESSED)
      List<QuotationPaymentRequest> quotationPaymentRequestlistPayed = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.PAYED)
      List<QuotationPaymentRequest> quotationPaymentRequestlistSend = QuotationPaymentRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationPaymentRequestStatus.SEND)
      BigDecimal commission = calculateCommission(quotationRequest)
      BigDecimal income = quotationRequest*.total.sum() ?: 0
      BigDecimal transit = quotationPaymentRequestlistSend*.amount.sum() ?: 0
      BigDecimal expenses = quotationPaymentRequestlistPayed*.amount.sum() ?: 0
      BigDecimal available = income - transit - expenses - commission
      BigDecimal total = available + transit
      [
        income:income,
        transit:transit,
        expenses:expenses,
        available:available,
        total:total
      ]
    }

    List<QuotationBalanceGeneralConcept> quotationBalanceGeneralConcept(List<QuotationContract> quotationContractList){
      List<QuotationBalanceGeneralConcept> quotationBalanceGeneralConceptList = []
      quotationContractList.each{ quotation ->
        QuotationBalanceGeneralConcept quotationBalanceGeneralConcept = new QuotationBalanceGeneralConcept()
        quotationBalanceGeneralConcept.quotationContract = quotation
        Map summary = calculateSummaryForBalance(quotation)
        quotationBalanceGeneralConcept.request = summary.income
        quotationBalanceGeneralConcept.payment = summary.transit + summary.expenses
        quotationBalanceGeneralConcept.balance = (summary.income) - (summary.transit + summary.expenses)
        quotationBalanceGeneralConceptList << quotationBalanceGeneralConcept
      }
      quotationBalanceGeneralConceptList
    }

    List<QuotationConcept> calculateBalancesForConcepts(List<QuotationConcept> conceptList, QuotationContract quotationContract, Period period) {
      BigDecimal previousBalance = getPreviousBalance(quotationContract, period.init)
      conceptList.each { concept ->
         previousBalance = previousBalance + (concept.deposit?:0) - (concept.charge?:0)
         concept.balance = previousBalance
      }
      conceptList
    }

    BigDecimal getPreviousBalance(QuotationContract quotationContract, Date initDate){
      List<QuotationRequest> quotationRequestList = QuotationRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, QuotationRequestStatus.PROCESSED, initDate) 
      BigDecimal previosWithoutCommission = (quotationRequestList*.total.sum() ?: 0) -
      (QuotationPaymentRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, [QuotationPaymentRequestStatus.SEND, QuotationPaymentRequestStatus.PAYED], initDate)*.amount.sum() ?: 0)
      BigDecimal commission = calculateCommission(quotationRequestList)
      previosWithoutCommission - commission
    }

    List<QuotationConcept> mergeList(List<QuotationRequest> quotationRequestList, List<QuotationPaymentRequest> quotationPaymentRequestlistPayed){
      List<QuotationConcept> quotationConceptList = []
      quotationRequestList.each{ request ->
        QuotationConcept quotationConcept = new QuotationConcept()
        quotationConcept.concept = "Deposito"
        quotationConcept.date = request.dateCreated
        quotationConcept.deposit = request.total
        quotationConceptList << quotationConcept
        QuotationConcept quotationConceptCommission = new QuotationConcept() 
        QuotationCommission quotationCommission = QuotationCommission.findByQuotationRequest(request)
        quotationConceptCommission.concept = "Comisión"
        quotationConceptCommission.date = quotationCommission.dateCreated
        quotationConceptCommission.charge = (quotationCommission.amount * quotationCommission.commissionApply) / 100
        quotationConceptList << quotationConceptCommission
      }

      quotationPaymentRequestlistPayed.each{ paymentRequest ->
        QuotationConcept quotationConcept = new QuotationConcept()
        quotationConcept.concept = "Pago"
        quotationConcept.date = paymentRequest.dateCreated
        quotationConcept.charge = paymentRequest.amount
        quotationConceptList << quotationConcept
        
      }

      quotationConceptList.sort {it.date}
    }

    List<Map> getQuotationWithCommision(List<QuotationContract> quotationContractList){
      List<Map> quotationWithCommissionList = []
      quotationContractList.each(){ quotation ->

        Map summary = calculateFeeIncome(quotation)
        Map quotationWithCommission = [
          client: quotation.client,
          amount: summary.subtotal,
          commission: summary.commission,
          commissionAmount:summary.commission 
        ]
        quotationWithCommissionList << quotationWithCommission
      }
      quotationWithCommissionList
    }

    Map calculateFeeIncome(QuotationContract quotationContract){
      List<QuotationRequest> quotationRequestList = QuotationRequest.findAllByQuotationContractAndStatus(quotationContract, QuotationRequestStatus.PROCESSED)
      BigDecimal commission = calculateCommission(quotationRequestList) 
      [
        subtotal:quotationRequestList*.subtotal.sum() ?: 0,
        commission: commission 

      ]

    }

    BigDecimal calculateCommission(List<QuotationRequest> quotationRequestList){
      BigDecimal commission = 0
      quotationRequestList.each{ request -> 
        QuotationCommission quotationCommission = QuotationCommission.findByQuotationRequest(request)
        commission = commission + ((quotationCommission.amount * quotationCommission.commissionApply)/100)
      }
      commission
    }

    @Transactional
    def addUsersToQuotationContract(List<Integer> ids, QuotationContract quotationContract){
      List<User> users = User.getAll(ids)
      quotationContract.users.addAll(users)
      quotationContract.save()
    }

    @Transactional
    def removeOneUserOfQuotationContract(QuotationContract quotationContract, User user){
      quotationContract.users.remove(user)
      quotationContract.save()
    }

    def getListUsersForCorpotate(QuotationContract quotationContract, Company company){
      def corporate = corporateService.getCorporateFromCompany(company.id)
      List<User> users = corporateService.findCorporateUsers(corporate.id)
    }
}
