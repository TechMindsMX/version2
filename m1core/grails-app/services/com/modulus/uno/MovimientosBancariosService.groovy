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

}
