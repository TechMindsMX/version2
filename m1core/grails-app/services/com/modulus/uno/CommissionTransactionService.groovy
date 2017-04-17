package com.modulus.uno

import grails.transaction.Transactional
import java.math.RoundingMode

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

  CommissionTransaction registerCommissionForSaleOrder(SaleOrder order) {
    FeeCommand feeCommand = createFeeCommandForSaleOrder(order)
    def commission = saveCommissionTransaction(feeCommand)
    commission
  }

  private FeeCommand createFeeCommandForSaleOrder(SaleOrder saleOrder) {
    def command = null
    Commission commission = saleOrder.company.commissions.find { com ->
        com.type == CommissionType."FACTURA"
    }

    if (!commission) {
      throw new BusinessException("No existe comisión de facturación registrada")
    }

    BigDecimal amountFee = 0
    if (commission){
      if (commission.fee){
        amountFee = commission.fee * 1.0
      } else {
        amountFee = saleOrder.total * (commission.percentage/100)
      }
      command = new FeeCommand(companyId:saleOrder.company.id, amount:amountFee.setScale(2, RoundingMode.HALF_UP),type:commission.type)
    }
    command
  }

}

