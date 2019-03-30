package com.modulus.uno.messages

import com.budjb.rabbitmq.publisher.RabbitMessagePublisher

class SenderQueueService {

  RabbitMessagePublisher rabbitMessagePublisher

  def generateFacturaForSaleOrder(def saleOrderId) {
    rabbitMessagePublisher.send {
      routingKey = "modulus.uno"
      body = [saleOrderId: saleOrderId]
    }
  }
}
