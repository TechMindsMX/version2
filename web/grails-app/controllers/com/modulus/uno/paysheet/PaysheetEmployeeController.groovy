package com.modulus.uno.paysheet

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PaysheetEmployeeController {

  def showPaysheetReceipts(PaysheetEmployee employee) {
    [employee:employee] 
  }

}


