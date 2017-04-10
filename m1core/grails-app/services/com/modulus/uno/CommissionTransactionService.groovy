package com.modulus.uno

import grails.transaction.Transactional

@Transactional
class CommissionTransactionService {

  def saveCommissionTransaction (FeeCommand feeCommand) {
    CommissionTransaction commissionTransaction = feeCommand.createCommissionTransaction()
    commissionTransaction.save()
    commissionTransaction
  }

  def getCommissionsPendingBalanceForCompany(Company company) {
    List balances = []
    company.commissions.sort{it.type}.each {
      Map balance = [typeCommission:it.type, balance: getCommissionsPendingBalanceForTypeAndCompany(it.type, company) ?: 0]
      balances.add(balance)
    }
    balances
  }

  BigDecimal getCommissionsPendingBalanceForTypeAndCompany(CommissionType type, Company company) {
    BigDecimal total = CommissionTransaction.createCriteria().get {
      and {
        eq("company", company)
        eq("type", type)
        eq("status", CommissionTransactionStatus.PENDING)
      }
      projections {
        sum "amount"
      }
    }
    total
  }

}

