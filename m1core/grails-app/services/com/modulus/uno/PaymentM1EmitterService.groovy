package com.modulus.uno

class PaymentM1EmitterService {

  List<PaymentM1Emitter> getPaymentsInStatus(PaymentStatus status) {
    PaymentM1Emitter.findAllByStatus(status)
  }

}
