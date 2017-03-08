  package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

@Transactional
class LoanOrderHelperService {

  private final static Integer ONE_HUNDRED = 100
  private final static Integer DAYS_IN_ONE_YEAR = 365

  def grailsApplication
  def emailSenderService
  def springSecurityService
  def modulusUnoService

  def addAuthorizationToLoanPaymentOrder(LoanPaymentOrder loanPaymentOrder, User user) {
    Authorization authorization = new Authorization(user:user)
    loanPaymentOrder.addToAuthorizations(authorization)
    loanPaymentOrder.save flush:true
    loanPaymentOrder
  }

  def authorizeLoanPaymentOrder (LoanPaymentOrder loanPaymentOrder) {
    loanPaymentOrder.status = LoanPaymentOrderStatus.AUTHORIZED
    loanPaymentOrder.save flush:true
    emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
    loanPaymentOrder
  }

  BigDecimal computeCommissionPayment(LoanPaymentOrder loanPaymentOrder){
    BigDecimal payment = loanPaymentOrder.amountInterest + loanPaymentOrder.amountIvaInterest
    Commission commission = loanPaymentOrder.loanOrder.company.commissions.find {
      it.type == CommissionType.DEPOSITO
    }

    BigDecimal result = new BigDecimal(0)
    if (commission) {
      result = commission.percentage ? payment * commission.percentage / 100 : commission.fee
    } else {
      throw new CommissionException('No existe comisión definida para la operación')
    }

    result.setScale(2, RoundingMode.HALF_UP)
  }

  LoanPaymentOrder computeMinimumPayment(LoanOrder loanOrder) {
    LoanPaymentOrder result = new LoanPaymentOrder()
    result.loanOrder = loanOrder
    result.datePayment = new Date()
    result.daysPeriod = new Date() - getLastDatePayment(loanOrder)
    result.amountInterest = (getCurrentBalance(loanOrder) * (loanOrder.rate/DAYS_IN_ONE_YEAR/ONE_HUNDRED) * result.daysPeriod).setScale(2, RoundingMode.HALF_UP)
    result.amountIvaInterest = (result.amountInterest * grailsApplication.config.iva / 100).setScale(2, RoundingMode.HALF_UP)
    result.amountToCapital = (loanOrder.amount / loanOrder.term).setScale(2, RoundingMode.HALF_UP)
    result
  }

  private TransferFundsCommand createTransferFundsCommandFromLoanPaymentOrder(LoanPaymentOrder loanPaymentOrder) {
    TransferFundsCommand command = new TransferFundsCommand (uuidOrigin:loanPaymentOrder.company.accounts.first().timoneUuid, uuidDestination:loanPaymentOrder.loanOrder.company.accounts.first().timoneUuid, amount:loanPaymentOrder.totalPayment, fee:computeCommissionPayment(loanPaymentOrder), typeFee:"DEPOSIT_FEE")
    command
  }

  def executeLoanPaymentOrder (LoanPaymentOrder loanPaymentOrder) {
    modulusUnoService.applyTransferFunds(createTransferFundsCommandFromLoanPaymentOrder(loanPaymentOrder))
    loanPaymentOrder.status = LoanPaymentOrderStatus.EXECUTED
    loanPaymentOrder.save flush:true
    emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
    loanPaymentOrder
  }

  Boolean loanPaymentOrderInProcess (LoanOrder loanOrder) {
    loanOrder.payments.findAll {it.status == LoanPaymentOrderStatus.CREATED || it.status == LoanPaymentOrderStatus.VALIDATE || it.status == LoanPaymentOrderStatus.AUTHORIZED }.size() > 0
  }

  BigDecimal getCurrentBalance (LoanOrder loanOrder) {
    BigDecimal tot = loanOrder.payments ? loanOrder.payments.findAll { it.status != LoanPaymentOrderStatus.CANCELED && it.status != LoanPaymentOrderStatus.REJECTED }.sum {it.amountToCapital} : 0.0f
    loanOrder.amount - (tot ?: 0.0f)
  }

  Date getLastDatePayment (LoanOrder loanOrder) {
    Date lastDate = loanOrder.loanDate

    if (loanOrder.payments) {
      List<LoanPaymentOrder> loanPaymentList = loanOrder.payments.findAll {it.status != LoanPaymentOrderStatus.CANCELED && it.status != LoanPaymentOrderStatus.REJECTED}.sort {it.datePayment}
      if (loanPaymentList)
        lastDate = loanPaymentList.last().datePayment
    }

    lastDate
  }

  BigDecimal getTotalOrdersAuthorizedOfCompany(Company company){
    LoanPaymentOrder.findAllByCompanyAndStatus(company, LoanPaymentOrderStatus.AUTHORIZED).totalPayment.sum()
  }

  Boolean loanPaymentIsFullAuthorized(LoanPaymentOrder loanPaymentOrder){
    (loanPaymentOrder.authorizations?.size() ?: 0) >= loanPaymentOrder.company.numberOfAuthorizations
  }

  LoanPaymentOrder sendToAuthorize(LoanPaymentOrder loanPaymentOrder) {
    loanPaymentOrder.status = LoanPaymentOrderStatus.VALIDATE
    loanPaymentOrder.save flush:true
    emailSenderService.notifyLoanPaymentOrderChangeStatus(loanPaymentOrder)
    loanPaymentOrder
  }

}
