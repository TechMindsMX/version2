package com.modulus.uno

import grails.transaction.Transactional

class StatusOrderStpService {

  @Transactional
  StatusOrderStp saveStatusOrderStp(StatusOrderStp statusOrderStp) {
    statusOrderStp.save()
    if (statusOrderStp.status == "DEVOLUCION") {
      log.info "Initializing reverse transaction"
      notify("reverseTransaction", statusOrderStp)
    }
    statusOrderStp
  }

}
