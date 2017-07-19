package com.modulus.uno

class StatusOrderStpSwagger  {
  String id
  String empresa
  String folioOrigen
  String estado
  String causaDevolucion

  StatusOrderStp createStatusOrderStp() {
    new StatusOrderStp(
      keyTransaction:this.id,
      company:this.empresa,
      trackingKey:this.folioOrigen,
      status:this.estado,
      causeRefund:getCauseRefundStp(this.causaDevolucion)
      )
  }

  private CauseRefundStp getCauseRefundStp(String causeValue) {
    String causeReceived = causeValue ?: "0"
    CauseRefundStp causeFound = CauseRefundStp.values().find { it.id == Integer.parseInt(causeReceived) }
    CauseRefundStp causeRefund = causeFound ?: CauseRefundStp.UNKNOWN_CAUSE
    causeRefund
  }
}
