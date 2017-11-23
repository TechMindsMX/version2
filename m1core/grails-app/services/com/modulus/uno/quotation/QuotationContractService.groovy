package com.modulus.uno.quotation

import grails.transaction.Transactional
import com.modulus.uno.Company
import com.modulus.uno.Period
import com.modulus.uno.CollaboratorService

class QuotationContractService {

  CollaboratorService collaboratorService

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

    private Period getPeriodForBalance(Map params) {
      Period period
      if (params.initDate && params.lastDate) {
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
      BigDecimal income = quotationRequest*.amount.sum() ?: 0
      BigDecimal transit = quotationPaymentRequestlistSend*.amount.sum() ?: 0
      BigDecimal expenses = quotationPaymentRequestlistPayed*.amount.sum() ?: 0
      BigDecimal available = income - transit - expenses
      BigDecimal total = available + transit
      [
        income:income,
        transit:transit,
        expenses:expenses,
        available:available,
        total:total
      ]
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
      (QuotationRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, QuotationRequestStatus.PROCESSED, initDate)*.amount.sum() ?: 0) -
      (QuotationPaymentRequest.findAllByQuotationContractAndStatusAndDateCreatedLessThan(quotationContract, [QuotationPaymentRequestStatus.SEND, QuotationPaymentRequestStatus.PAYED], initDate)*.amount.sum() ?: 0)
    }

    List<QuotationConcept> mergeList(List<QuotationRequest> quotationRequestList, List<QuotationPaymentRequest> quotationPaymentRequestlistPayed){
      List<QuotationConcept> quotationConceptList = []
      quotationRequestList.each{ request ->
        QuotationConcept quotationConcept = new QuotationConcept()
        quotationConcept.concept = "Deposito"
        quotationConcept.date = request.dateCreated
        quotationConcept.deposit = request.amount
        quotationConceptList << quotationConcept
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
}
