package com.modulus.uno

import grails.transaction.Transactional

class StatusOrderStpService {

  @Transactional
  StatusOrderStp saveStatusOrderStp(StatusOrderStp statusOrderStp) {
    statusOrderStp.save()
    //procesar notificación cuando el estatus es rechazado
    statusOrderStp
  }

}
