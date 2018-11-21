package com.modulus.uno

import grails.transaction.Transactional
import com.modulus.uno.status.ConciliationStatus

@Transactional
class MovimientosBancariosService {

  def createMovimeintosBancariosFromList(MovimientosBancariosCommand command, BankAccount bankAccount) {
      MovimientosBancarios movimiento = command.createObjectByRow()
      movimiento.cuenta = bankAccount
      movimiento.save()
      movimiento
  }

  private def isValidList(def elementos) {
    def resultEachList = []
    elementos.each { row ->
      if (row.length() == 0)
        resultEachList << true
    }
    if (resultEachList.size() > 2)
      return false
    else
      return true
  }

  List<MovimientosBancarios> findBankingDepositsToConciliateForCompany(Company company) {
    MovimientosBancarios.findAllReconcilableByCuentaInListAndConciliationStatusAndType(company.banksAccounts, ConciliationStatus.TO_APPLY, MovimientoBancarioType.CREDITO, [sort:"dateEvent", order:"desc"])
  }

  void conciliateBankingTransaction(MovimientosBancarios bankingTransaction) {
    bankingTransaction.conciliationStatus = ConciliationStatus.APPLIED
    bankingTransaction.save()
  }

  BigDecimal getBalanceByCuentaPriorToDate(BankAccount bankAccount, Date date) {
    def totalDeposits = getTotalByCuentaAndTypePriorToDate(bankAccount, MovimientoBancarioType.CREDITO, date) ?: new BigDecimal(0)
    def totalWithDrawals = getTotalByCuentaAndTypePriorToDate(bankAccount, MovimientoBancarioType.DEBITO, date) ?: new BigDecimal(0)
    totalDeposits - totalWithDrawals
  }

  BigDecimal getTotalByCuentaAndTypePriorToDate(BankAccount bankAccount, MovimientoBancarioType type, Date date) {
    BigDecimal total = MovimientosBancarios.createCriteria().get {
      and {
        eq("cuenta", bankAccount)
        eq("type", type)
        lt("dateEvent", date)
      }
      projections {
        sum "amount"
      }
    }
    total
  }

  List<MovimientosBancarios> findBankingWithdrawsToConciliateForCompany(Company company) {
    MovimientosBancarios.findAllReconcilableByCuentaInListAndConciliationStatusAndType(company.banksAccounts, ConciliationStatus.TO_APPLY, MovimientoBancarioType.DEBITO, [sort:"dateEvent", order:"desc"])
  }

  List<MovimientosBancarios> findBankingDepositsForCompanyConciliated(Company company) {
    MovimientosBancarios.findAllReconcilableByCuentaInListAndConciliationStatusAndType(company.banksAccounts, ConciliationStatus.APPLIED, MovimientoBancarioType.CREDITO, [sort:"dateEvent", order:"desc"])
  }

  List<MovimientosBancarios> findBankingWithdrawsForCompanyConciliated(Company company) {
    MovimientosBancarios.findAllReconcilableByCuentaInListAndConciliationStatusAndType(company.banksAccounts, ConciliationStatus.APPLIED, MovimientoBancarioType.DEBITO, [sort:"dateEvent", order:"desc"])
  }

}
