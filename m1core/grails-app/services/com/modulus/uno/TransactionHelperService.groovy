package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.saleorder.SaleOrder

@Transactional
class TransactionHelperService {

  TransferFundsCommand generateCommandFromLoanOrder(LoanOrder loanOrder, Commission commission){

    BigDecimal fee = commission.fee ?: loanOrder.amount * commission.percentage / 100

    TransferFundsCommand command = new TransferFundsCommand(
      uuidOrigin:loanOrder.company.accounts[0].timoneUuid,
      uuidDestination: loanOrder.creditor.accounts[0].timoneUuid,
      amount: loanOrder.amount,
      fee: fee,
      typeFee:"LOAN_FEE"
    )
    command
  }

  CashoutCommand generateCommandFromSaleOrder(SaleOrder saleOrder, Commission commission){

    BigDecimal fee = commission.fee ?: saleOrder.amount * commission.percentage / 100

    CashoutCommand command = new CashoutCommand(
      uuid:saleOrder.company.accounts[0].timoneUuid,
      clabe: 'clabe',
      bankCode: 1,
      amount: loanOrder.amount,
      fee: fee,
      typeFee:"SALE_FEE"
    )
    command
  }

}
