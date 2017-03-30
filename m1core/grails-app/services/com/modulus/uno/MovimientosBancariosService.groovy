package com.modulus.uno

import grails.transaction.Transactional

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

  List<MovimientosBancarios> findBankingsTransactionsToConciliateForCompany(Company company) {
    MovimientosBancarios.findAllReconcilableByCuentaInListAndConciliationStatus(company.banksAccounts, ConciliationStatus.TO_APPLY, [sort:"dateEvent", order:"desc"])
  }

  void conciliateBankingTransaction(MovimientosBancarios bankingTransaction) {
    bankingTransaction.conciliationStatus = ConciliationStatus.APPLIED
    bankingTransaction.save()
  }
}
