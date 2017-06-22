package com.modulus.uno

import grails.validation.Validateable

class StatusOrderStpCommand implements Validateable {
  String id
  String empresa
  String folioOrigen
  String estado
  String causaDevolucion

  static constraints = {
    id nullable:false
    empresa nullable:false
    folioOrigen nullable:true, blank:true
    estado nullable:false
    causaDevolucion nullable:false
  }

  StatusOrderStp createStatusOrderStp() {

    StatusOrderStp statusOrderStp = new StatusOrderStp(
      keyTransaction:this.id,
      company:this.empresa,
      trackingKey:this.folioOrigen,
      status:this.estado,
      causeRefund:getCauseRefundStp(this.causaDevolucion)
    )
  }

  private CauseRefundStp getCauseRefundStp(String causeReceived) {
    def cause = causeReceived ?: "0"
    def causeFound = CauseRefundStp.values().find {it.id == Integer.parseInt(causa) }
    def causeRefund = causeFound ?: CauseRefundStp.UNKNOWN_CAUSE
  }

}
